package com.hhy.util.thread

import android.os.Looper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.concurrent.thread
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
@SmallTest
class MainThreadTest {
    companion object {
        const val THREAD_NAME = "thread-x"
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
    fun mainLooper_shouldEqualsMainLooperInLooper() {
        assertEquals(Looper.getMainLooper(), mainLooper)
    }

    @Test
    fun mainThread_shouldEqualsThreadInMainLooper() {
        assertEquals(Looper.getMainLooper().thread, mainThread)
    }

    @Test
    fun isMainThread_shouldReturnTrue_whenInMainThread() {
        assertTrue { isMainThread }
    }

    @Test
    fun isMainThread_shouldReturnFalse_whenInOtherThread() {
        thread {
            assertFalse { isMainThread }
        }
    }

    @Test
    fun currentThread_shouldReturnCurrentThread() {
        assertSame(Thread.currentThread(), currentThread)
    }

    @Test
    fun currentThreadName_shouldReturnName() {
        thread(name = THREAD_NAME) {
            assertEquals(THREAD_NAME, currentThreadName)
        }
    }

    @Test
    fun checkMainThread_shouldRun_whenInMainThread() {
        checkMainThread()
    }

    @Test
    fun checkMainThread_shouldThrowsException_whenInOtherThread() {
        thread {
            assertFailsWith<Exception> {
                checkMainThread()
            }
        }
    }

    @Test
    fun checkNotMainThread_shouldThrowsException_whenInMainThread() {
        assertFailsWith<Exception> {
            checkNotMainThread()
        }
    }

    @Test
    fun checkNotMainThread_shouldRun_whenInOtherThread() {
        thread {
            checkNotMainThread()
        }
    }
}
