package com.hhy.util.io

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.hhy.util.appctx.appCtx
import com.hhy.util.appctx.injectAsAppCtx
import com.hhy.util.exception.unsupported
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class IOTest {
    companion object {
        const val DIR_NAME = "language"
        const val FILE_NAME = "cn"
        const val CONTENT = "Hello, Kotlin!"
    }

    lateinit var app: Application
    lateinit var file: File

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        app = ApplicationProvider.getApplicationContext()
        app.injectAsAppCtx()
        val dir = File(appCtx.getExternalFilesDir(null), DIR_NAME)
        dir.mkdirs()
        file = File(dir, FILE_NAME)
        file.writeText(CONTENT)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun doSafe_shouldOperateCloseable_whenInNormal() {
        val reader = file.bufferedReader()
        reader.doSafe {
            val content = readText()
            assertEquals(CONTENT, content)
        }
        assertFailsWith<Exception> {
            reader.readText()
        }
    }

    @Test
    fun doSafe_shouldAutoCloseCloseable_whenAfterOperation() {
        val reader = file.bufferedReader()
        reader.doSafe { }
        assertFailsWith<Exception> {
            reader.readText()
        }
    }

    @Test
    fun doSafe_shouldAutoCloseCloseable_whenIOErrorOccurs() {
        val reader = file.bufferedReader()
        reader.doSafe {
            throw IOException()
        }
        assertFailsWith<Exception> {
            reader.readText()
        }
    }

    @Test
    fun closeQuietly_shouldBeAble_whenCloseableIsNull() {
        val reader: BufferedReader? = null
        reader.closeQuietly()
    }

    @Test
    fun closeQuietly_shouldBeOk_whenErrorOccurs() {
        val inputStream = mockk<FileInputStream> {
            every { close() } throws Exception()
        }
        inputStream.closeQuietly()
    }

    @Test
    fun use_shouldCloseAllCloseable_whenOperationDone() {
        val inputStreamA = mockk<FileInputStream> {
            justRun { close() }
        }
        val inputStreamB = mockk<FileInputStream> {
            justRun { close() }
        }

        val inputs = arrayOf(inputStreamA, inputStreamB)
        inputs.use { }

        verify { inputStreamA.close() }
        verify { inputStreamB.close() }
    }

    @Test
    fun use_shouldCloseAllCloseable_whenErrorOccurs() {
        val inputStreamA = mockk<FileInputStream> {
            justRun { close() }
        }
        val inputStreamB = mockk<FileInputStream> {
            every { close() } throws Exception()
        }

        val inputStreamC: FileInputStream? = null

        val inputs = arrayOf(inputStreamA, inputStreamB, inputStreamC)
        try {
            inputs.use {
                unsupported()
            }
        } catch (ignored: Exception) {
        }

        verify { inputStreamA.close() }
        verify { inputStreamB.close() }
    }
}
