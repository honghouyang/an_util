package com.hhy.util.exception

import android.util.Log.getStackTraceString
import androidx.test.filters.SmallTest
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@SmallTest
class ExceptionTest {
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun unexpectedValue_shouldThrowIllegalStateException_withErrorMsg() {
        val value = "time"
        val exception = assertFailsWith<IllegalStateException> {
            unexpectedValue(
                value
            )
        }
        assertNotNull(exception.message)
        assertTrue {
            val message = exception.message!!
            message.contains(value)
        }
    }

    @Test
    fun illegalArg_shouldThrowIllegalArgumentException_withErrorMsg() {
        val arg = "time"
        val errorMsg = "value can't be bigger than now"
        val exception = assertFailsWith<IllegalArgumentException> {
            illegalArg(
                arg,
                errorMsg
            )
        }
        assertNotNull(exception.message)
        assertTrue {
            val message = exception.message!!
            message.contains(arg) && message.contains(errorMsg)
        }
    }

    @Test
    fun illegalArg_shouldThrowIllegalArgumentException_withoutErrorMsg() {
        val arg = "time"
        val exception = assertFailsWith<IllegalArgumentException> { illegalArg(arg) }
        assertNotNull(exception.message)
        assertTrue {
            val message = exception.message!!
            message.contains(arg)
        }
    }

    @Test
    fun unsupported_shouldThrowUnsupportedOperationException_withErrorMsg() {
        val errorMsg = "This operation is not supported."
        val exception = assertFailsWith<UnsupportedOperationException> {
            unsupported(
                errorMsg
            )
        }
        assertNotNull(exception.message)
        assertTrue {
            val message = exception.message!!
            message == errorMsg
        }
    }

    @Test
    fun unsupported_shouldThrowUnsupportedOperationException_withoutErrorMsg() {
        val exception = assertFailsWith<UnsupportedOperationException> { unsupported() }
        assertNull(exception.message)
    }

    @Test
    fun throwableMsg_shouldBeSame_withMessage() {
        val exception = Exception("kotlin")
        assertEquals(exception.message, exception.msg)
    }

    @Test
    fun throwableMsg_shouldBeSimpleName_withoutMessage() {
        val exception = Exception()
        assertEquals(exception.javaClass.simpleName, exception.msg)
    }

    @Test
    fun throwableStackTraceMsg_shouldGetStackTrace() {
        val exception = Exception("kotlin")
        mockkStatic("android.util.Log")
        val stackTraceMsg = "stub"
        every { getStackTraceString(exception) } returns stackTraceMsg

        val msg = exception.stackTraceMsg

        assertEquals(stackTraceMsg, msg)
        verify { getStackTraceString(exception) }
    }
}
