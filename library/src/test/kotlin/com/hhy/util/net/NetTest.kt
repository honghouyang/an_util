@file:Suppress("DEPRECATION")

package com.hhy.util.net

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.os.Build.VERSION_CODES.M
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.hhy.util.appctx.injectAsAppCtx
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.shadows.ShadowNetworkCapabilities
import org.robolectric.shadows.ShadowNetworkInfo
import java.net.NetworkInterface
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
@SmallTest
class NetTest {
    lateinit var app: Application
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkCapabilities: NetworkCapabilities

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        app = ApplicationProvider.getApplicationContext()
        app.injectAsAppCtx()
        connectivityManager =
            app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        networkCapabilities = ShadowNetworkCapabilities.newInstance()
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun networkType_shouldReturnNone_whenNetworkUnavailable() {
        if (Build.VERSION.SDK_INT >= M) {
            shadowOf(networkCapabilities).addCapability(NetworkCapabilities.NET_CAPABILITY_MMS)
            shadowOf(connectivityManager).setNetworkCapabilities(
                connectivityManager.activeNetwork,
                networkCapabilities
            )
            assertEquals(NetworkType.NONE, networkType())

            shadowOf(connectivityManager).setNetworkCapabilities(
                connectivityManager.activeNetwork,
                null
            )
            assertEquals(NetworkType.NONE, networkType())
        } else {
            val shadowNetworkInfo = ShadowNetworkInfo.newInstance(
                NetworkInfo.DetailedState.DISCONNECTED,
                ConnectivityManager.TYPE_BLUETOOTH,
                0,
                false,
                NetworkInfo.State.DISCONNECTED
            )
            shadowOf(connectivityManager).setActiveNetworkInfo(shadowNetworkInfo)
            assertEquals(NetworkType.NONE, networkType())

            shadowOf(connectivityManager).setActiveNetworkInfo(null)
            assertEquals(NetworkType.NONE, networkType())
        }
    }

    @Test
    fun networkType_shouldReturnWifi_whenNetworkInWifi() {
        if (Build.VERSION.SDK_INT >= M) {
            shadowOf(networkCapabilities)
                .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            shadowOf(networkCapabilities).addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            shadowOf(connectivityManager).setNetworkCapabilities(
                connectivityManager.activeNetwork,
                networkCapabilities
            )
            assertEquals(NetworkType.WIFI, networkType())
        } else {
            val shadowNetworkInfo = ShadowNetworkInfo.newInstance(
                NetworkInfo.DetailedState.CONNECTED,
                ConnectivityManager.TYPE_WIFI,
                0,
                true,
                NetworkInfo.State.CONNECTED
            )
            shadowOf(connectivityManager).setActiveNetworkInfo(shadowNetworkInfo)
            assertEquals(NetworkType.WIFI, networkType())
        }
    }

    @Test
    fun networkType_shouldReturnMobile_whenNetworkInCellular() {
        if (Build.VERSION.SDK_INT >= M) {
            shadowOf(networkCapabilities)
                .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            shadowOf(networkCapabilities).addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            shadowOf(connectivityManager).setNetworkCapabilities(
                connectivityManager.activeNetwork,
                networkCapabilities
            )
            assertEquals(NetworkType.MOBILE, networkType())
        } else {
            val shadowNetworkInfo = ShadowNetworkInfo.newInstance(
                NetworkInfo.DetailedState.CONNECTED,
                ConnectivityManager.TYPE_MOBILE,
                0,
                true,
                NetworkInfo.State.CONNECTED
            )
            shadowOf(connectivityManager).setActiveNetworkInfo(shadowNetworkInfo)
            assertEquals(NetworkType.MOBILE, networkType())
        }
    }

