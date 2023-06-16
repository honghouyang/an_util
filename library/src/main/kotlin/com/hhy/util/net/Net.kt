package com.hhy.util.net

import android.Manifest
import android.annotation.SuppressLint
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.annotation.VisibleForTesting
import com.hhy.util.exception.unsupported
import com.hhy.util.os.connectivityManager
import com.hhy.util.os.execCmd
import com.hhy.util.os.osVerCode
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.LinkedList
import java.util.Locale

enum class NetworkType {
    NONE,
    WIFI,
    MOBILE,
    ETHERNET,
    OTHER
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Suppress("DEPRECATION")
@SuppressLint("NewApi")
fun networkType(): NetworkType {
    if (osVerCode >= Build.VERSION_CODES.M) {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities == null ||
            !capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        ) return NetworkType.NONE
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkType.WIFI
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkType.MOBILE
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ->
                NetworkType.ETHERNET
            else -> NetworkType.OTHER
        }
    } else {
        val info = connectivityManager.activeNetworkInfo
        if (info == null || !info.isConnected) {
            return NetworkType.NONE
        }
        return when (info.type) {
            ConnectivityManager.TYPE_WIFI -> NetworkType.WIFI
            ConnectivityManager.TYPE_MOBILE -> NetworkType.MOBILE
            ConnectivityManager.TYPE_ETHERNET -> NetworkType.ETHERNET
            else -> NetworkType.OTHER
        }
    }
}

/** Check if the network is connected. */
fun netConnected(): Boolean {
    return networkType() != NetworkType.NONE
}

/** Check if it is connected to WiFi. */
fun wifiConnected(): Boolean {
    return networkType() == NetworkType.WIFI
}

/** Check if it is connected to mobile. */
fun mobileConnected(): Boolean {
    return networkType() == NetworkType.MOBILE
}

/** Check if it is connected to ethernet. */
fun ethernetConnected(): Boolean {
    return networkType() == NetworkType.ETHERNET
}

/** Ping host address. */
fun ping(host: String, times: Int, timeout: Int): String {
    val cmd = "ping -c $times -W $timeout $host"
    val cmdResult = execCmd(cmd, outputMsg = true)
    return cmdResult.toString()
}

/**
 * Return the ip address.
 * If you don't wanna return ipv4, you can make parameter [useIpv4] to be 'false'.
 * Must hold `<uses-permission android:name="android.permission.INTERNET" />`
 */
@RequiresPermission(Manifest.permission.INTERNET)
fun ipAddress(useIpv4: Boolean = true): String {
    findInetAddresses().forEach { add ->
        if (!add.isLoopbackAddress) {
            val hostAddress = add.hostAddress
            val isIpv4 = hostAddress.indexOf(':') < 0
            if (useIpv4) {
                if (isIpv4) {
                    return hostAddress
                }
            } else {
                if (!isIpv4) {
                    return nonIpv4Address(hostAddress)
                }
            }
        }
    }
    unsupported("Couldn't find ip address.")
}

private fun findInetAddresses(): LinkedList<InetAddress> {
    val nis = NetworkInterface.getNetworkInterfaces()
    val adds = LinkedList<InetAddress>()
    while (nis.hasMoreElements()) {
        val ni = nis.nextElement()
        // To prevent phone of xiaomi return "10.0.2.15"
        if (!ni.isUp || ni.isLoopback) continue
        val addresses = ni.inetAddresses
        while (addresses.hasMoreElements()) {
            adds.addFirst(addresses.nextElement())
        }
    }
    return adds
}

private fun nonIpv4Address(hostAddress: String): String {
    val index = hostAddress.indexOf('%')
    return if (index < 0) {
        hostAddress.uppercase(Locale.US)
    } else {
        hostAddress.substring(0, index).uppercase(Locale.US)
    }
}
