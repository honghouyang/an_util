package com.hhy.util.dimension

import android.content.Context
import android.util.DisplayMetrics
import android.view.View
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
import kotlin.test.assertEquals

@SmallTest
class DimensionTest {
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun dip_shouldWorks_whenInContext() {
        val displayMetrics = DisplayMetrics()
        displayMetrics.density = 2.0f
        val context = mockk<Context> {
            every { resources.displayMetrics } returns displayMetrics
        }

        val value = context.dip(10)
        assertEquals(20, value)

        verify { context.resources.displayMetrics.density }
    }

    @Test
    fun dp_shouldWorks_whenInContext() {
        val displayMetrics = DisplayMetrics()
        displayMetrics.density = 2.5f
        val context = mockk<Context> {
            every { resources.displayMetrics } returns displayMetrics
        }

        val value = context.dp(10)
        assertEquals(25f, value)

        verify { context.resources.displayMetrics.density }
    }

    @Test
    fun dip_shouldWorks_whenInView() {
        val view = mockk<View>()
        mockkStatic("com.hhy.util.dimension.DimensionKt")
        every { view.context.dip(10) } returns 10

        assertEquals(10, view.dip(10))
    }

    @Test
    fun dp_shouldWorks_whenInView() {
        val view = mockk<View>()
        mockkStatic("com.hhy.util.dimension.DimensionKt")
        every { view.context.dp(any()) } returns 10f

        assertEquals(10f, view.dp(10))
    }
}
