package com.hhy.util.resource

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import androidx.core.content.ContextCompat
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
class ColorResourcesTest {
    companion object {
        const val RES_ID = 1
        const val COLOR_INT = 1314
    }

    private val states = Array(2) { IntArray(2) }
    private val colors = IntArray(2)
    private lateinit var colorStateList: ColorStateList

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        colorStateList = ColorStateList(states, colors)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun color_shouldReturnColorInInt_whenInContext() {
        val context = spyk<Context>()
        mockkStatic(ContextCompat::class)
        every { ContextCompat.getColor(any(), any()) } returns COLOR_INT
        assertEquals(COLOR_INT, context.color(RES_ID))
        verify { ContextCompat.getColor(any(), any()) }
    }

    @Test
    fun color_shouldReturnColorInInt_whenInFragment() {
        mockkStatic("com.hhy.util.resource.ColorResourcesKt")
        val fragment = mockk<Fragment> {
            every { requireContext() } returns mockk {
                every { color(any()) } returns COLOR_INT
            }
        }
        val color = fragment.color(RES_ID)
        assertEquals(COLOR_INT, color)
        verify { fragment.color(any()) }
    }

    @Test
    fun color_shouldReturnColorInInt_whenInView() {
        mockkStatic("com.hhy.util.resource.ColorResourcesKt")
        val view = mockk<View> {
            every { context } returns mockk {
                every { color(any()) } returns COLOR_INT
            }
        }
        val color = view.color(RES_ID)
        assertEquals(COLOR_INT, color)
        verify { view.color(any()) }
    }

    @Test
    fun color_shouldReturnColorInInt_whenInGlobal() {
        mockkStatic("com.hhy.util.resource.ColorResourcesKt")
        val context = mockk<Context> {
            every { color(any()) } returns COLOR_INT
        }
        context.injectAsAppCtx()
        val color = appColor(RES_ID)
        assertEquals(COLOR_INT, color)
        verify { context.color(any()) }
    }

    @Test
    fun colorSl_shouldReturnColorStateList_whenInContext() {
        val context = spyk<Context>()
        mockkStatic(ResourcesCompat::class)
        every { ResourcesCompat.getColorStateList(any(), any(), any()) } returns colorStateList
        assertEquals(colorStateList, context.colorSL(RES_ID))
        verify { ResourcesCompat.getColorStateList(any(), any(), any()) }
    }

    @Test
    fun colorSl_shouldReturnColorStateList_whenInFragment() {
        mockkStatic("com.hhy.util.resource.ColorResourcesKt")
        val fragment = mockk<Fragment> {
            every { requireContext() } returns mockk {
                every { colorSL(any()) } returns colorStateList
            }
        }
        val colors = fragment.colorSL(RES_ID)
        assertEquals(colorStateList, colors)
        verify { fragment.colorSL(any()) }
    }

    @Test
    fun colorSl_shouldReturnColorStateList_whenInView() {
        mockkStatic("com.hhy.util.resource.ColorResourcesKt")
        val view = mockk<View> {
            every { context } returns mockk {
                every { colorSL(any()) } returns colorStateList
            }
        }
        val colors = view.colorSL(RES_ID)
        assertEquals(colorStateList, colors)
        verify { view.colorSL(any()) }
    }

    @Test
    fun colorSl_shouldReturnColorStateList_whenInGlobal() {
        mockkStatic("com.hhy.util.resource.ColorResourcesKt")
        val context = mockk<Context> {
            every { colorSL(any()) } returns colorStateList
        }
        context.injectAsAppCtx()
        val colors = appColorSL(RES_ID)
        assertEquals(colorStateList, colors)
        verify { context.colorSL(any()) }
    }

    @Test
    fun styledColor_shouldReturnColorStateList_whenInContext() {
        val context = spyk<Context>()
        mockkStatic("com.hhy.util.resource.StyledAttributesKt")
        every { context.resolveThemeAttribute(any()) } returns RES_ID

        mockkStatic("com.hhy.util.resource.ColorResourcesKt")
        every { context.color(any()) } returns COLOR_INT

        assertEquals(COLOR_INT, context.styledColor(RES_ID))
        verify { context.color(any()) }
    }

    @Test
    fun styledColor_shouldReturnColorStateList_whenInFragment() {
        mockkStatic("com.hhy.util.resource.ColorResourcesKt")
        val fragment = mockk<Fragment> {
            every { requireContext() } returns mockk {
                every { styledColor(any()) } returns COLOR_INT
            }
        }
        val color = fragment.styledColor(RES_ID)
        assertEquals(COLOR_INT, color)
        verify { fragment.styledColor(any()) }
    }

    @Test
    fun styledColor_shouldReturnColorStateList_whenInView() {
        mockkStatic("com.hhy.util.resource.ColorResourcesKt")
        val view = mockk<View> {
            every { context } returns mockk {
                every { styledColor(any()) } returns COLOR_INT
            }
        }
        val color = view.styledColor(RES_ID)
        assertEquals(COLOR_INT, color)
        verify { view.styledColor(any()) }
    }

    @Test
    fun styledColor_shouldReturnColorStateList_whenInGlobal() {
        mockkStatic("com.hhy.util.resource.ColorResourcesKt")
        val context = mockk<Context> {
            every { styledColor(any()) } returns COLOR_INT
        }
        context.injectAsAppCtx()
        val color = appStyledColor(RES_ID)
        assertEquals(COLOR_INT, color)
        verify { context.styledColor(any()) }
    }

    @Test
    fun styledColorSL_shouldReturnColorStateList_whenInContext() {
        val context = spyk<Context>()
        mockkStatic("com.hhy.util.resource.StyledAttributesKt")
        every { context.resolveThemeAttribute(any()) } returns RES_ID

        mockkStatic("com.hhy.util.resource.ColorResourcesKt")
        every { context.colorSL(any()) } returns colorStateList

        assertEquals(colorStateList, context.styledColorSL(RES_ID))
        verify { context.colorSL(any()) }
    }

    @Test
    fun styledColorSL_shouldReturnColorStateList_whenInFragment() {
        mockkStatic("com.hhy.util.resource.ColorResourcesKt")
        val fragment = mockk<Fragment> {
            every { requireContext() } returns mockk {
                every { styledColorSL(any()) } returns colorStateList
            }
        }
        val colors = fragment.styledColorSL(RES_ID)
        assertEquals(colorStateList, colors)
        verify { fragment.styledColorSL(any()) }
    }

    @Test
    fun styledColorSL_shouldReturnColorStateList_whenInView() {
        mockkStatic("com.hhy.util.resource.ColorResourcesKt")
        val view = mockk<View> {
            every { context } returns mockk {
                every { styledColorSL(any()) } returns colorStateList
            }
        }
        val colors = view.styledColorSL(RES_ID)
        assertEquals(colorStateList, colors)
        verify { view.styledColorSL(any()) }
    }

    @Test
    fun styledColorSL_shouldReturnColorStateList_whenInGlobal() {
        mockkStatic("com.hhy.util.resource.ColorResourcesKt")
        val context = mockk<Context> {
            every { styledColorSL(any()) } returns colorStateList
        }
        context.injectAsAppCtx()
        val colors = appStyledColorSL(RES_ID)
        assertEquals(colorStateList, colors)
        verify { context.styledColorSL(any()) }
    }
}
