package com.hhy.util.crypto

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.hhy.util.appctx.appCtx
import com.hhy.util.file.FileTest
import com.hhy.util.file.create
import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
@SmallTest
class CryptoTest {
    companion object {
        val SOURCE_BYTES = byteArrayOf(0x0A, 0x0B, 0x0C, 0x0D)
        const val SOURCE_TEXT = "kotlin 非常好用"
        const val SOURCE_KEY = "hi"
        const val SOURCE_TEXT_MD5 = "0b8281a0583d47126fde3f9bc0683d5e"
        const val SOURCE_TEXT_MD5_SHORT = "583d47126fde3f9b"
        const val SOURCE_TEXT_AES = "DQgj6iPdYSZFNm+Xl876f2SExc6UNCKSpd9ZHQjrOxb6Zxw="
        const val SOURCE_TEXT_BASE64 = "a290bGluIOmdnuW4uOWlveeUqA=="
        const val SOURCE_TEXT_SHA256 =
            "b81bf92092915cf67f008a4400f47f3d3ad6105152765ae4138277130f825fbe"
        const val SOURCE_TEXT_HMAC_SHA256 =
            "e1b029334ca394a90aac2f30a237a8b90d10da58be337647f46d99d2dae7622a"
        const val PUBLIC_KEY_RSA = """
            MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvLziJIdl6QPM4RJQBzJW
            vWGyfn+gWD/BPkXZB/z3Ud6PhBC6C5PvNz0pH5qc0KOfXZIluTzQ4c03VOxz9/CQ
            TvOlh52sSd/+LDBiMaDT/VuZqnvSLCFz0Wxhh5oMUxGL0kRLHZmm9zMCIlFmZo3n
            2B3pvhRKYGE08YCnPpA+jBL/o4cUkLO/izdLPACALTl/AANcXB3riUzH9N41Q8oi
            p6ZlGgfo5eN95VRMypRrEgmnC+qLTpDb3ga21w56uLwacR7KxmXBtQOcmyeFJI62
            dXRI833fJZney93X7Pb8lOUMxwj8Y4Ipqxx4kmTcKYUImtEWA65Y0nxA/vIPumDW
            nwIDAQAB
            """
        const val PRIVATE_KEY_RSA = """
            MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC8vOIkh2XpA8zh
            ElAHMla9YbJ+f6BYP8E+RdkH/PdR3o+EELoLk+83PSkfmpzQo59dkiW5PNDhzTdU
            7HP38JBO86WHnaxJ3/4sMGIxoNP9W5mqe9IsIXPRbGGHmgxTEYvSREsdmab3MwIi
            UWZmjefYHem+FEpgYTTxgKc+kD6MEv+jhxSQs7+LN0s8AIAtOX8AA1xcHeuJTMf0
            3jVDyiKnpmUaB+jl433lVEzKlGsSCacL6otOkNveBrbXDnq4vBpxHsrGZcG1A5yb
            J4UkjrZ1dEjzfd8lmd7L3dfs9vyU5QzHCPxjgimrHHiSZNwphQia0RYDrljSfED+
            8g+6YNafAgMBAAECggEAQS+Pf4RQG2GXomi7wTCOqswSZQRZcgFbEdH3+n1RQjJ3
            KOrKBvOqHE8AZAz3ekv43815P/gUQNiq8qbUCt9ISCx4kFVRyxYU6FgxoehlL00y
            zqIDXgCTytH/AWGgPhSCUTYLJ4cJFrnEU+v/TQ5X89mpAGBrq8wHCB4iJUaWg7P3
            4kjim6iA5fbaFwE62Cc1JR+K3UqZizAjfgl80fsl6k+drjBMRdOfjuIdUTxe88/l
            bSj3XgVOv58mK4xziVk7vp/sbzjjlZ6wwXFIh3LvqFVocaCeUzUnRAqXldvWRA0a
            LPRPNF+B6RVeobe2+Sgt4r4riJXvu8j3CVD50t/w2QKBgQDsLleOD3jzGlBLwMrK
            A/7vF1i1znMaZbB69fR0jlxSQcikvngygbnxs9ZtVkNXC+wJ2eWLg+IDokFwa6QV
            3YYy7DszDwYW3jtCh8m8n45Vj3O7cUxIc2iSBwodEkG2yDI7hxn1YaaXDfoAlWHD
            4C6tgcbCIFBt8bjDy/tN07GM5QKBgQDMk1z1ZHS7pt1iSU6Mwy3EasSkFBGuubVD
            7zze9//fG1+Cm5Nk75HMUJkAysMOtMbGF2chH0jsGxYKo4dEaQ/pziiPUpwnrA/c
            N6XrEwmYSpeCPlVfr8RP6SrdoJIBxY7K8CuxJVzgNFhXUB/wS8oz7rTpfLlaxSjW
            2YCW/jNhMwKBgQDp0SMq59R0z9soc9APWQ9cwj83lhBL1Np3kOGXNozdfnTV7B7w
            2Y8tYz3odpsqJQpj4k0m5sStlke0tUv3vZyq80XUUY5Uu2PthKLK0ydFO0R3ED5W
            B7NBxihUCYv3GZDpb+HaFPeo2dRkGX91PI7ZhP4xl24oukKRzHKZZPeEpQKBgQCN
            YI3OfIT7pf66zOR+/OelJRAOQFxT/r0ZXL0ZbhQjMHyZ+0AqcjG1HEIQvQ9+G0p/
            XW8FAeTYXoOmCIsTqPO5ZaK8IaAO/qbbTo3YXgi0eWApP0Cq5wJpIz9feHdOyCO9
            dasMOkDsJOCkPphD7Wyo5R+beHl0FGr7x8ZJExWY5wKBgGngrQRQ+7PAAJok+UZh
            GYzxk3WLGPyv//7MXmd6vDsccUyzhJH7LErgArEvRM66XOcPsNTKGd7nBvyfjISF
            ccGgBleLsVxY215dYkSywAntcjgc7bMpI488FC6z4VIoMZEPbDwv0/5QkfVhK58g
            hpldpHqWH3KcFbkGVhk4wcc9
            """
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun encodeHex_shouldChangeByteArrayToCharArray() {
        val chars = SOURCE_BYTES.encodeHex()
        assertTrue {
            chars.size == SOURCE_BYTES.size * 2
        }
        var firstChar = '0'
        for ((index, char) in chars.withIndex()) {
            if (index % 2 != 0) {
                assertEquals(
                    SOURCE_BYTES[(index - 1) / 2].toInt(),
                    (firstChar.toString() + char.toString()).toInt(16)
                )
            } else {
                firstChar = char
            }
        }
    }

    @Test
    fun encodeHex_shouldBeUpperCase_whenToLowerCaseIsFalse() {
        val chars = SOURCE_BYTES.encodeHex(false)
        for (char in chars) {
            if (char.isLetter()) {
                assertTrue { char.isUpperCase() }
            }
        }
    }

    @Test
    fun md5_shouldReturnMD5InFullMode() {
        repeat(5) {
            assertEquals(SOURCE_TEXT_MD5, SOURCE_TEXT.md5())
        }
    }

    @Test
    fun md5_shouldBeUpperCase_whenToLowerCaseIsFalse() {
        val chars = SOURCE_TEXT.md5(false)
        for (char in chars) {
            if (char.isLetter()) {
                assertTrue { char.isUpperCase() }
            }
        }
    }

    @Test
    fun md5Short_shouldReturnMD5InFullMode() {
        repeat(5) {
            assertEquals(SOURCE_TEXT_MD5_SHORT, SOURCE_TEXT.md5Short())
        }
    }

    @Test
    fun md5Short_shouldBeUpperCase_whenToLowerCaseIsFalse() {
        val chars = SOURCE_TEXT.md5Short(false)
        for (char in chars) {
            if (char.isLetter()) {
                assertTrue { char.isUpperCase() }
            }
        }
    }

    @Test
    fun fileToMd5_shouldReturnMD5InFullMode() {
        val file = File(appCtx.getExternalFilesDir(null), FileTest.FILE_A_NAME)
        file.create()
        file.writeText(SOURCE_TEXT)

        repeat(5) {
            assertEquals(SOURCE_TEXT_MD5, file.md5())
        }
    }

    @Test
    fun fileTomMd5Short_shouldReturnMD5InFullMode() {
        val file = File(appCtx.getExternalFilesDir(null), FileTest.FILE_A_NAME)
        file.create()
        file.writeText(SOURCE_TEXT)

        repeat(5) {
            assertEquals(SOURCE_TEXT_MD5_SHORT, file.md5Short())
        }
    }

    @Test
    fun fileTomMd5Short_shouldBeUpperCase_whenToLowerCaseIsFalse() {
        val file = File(appCtx.getExternalFilesDir(null), FileTest.FILE_A_NAME)
        file.create()
        file.writeText(SOURCE_TEXT)

        val chars = file.md5Short(false)
        for (char in chars) {
            if (char.isLetter()) {
                assertTrue { char.isUpperCase() }
            }
        }
    }

    @Test
    fun encodeAes_shouldWorks() {
        repeat(5) {
            assertEquals(SOURCE_TEXT_AES, SOURCE_TEXT.encodeAes(SOURCE_TEXT_MD5_SHORT))
        }
    }

    @Test
    fun decodeAes_shouldWorks() {
        repeat(5) {
            assertEquals(SOURCE_TEXT, SOURCE_TEXT_AES.decodeAes(SOURCE_TEXT_MD5_SHORT))
        }
    }

    @Test
    fun encodeBase64_shouldWorks() {
        repeat(5) {
            assertEquals(SOURCE_TEXT_BASE64, SOURCE_TEXT.encodeBase64())
        }
    }

    @Test
    fun decodeBase64_shouldWorks() {
        repeat(5) {
            assertEquals(SOURCE_TEXT, SOURCE_TEXT_BASE64.decodeBase64())
        }
    }

    @Test
    fun encodeSha256_shouldWorks() {
        repeat(5) {
            assertEquals(SOURCE_TEXT_SHA256, SOURCE_TEXT.encodeSha256())
        }
    }

    @Test
    fun encodeHmacSha256_shouldWorks() {
        repeat(5) {
            assertEquals(SOURCE_TEXT_HMAC_SHA256, SOURCE_TEXT.encodeHmacSha256(SOURCE_KEY))
        }
    }

    @Test
    fun encodeAndDecodeRSA_shouldWorks() {
        repeat(5) {
            assertEquals(
                SOURCE_TEXT,
                SOURCE_TEXT.encodeRsa(PUBLIC_KEY_RSA).decodeRsa(PRIVATE_KEY_RSA)
            )
        }
    }
}
