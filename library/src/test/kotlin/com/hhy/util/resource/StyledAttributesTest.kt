package com.hhy.util.resource

import android.content.Context
import android.content.res.Resources
import android.os.Looper
import androidx.test.filters.SmallTest
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.concurrent.thread
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@SmallTest
class StyledAttributesTest {
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun resolveThemeAttribute_shouldReturn0_whenInMainThreadAndResolveSuccess() {
        mockkStatic(Looper::class)
        every { Looper.getMainLooper() } returns mockk {
            every { thread } returns Thread.currentThread()
        }

        val context = mockk<Context> {
            every { theme.resolveAttribute(any(), any(), any()) } returns true
        }

        val value1 = context.resolveThemeAttribute(0)
        assertEquals(0, value1)

        val value2 = context.resolveThemeAttribute(0, false)
        assertEquals(0, value2)

        verify { context.theme.resolveAttribute(any(), any(), any()) }
    }

    @Test
    fun resolveThemeAttribute_shouldThrowException_whenInMainThreadAndResolveFailure() {
        mockkStatic(Looper::class)
        every { Looper.getMainLooper() } returns mockk {
            every { thread } returns Thread.currentThread()
        }

        val context = mockk<Context> {
            every { theme.resolveAttribute(any(), any(), any()) } returns false
        }

        assertFailsWith<Resources.NotFoundException> {
            context.resolveThemeAttribute(0)
        }

        assertFailsWith<Resources.NotFoundException> {
            context.resolveThemeAttribute(0, false)
        }

        verify { context.theme.resolveAttribute(any(), any(), any()) }
    }

    @Test
    fun resolveThemeAttribute_shouldReturn0_whenNotInMainThreadAndResolveSuccess() {
        mockkStatic(Looper::class)
        every { Looper.getMainLooper() } returns mockk {
            every { thread } returns mockk()
        }

        val context = mockk<Context> {
            every { theme.resolveAttribute(any(), any(), any()) } returns true
        }

        thread {
            val value1 = context.resolveThemeAttribute(0)
            assertEquals(0, value1)

            val value2 = context.resolveThemeAttribute(0, false)
            assertEquals(0, value2)

            verify { context.theme.resolveAttribute(any(), any(), any()) }
        }
    }

    @Test
    fun resolveThemeAttribute_shouldThrowException_whenNotInMainThreadAndResolveFailure() {
        mockkStatic(Looper::class)
        every { Looper.getMainLooper() } returns mockk {
            every { thread } returns mockk()
        }

        val context = mockk<Context> {
            every { theme.resolveAttribute(any(), any(), any()) } returns false
        }

        thread {
            assertFailsWith<Resources.NotFoundException> {
                context.resolveThemeAttribute(0)
            }

            assertFailsWith<Resources.NotFoundException> {
                context.resolveThemeAttribute(0, false)
            }

            verify { context.theme.resolveAttribute(any(), any(), any()) }
        }
    }
}