    @Test
    fun networkType_shouldReturnEthernet_whenNetworkInEthernet() {
        if (Build.VERSION.SDK_INT >= M) {
            shadowOf(networkCapabilities)
                .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            shadowOf(networkCapabilities).addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
            shadowOf(connectivityManager).setNetworkCapabilities(
                connectivityManager.activeNetwork,
                networkCapabilities
            )
            assertEquals(NetworkType.ETHERNET, networkType())
        } else {
            val shadowNetworkInfo = ShadowNetworkInfo.newInstance(
                NetworkInfo.DetailedState.CONNECTED,
                ConnectivityManager.TYPE_ETHERNET,
                0,
                true,
                NetworkInfo.State.CONNECTED
            )
            shadowOf(connectivityManager).setActiveNetworkInfo(shadowNetworkInfo)
            assertEquals(NetworkType.ETHERNET, networkType())
        }
    }

    @Test
    fun networkType_shouldReturnOther_whenNetworkInBluetooth() {
        if (Build.VERSION.SDK_INT >= M) {
            shadowOf(networkCapabilities)
                .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            shadowOf(networkCapabilities).addTransportType(NetworkCapabilities.TRANSPORT_BLUETOOTH)
            shadowOf(connectivityManager).setNetworkCapabilities(
                connectivityManager.activeNetwork,
                networkCapabilities
            )
            assertEquals(NetworkType.OTHER, networkType())
        } else {
            val shadowNetworkInfo = ShadowNetworkInfo.newInstance(
                NetworkInfo.DetailedState.CONNECTED,
                ConnectivityManager.TYPE_BLUETOOTH,
                0,
                true,
                NetworkInfo.State.CONNECTED
            )
            shadowOf(connectivityManager).setActiveNetworkInfo(shadowNetworkInfo)
            assertEquals(NetworkType.OTHER, networkType())
        }
    }

    @Test
    fun netConnected_shouldReturnFalse_whenNetworkUnavailable() {
        if (Build.VERSION.SDK_INT >= M) {
            shadowOf(networkCapabilities).addCapability(NetworkCapabilities.NET_CAPABILITY_MMS)
            shadowOf(connectivityManager).setNetworkCapabilities(
                connectivityManager.activeNetwork,
                networkCapabilities
            )

            assertFalse {
                netConnected()
            }
        } else {
            val shadowNetworkInfo = ShadowNetworkInfo.newInstance(
                NetworkInfo.DetailedState.DISCONNECTED,
                ConnectivityManager.TYPE_BLUETOOTH,
                0,
                false,
                NetworkInfo.State.DISCONNECTED
            )
            shadowOf(connectivityManager).setActiveNetworkInfo(shadowNetworkInfo)

            assertFalse {
                netConnected()
            }
        }
    }

    @Test
    fun netConnected_shouldReturnTrue_whenNetworkAvailable() {
        if (Build.VERSION.SDK_INT >= M) {
            shadowOf(networkCapabilities)
                .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            shadowOf(networkCapabilities).addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            shadowOf(connectivityManager).setNetworkCapabilities(
                connectivityManager.activeNetwork,
                networkCapabilities
            )

            assertTrue {
                netConnected()
            }
        } else {
            val shadowNetworkInfo = ShadowNetworkInfo.newInstance(
                NetworkInfo.DetailedState.CONNECTED,
                ConnectivityManager.TYPE_WIFI,
                0,
                true,
                NetworkInfo.State.CONNECTED
            )
            shadowOf(connectivityManager).setActiveNetworkInfo(shadowNetworkInfo)

            assertTrue {
                netConnected()
            }
        }
    }

    @Test
    fun wifiConnected_shouldReturnFalse_whenNetworkUnavailable() {
        if (Build.VERSION.SDK_INT >= M) {
            shadowOf(networkCapabilities).addCapability(NetworkCapabilities.NET_CAPABILITY_MMS)
            shadowOf(connectivityManager).setNetworkCapabilities(
                connectivityManager.activeNetwork,
                networkCapabilities
            )

            assertFalse {
                wifiConnected()
            }
        } else {
            val shadowNetworkInfo = ShadowNetworkInfo.newInstance(
                NetworkInfo.DetailedState.DISCONNECTED,
                ConnectivityManager.TYPE_BLUETOOTH,
                0,
                false,
                NetworkInfo.State.DISCONNECTED
            )
            shadowOf(connectivityManager).setActiveNetworkInfo(shadowNetworkInfo)

            assertFalse {
                wifiConnected()
            }
        }
    }

