package com.hhy.util.barcode

import android.graphics.Bitmap
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
@SmallTest
class BarcodeTest {
    companion object {
        const val CONTENT = "kotlin"
        const val width = 200
        const val height = 200
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
    fun createQRCode_should_when() {
        val bitmap = createQRCode(CONTENT, width, height)
        assertEquals(width, bitmap.width)
        assertEquals(height, bitmap.height)

        println(bitmap.readQRCode())
    }

    @Test
    fun createQRCodeWithLogo_should_when() {
        val logoBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        val bitmap = createQRCodeWithIcon(CONTENT, width, height, logoBitmap)
        assertEquals(width, bitmap.width)
        assertEquals(height, bitmap.height)

        println(bitmap.readQRCode())
    }
}
