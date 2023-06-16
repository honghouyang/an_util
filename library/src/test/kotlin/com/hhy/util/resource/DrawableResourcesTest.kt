package com.hhy.util.resource

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.test.filters.SmallTest
import com.hhy.util.appctx.injectAsAppCtx
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@SmallTest
class DrawableResourcesTest {
    companion object {
        const val RES_ID = 1
    }

    private lateinit var colorDrawable: ColorDrawable

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        colorDrawable = ColorDrawable()
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun drawable_shouldReturnDrawable_whenInContext() {
        val context = spyk<Context>()
        mockkStatic(ResourcesCompat::class)
        every { ResourcesCompat.getDrawable(any(), any(), any()) } returns colorDrawable
        assertEquals(colorDrawable, context.drawable(RES_ID))
        verify { ResourcesCompat.getDrawable(any(), any(), any()) }
    }

    @Test
    fun drawable_shouldReturnDrawable_whenInFragment() {
        mockkStatic("com.hhy.util.resource.DrawableResourcesKt")
        val fragment = mockk<Fragment> {
            every { requireContext() } returns mockk {
                every { drawable(any()) } returns colorDrawable
            }
        }
        val drawable = fragment.drawable(RES_ID)
        assertEquals(colorDrawable, drawable)
        verify { fragment.drawable(any()) }
    }

    @Test
    fun drawable_shouldReturnDrawable_whenInView() {
        mockkStatic("com.hhy.util.resource.DrawableResourcesKt")
        val view = mockk<View> {
            every { context } returns mockk {
                every { drawable(any()) } returns colorDrawable
            }
        }
        val drawable = view.drawable(RES_ID)
        assertEquals(colorDrawable, drawable)
        verify { view.drawable(any()) }
    }

    @Test
    fun drawable_shouldReturnDrawable_whenInGlobal() {
        mockkStatic("com.hhy.util.resource.DrawableResourcesKt")
        val context = mockk<Context> {
            every { drawable(any()) } returns colorDrawable
        }
        context.injectAsAppCtx()
        val drawable = appDrawable(RES_ID) as ColorDrawable
        assertEquals(colorDrawable, drawable)
        verify { context.drawable(any()) }
    }

    @Test
    fun styledDrawable_shouldReturnDrawable_whenInContext() {
        val context = spyk<Context>()
        mockkStatic("com.hhy.util.resource.StyledAttributesKt")
        every { context.resolveThemeAttribute(any()) } returns RES_ID

        mockkStatic("com.hhy.util.resource.DrawableResourcesKt")
        every { context.drawable(any()) } returns colorDrawable

        assertEquals(colorDrawable, context.styledDrawable(RES_ID))
        verify { context.drawable(any()) }
    }

    @Test
    fun styledDrawable_shouldReturnDrawable_whenInFragment() {
        mockkStatic("com.hhy.util.resource.DrawableResourcesKt")
        val fragment = mockk<Fragment> {
            every { requireContext() } returns mockk {
                every { styledDrawable(any()) } returns colorDrawable
            }
        }
        val drawable = fragment.styledDrawable(RES_ID)
        assertEquals(colorDrawable, drawable)
        verify { fragment.styledDrawable(any()) }
    }

    @Test
    fun styledDrawable_shouldReturnDrawable_whenInView() {
        mockkStatic("com.hhy.util.resource.DrawableResourcesKt")
        val view = mockk<View> {
            every { context } returns mockk {
                every { styledDrawable(any()) } returns colorDrawable
            }
        }
        val drawable = view.styledDrawable(RES_ID)
        assertEquals(colorDrawable, drawable)
        verify { view.styledDrawable(any()) }
    }

    @Test
    fun styledDrawable_shouldReturnDrawable_whenInGlobal() {
        mockkStatic("com.hhy.util.resource.DrawableResourcesKt")
        val context = mockk<Context> {
            every { styledDrawable(any()) } returns colorDrawable
        }
        context.injectAsAppCtx()
        val drawable = appStyledDrawable(RES_ID)
        assertEquals(colorDrawable, drawable)
        verify { context.styledDrawable(any()) }
    }
}