    @Test
    fun wifiConnected_shouldReturnTrue_whenNetworkAvailable() {
        if (Build.VERSION.SDK_INT >= M) {
            shadowOf(networkCapabilities)
                .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            shadowOf(networkCapabilities).addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            shadowOf(connectivityManager).setNetworkCapabilities(
                connectivityManager.activeNetwork,
                networkCapabilities
            )

            assertTrue {
                wifiConnected()
            }
        } else {
            val shadowNetworkInfo = ShadowNetworkInfo.newInstance(
                NetworkInfo.DetailedState.CONNECTED,
                ConnectivityManager.TYPE_WIFI,
                0,
                true,
                NetworkInfo.State.CONNECTED
            )
            shadowOf(connectivityManager).setActiveNetworkInfo(shadowNetworkInfo)

            assertTrue {
                wifiConnected()
            }
        }
    }

    @Test
    fun mobileConnected_shouldReturnFalse_whenNetworkUnavailable() {
        if (Build.VERSION.SDK_INT >= M) {
            shadowOf(networkCapabilities).addCapability(NetworkCapabilities.NET_CAPABILITY_MMS)
            shadowOf(connectivityManager).setNetworkCapabilities(
                connectivityManager.activeNetwork,
                networkCapabilities
            )

            assertFalse {
                mobileConnected()
            }
        } else {
            val shadowNetworkInfo = ShadowNetworkInfo.newInstance(
                NetworkInfo.DetailedState.DISCONNECTED,
                ConnectivityManager.TYPE_BLUETOOTH,
                0,
                false,
                NetworkInfo.State.DISCONNECTED
            )
            shadowOf(connectivityManager).setActiveNetworkInfo(shadowNetworkInfo)

            assertFalse {
                mobileConnected()
            }
        }
    }

    @Test
    fun mobileConnected_shouldReturnTrue_whenNetworkAvailable() {
        if (Build.VERSION.SDK_INT >= M) {
            shadowOf(networkCapabilities)
                .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            shadowOf(networkCapabilities).addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            shadowOf(connectivityManager).setNetworkCapabilities(
                connectivityManager.activeNetwork,
                networkCapabilities
            )

            assertTrue {
                mobileConnected()
            }
        } else {
            val shadowNetworkInfo = ShadowNetworkInfo.newInstance(
                NetworkInfo.DetailedState.CONNECTED,
                ConnectivityManager.TYPE_MOBILE,
                0,
                true,
                NetworkInfo.State.CONNECTED
            )
            shadowOf(connectivityManager).setActiveNetworkInfo(shadowNetworkInfo)

            assertTrue {
                mobileConnected()
            }
        }
    }

    @Test
    fun ethernetConnected_shouldReturnFalse_whenNetworkUnavailable() {
        if (Build.VERSION.SDK_INT >= M) {
            shadowOf(networkCapabilities).addCapability(NetworkCapabilities.NET_CAPABILITY_MMS)
            shadowOf(connectivityManager).setNetworkCapabilities(
                connectivityManager.activeNetwork,
                networkCapabilities
            )

            assertFalse {
                ethernetConnected()
            }
        } else {
            val shadowNetworkInfo = ShadowNetworkInfo.newInstance(
                NetworkInfo.DetailedState.DISCONNECTED,
                ConnectivityManager.TYPE_BLUETOOTH,
                0,
                false,
                NetworkInfo.State.DISCONNECTED
            )
            shadowOf(connectivityManager).setActiveNetworkInfo(shadowNetworkInfo)

            assertFalse {
                ethernetConnected()
            }
        }
    }

    @Test
    fun ethernetConnected_shouldReturnTrue_whenNetworkAvailable() {
        if (Build.VERSION.SDK_INT >= M) {
            shadowOf(networkCapabilities)
                .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            shadowOf(networkCapabilities).addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
            shadowOf(connectivityManager).setNetworkCapabilities(
                connectivityManager.activeNetwork,
                networkCapabilities
            )

            assertTrue {
                ethernetConnected()
            }
        } else {
            val shadowNetworkInfo = ShadowNetworkInfo.newInstance(
                NetworkInfo.DetailedState.CONNECTED,
                ConnectivityManager.TYPE_ETHERNET,
                0,
                true,
                NetworkInfo.State.CONNECTED
            )
            shadowOf(connectivityManager).setActiveNetworkInfo(shadowNetworkInfo)

            assertTrue {
                ethernetConnected()
            }
        }
    }

