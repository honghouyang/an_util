package com.hhy.util.crypto

import android.util.Base64
import android.util.Base64.NO_WRAP
import com.hhy.util.config.Consts.DIGIT.LOWER
import com.hhy.util.config.Consts.DIGIT.UPPER
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.security.KeyFactory
import java.security.MessageDigest
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.Mac
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Converts an array of bytes into an array of characters representing
 * the hexadecimal values of each byte in order. The returned array will
 * be double the length of the passed array, as it takes two characters
 * to represent any given byte.
 *
 * @param toLowerCase `true` converts to lowercase, `false` to uppercase
 */
fun ByteArray.encodeHex(toLowerCase: Boolean = true): CharArray {
    val l = size
    val out = CharArray(l shl 1)

    val toDigits = if (toLowerCase) LOWER else UPPER

    var i = 0
    var j = 0
    while (i < l) {
        out[j++] = toDigits[(0xF0 and get(i).toInt()).ushr(4)]
        out[j++] = toDigits[0x0F and get(i).toInt()]
        i++
    }
    return out
}

/**
 * Calculate string's md5 value, 32 chars.
 */
fun String.md5(toLowerCase: Boolean = true): String {
    val digest = MessageDigest.getInstance("MD5")
    digest.update(toByteArray())
    val bytes = digest.digest()
    return String(bytes.encodeHex(toLowerCase))
}

/**
 * Calculate string's short md5 value, 16 chars.
 */
fun String.md5Short(toLowerCase: Boolean = true): String {
    return md5(toLowerCase).substring(8, 24)
}

/**
 * Calculate file's md5 value, 32 chars.
 */
fun File.md5(toLowerCase: Boolean = true): String {
    val bis = BufferedInputStream(FileInputStream(this))
    val digest = MessageDigest.getInstance("MD5")
    val bytes = digest.digest(bis.readBytes())
    return String(bytes.encodeHex(toLowerCase))
}

/**
 *  Calculate file's short md5 value, 16 chars.
 */
fun File.md5Short(toLowerCase: Boolean = true): String {
    return md5(toLowerCase).substring(8, 24)
}

/**
 * Encode string by base64, NO_WRAP mode.
 */
fun String.encodeBase64(): String {
    return Base64.encodeToString(toByteArray(), NO_WRAP)
}

/**
 * Decode string by base64, NO_WRAP mode.
 */
fun String.decodeBase64(): String {
    return String(Base64.decode(this, NO_WRAP))
}

/**
 * Encode string by aes.
 */
fun String.encodeAes(password: String): String {
    val secretKeySpec = SecretKeySpec(password.toByteArray(), "AES")
    val iv = ByteArray(16)
    val charArray = password.toCharArray()
    for (i in charArray.indices) {
        iv[i] = charArray[i].code.toByte()
    }
    val ivParameterSpec = IvParameterSpec(iv)

    val cipher = Cipher.getInstance("AES/GCM/NoPadding")
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec)

    val encryptedValue = cipher.doFinal(this.toByteArray())
    val str = Base64.encodeToString(encryptedValue, Base64.DEFAULT)
    return str.trim()
}

/**
 * Decode string by aes.
 */
fun String.decodeAes(password: String): String {
    val secretKeySpec = SecretKeySpec(password.toByteArray(), "AES")
    val iv = ByteArray(16)
    val charArray = password.toCharArray()
    for (i in charArray.indices) {
        iv[i] = charArray[i].code.toByte()
    }
    val ivParameterSpec = IvParameterSpec(iv)

    val cipher = Cipher.getInstance("AES/GCM/NoPadding")
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec)

    val decryptedByteValue = cipher.doFinal(Base64.decode(this, NO_WRAP))
    return String(decryptedByteValue)
}

/**
 * Encode string by SHA256.
 */
fun String.encodeSha256(toLowerCase: Boolean = true): String {
    val digest = MessageDigest.getInstance("SHA-256")
    val bytes = digest.digest(toByteArray())
    return String(bytes.encodeHex(toLowerCase))
}

/**
 * Encode string by HmacSHA256.
 */
fun String.encodeHmacSha256(key: String, toLowerCase: Boolean = true): String {
    val digest = Mac.getInstance("HmacSHA256")
    val secretKey = SecretKeySpec(key.toByteArray(), "HmacSHA256")
    digest.init(secretKey)
    val bytes = digest.doFinal(toByteArray())
    return String(bytes.encodeHex(toLowerCase))
}

/**
 * Encode string by RSA.
 */
fun String.encodeRsa(publicKey: String): String {
    // 得到公钥对象
    val keyFactory: KeyFactory = KeyFactory.getInstance("RSA")
    val keySpec = X509EncodedKeySpec(Base64.decode(publicKey, NO_WRAP))
    val pubKey: PublicKey = keyFactory.generatePublic(keySpec)
    // 加密数据
    val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
    cipher.init(Cipher.ENCRYPT_MODE, pubKey)
    val encryptedBytes = cipher.doFinal(this.toByteArray())
    return Base64.encodeToString(encryptedBytes, NO_WRAP)
}

/**
 * Decode string by RSA.
 */
fun String.decodeRsa(privateKey: String): String {
    // 得到私钥对象
    val keySpec = PKCS8EncodedKeySpec(Base64.decode(privateKey, NO_WRAP))
    val keyFactory = KeyFactory.getInstance("RSA")
    val priKey: PrivateKey = keyFactory.generatePrivate(keySpec)
    // 解密数据
    val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
    cipher.init(Cipher.DECRYPT_MODE, priKey)
    val decryptedBytes = cipher.doFinal(Base64.decode(this, NO_WRAP))
    return String(decryptedBytes)
}