    @Test
    fun ipAddress_shouldReturnIpv4Address_whenNetworkAvailable() {
        mockkStatic(NetworkInterface::class)
        every { NetworkInterface.getNetworkInterfaces() } returns mockk {
            every { hasMoreElements() } returns true andThen false
            every { nextElement() } returns mockk {
                every { isUp } returns true
                every { inetAddresses } returns mockk {
                    every { hasMoreElements() } returns true andThen false
                    every { nextElement() } returns mockk {
                        every { hostAddress } returns IPV4_ADDRESS
                    }
                }
            }
        }
        val ipv4 = ipAddress()
        println(ipv4)
        assertTrue(ipv4.matches(IPV4_PATTERN.toRegex()))
        assertEquals(IPV4_ADDRESS, ipv4)
    }

    @Test
    fun ipAddress_shouldReturnNonIpv4Address_whenNetworkAvailable() {
        mockkStatic(NetworkInterface::class)
        every { NetworkInterface.getNetworkInterfaces() } returns mockk {
            every { hasMoreElements() } returns true andThen false
            every { nextElement() } returns mockk {
                every { isUp } returns true
                every { inetAddresses } returns mockk {
                    every { hasMoreElements() } returns true andThen false
                    every { nextElement() } returns mockk {
                        every { hostAddress } returns IPV6_ADDRESS
                    }
                }
            }
        }
        val ipv6 = ipAddress(false)
        println(ipv6)
        assertTrue(ipv6.matches(IPV6_PATTERN.toRegex()))
        assertEquals(IPV6_ADDRESS.toUpperCase(), ipv6)
    }

    @Test
    fun ipAddress_shouldReturnNonIpv4AddressWithoutNetworkCard_whenNetworkAvailable() {
        mockkStatic(NetworkInterface::class)
        every { NetworkInterface.getNetworkInterfaces() } returns mockk {
            every { hasMoreElements() } returns true andThen false
            every { nextElement() } returns mockk {
                every { isUp } returns true
                every { inetAddresses } returns mockk {
                    every { hasMoreElements() } returns true andThen false
                    every { nextElement() } returns mockk {
                        every { hostAddress } returns IPV6_ADDRESS_WITH_NETWORK_CARD
                    }
                }
            }
        }
        val ipv6 = ipAddress(false)
        println(ipv6)
        assertTrue(ipv6.matches(IPV6_PATTERN.toRegex()))
        assertEquals(IPV6_ADDRESS.toUpperCase(), ipv6)
    }

    @Test
    fun ipAddress_shouldThrowException_whenNetworkUnavailable() {
        mockkStatic(NetworkInterface::class)
        every { NetworkInterface.getNetworkInterfaces() } returns mockk {
            every { hasMoreElements() } returns false
        }

        assertFailsWith<Exception> { ipAddress() }
    }

    companion object {
        const val IPV4_PATTERN =
            "^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}\$"
        const val IPV6_PATTERN =
            "^\\s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa" +
                "-f]{1,4}|((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1" +
                "-9]?\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|" +
                "2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(" +
                "([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5" +
                "]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:)" +
                ")|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:(" +
                "(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){" +
                "3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4})" +
                "{0,3}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]" +
                "?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f" +
                "]{1,4}){0,4}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\" +
                "d|[1-9]?\\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:(" +
                "(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){" +
                "3}))|:)))(%.+)?\\s*\$"

        const val IPV4_ADDRESS = "192.168.1.1"
        const val IPV6_ADDRESS = "fe80:0:0:0:a4e5:9e5c:9b44:2f36"
        const val IPV6_ADDRESS_WITH_NETWORK_CARD = "fe80:0:0:0:a4e5:9e5c:9b44:2f36%11/64"
    }
}
