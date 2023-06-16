package com.hhy.util.resource

import android.content.Context
import android.view.View
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
class DimenResourcesTest {
    companion object {
        const val RES_ID = 1
        const val DIMEN_FLOAT = 10f
        const val DIMEN_INT = 10
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
    fun dimen_shouldReturnDimenInFloat_whenInContext() {
        val context = mockk<Context> {
            every { resources.getDimension(any()) } returns DIMEN_FLOAT
        }
        assertEquals(DIMEN_FLOAT, context.dimen(RES_ID))
        verify { context.resources.getDimension(any()) }
    }

    @Test
    fun dimen_shouldReturnDimenInFloat_whenInFragment() {
        mockkStatic("com.hhy.util.resource.DimenResourcesKt")
        val context = mockk<Context> {
            every { dimen(any()) } returns DIMEN_FLOAT
        }
        val fragment = mockk<Fragment> {
            every { requireContext() } returns context
        }
        val dimen = fragment.dimen(RES_ID)
        assertEquals(DIMEN_FLOAT, dimen)
        verify {
            fragment.requireContext()
            context.dimen(any())
        }
    }

    @Test
    fun dimen_shouldReturnDimenInFloat_whenInView() {
        mockkStatic("com.hhy.util.resource.DimenResourcesKt")
        val view = mockk<View> {
            every { context } returns mockk {
                every { dimen(any()) } returns DIMEN_FLOAT
            }
        }
        val dimen = view.dimen(RES_ID)
        assertEquals(DIMEN_FLOAT, dimen)
        verify { view.dimen(any()) }
    }

    @Test
    fun dimen_shouldReturnDimenInFloat_whenInGlobal() {
        mockkStatic("com.hhy.util.resource.DimenResourcesKt")
        val context = mockk<Context> {
            every { dimen(any()) } returns DIMEN_FLOAT
        }
        context.injectAsAppCtx()
        val dimen = appDimen(RES_ID)
        assertEquals(DIMEN_FLOAT, dimen)
        verify { context.dimen(any()) }
    }

    @Test
    fun dimenPxSize_shouldReturnDimenInInt_whenInContext() {
        val context = mockk<Context> {
            every { resources.getDimensionPixelSize(any()) } returns DIMEN_INT
        }
        assertEquals(DIMEN_INT, context.dimenPxSize(RES_ID))
        verify { context.resources.getDimensionPixelSize(any()) }
    }

    @Test
    fun dimenPxSize_shouldReturnDimenInInt_whenInFragment() {
        mockkStatic("com.hhy.util.resource.DimenResourcesKt")
        val fragment = mockk<Fragment> {
            every { requireContext() } returns mockk {
                every { dimenPxSize(any()) } returns DIMEN_INT
            }
        }
        assertEquals(DIMEN_INT, fragment.dimenPxSize(RES_ID))
        verify { fragment.dimenPxSize(any()) }
    }

    @Test
    fun dimenPxSize_shouldReturnDimenInInt_whenInView() {
        mockkStatic("com.hhy.util.resource.DimenResourcesKt")
        val view = mockk<View> {
            every { context } returns mockk {
                every { dimenPxSize(any()) } returns DIMEN_INT
            }
        }
        assertEquals(DIMEN_INT, view.dimenPxSize(RES_ID))
        verify { view.dimenPxSize(any()) }
    }

    @Test
    fun dimenPxSize_shouldReturnDimenInInt_whenInGlobal() {
        mockkStatic("com.hhy.util.resource.DimenResourcesKt")
        val context = mockk<Context> {
            every { dimenPxSize(any()) } returns DIMEN_INT
        }
        context.injectAsAppCtx()
        assertEquals(DIMEN_INT, appDimenPxSize(RES_ID))
        verify { context.dimenPxSize(any()) }
    }

    @Test
    fun dimenPxOffset_shouldReturnDimenInInt_whenInContext() {
        val context = mockk<Context> {
            every { resources.getDimensionPixelOffset(any()) } returns DIMEN_INT
        }
        assertEquals(DIMEN_INT, context.dimenPxOffset(RES_ID))
        verify { context.resources.getDimensionPixelOffset(any()) }
    }

    @Test
    fun dimenPxOffset_shouldReturnDimenInInt_whenInFragment() {
        mockkStatic("com.hhy.util.resource.DimenResourcesKt")
        val fragment = mockk<Fragment> {
            every { requireContext() } returns mockk {
                every { dimenPxOffset(any()) } returns DIMEN_INT
            }
        }
        assertEquals(DIMEN_INT, fragment.dimenPxOffset(RES_ID))
        verify { fragment.dimenPxOffset(any()) }
    }

    @Test
    fun dimenPxOffset_shouldReturnDimenInInt_whenInView() {
        mockkStatic("com.hhy.util.resource.DimenResourcesKt")
        val view = mockk<View> {
            every { context } returns mockk {
                every { dimenPxOffset(any()) } returns DIMEN_INT
            }
        }
        assertEquals(DIMEN_INT, view.dimenPxOffset(RES_ID))
        verify { view.dimenPxOffset(any()) }
    }

    @Test
    fun dimenPxOffset_shouldReturnDimenInInt_whenInGlobal() {
        mockkStatic("com.hhy.util.resource.DimenResourcesKt")
        val context = mockk<Context> {
            every { dimenPxOffset(any()) } returns DIMEN_INT
        }
        context.injectAsAppCtx()
        assertEquals(DIMEN_INT, appDimenPxOffset(RES_ID))
        verify { context.dimenPxOffset(any()) }
    }

    @Test
    fun styledDimen_shouldReturnDimenInFloat_whenInContext() {
        val context = spyk<Context>()
        mockkStatic("com.hhy.util.resource.StyledAttributesKt")
        every { context.resolveThemeAttribute(any()) } returns RES_ID

        mockkStatic("com.hhy.util.resource.DimenResourcesKt")
        every { context.dimen(any()) } returns DIMEN_FLOAT

        assertEquals(DIMEN_FLOAT, context.styledDimen(RES_ID))
        verify { context.dimen(any()) }
    }

    @Test
    fun styledDimen_shouldReturnDimenInFloat_whenInFragment() {
        mockkStatic("com.hhy.util.resource.DimenResourcesKt")
        val fragment = mockk<Fragment> {
            every { requireContext() } returns mockk {
                every { styledDimen(any()) } returns DIMEN_FLOAT
            }
        }
        val dimen = fragment.styledDimen(RES_ID)
        assertEquals(DIMEN_FLOAT, dimen)
        verify { fragment.styledDimen(any()) }
    }

    @Test
    fun styledDimen_shouldReturnDimenInFloat_whenInView() {
        mockkStatic("com.hhy.util.resource.DimenResourcesKt")
        val view = mockk<View> {
            every { context } returns mockk {
                every { styledDimen(any()) } returns DIMEN_FLOAT
            }
        }
        val dimen = view.styledDimen(RES_ID)
        assertEquals(DIMEN_FLOAT, dimen)
        verify { view.styledDimen(any()) }
    }

    @Test
    fun styledDimen_shouldReturnDimenInFloat_whenInGlobal() {
        mockkStatic("com.hhy.util.resource.DimenResourcesKt")
        val context = mockk<Context> {
            every { styledDimen(any()) } returns DIMEN_FLOAT
        }
        context.injectAsAppCtx()
        val dimen = appStyledDimen(RES_ID)
        assertEquals(DIMEN_FLOAT, dimen)
        verify { context.styledDimen(any()) }
    }

    @Test
    fun styledDimenPxSize_shouldReturnDimenInInt_whenInContext() {
        val context = spyk<Context>()
        mockkStatic("com.hhy.util.resource.StyledAttributesKt")
        every { context.resolveThemeAttribute(any()) } returns RES_ID

        mockkStatic("com.hhy.util.resource.DimenResourcesKt")
        every { context.dimenPxSize(any()) } returns DIMEN_INT

        assertEquals(DIMEN_INT, context.styledDimenPxSize(RES_ID))
        verify { context.dimenPxSize(any()) }
    }

    @Test
    fun styledDimenPxSize_shouldReturnDimenInInt_whenInFragment() {
        mockkStatic("com.hhy.util.resource.DimenResourcesKt")
        val fragment = mockk<Fragment> {
            every { requireContext() } returns mockk {
                every { styledDimenPxSize(any()) } returns DIMEN_INT
            }
        }
        val dimen = fragment.styledDimenPxSize(RES_ID)
        assertEquals(DIMEN_INT, dimen)
        verify { fragment.styledDimenPxSize(any()) }
    }

    @Test
    fun styledDimenPxSize_shouldReturnDimenInInt_whenInView() {
        mockkStatic("com.hhy.util.resource.DimenResourcesKt")
        val view = mockk<View> {
            every { context } returns mockk {
                every { styledDimenPxSize(any()) } returns DIMEN_INT
            }
        }
        val dimen = view.styledDimenPxSize(RES_ID)
        assertEquals(DIMEN_INT, dimen)
        verify { view.styledDimenPxSize(any()) }
    }

    @Test
    fun styledDimenPxSize_shouldReturnDimenInInt_whenInGlobal() {
        mockkStatic("com.hhy.util.resource.DimenResourcesKt")
        val context = mockk<Context> {
            every { styledDimenPxSize(any()) } returns DIMEN_INT
        }
        context.injectAsAppCtx()
        val dimen = appStyledDimenPxSize(RES_ID)
        assertEquals(DIMEN_INT, dimen)
        verify { context.styledDimenPxSize(any()) }
    }

    @Test
    fun styledDimenPxOffset_shouldReturnDimenInInt_whenInContext() {
        val context = spyk<Context>()
        mockkStatic("com.hhy.util.resource.StyledAttributesKt")
        every { context.resolveThemeAttribute(any()) } returns RES_ID

        mockkStatic("com.hhy.util.resource.DimenResourcesKt")
        every { context.dimenPxOffset(any()) } returns DIMEN_INT

        assertEquals(DIMEN_INT, context.styledDimenPxOffset(RES_ID))
        verify { context.dimenPxOffset(any()) }
    }

    @Test
    fun styledDimenPxOffset_shouldReturnDimenInInt_whenInFragment() {
        mockkStatic("com.hhy.util.resource.DimenResourcesKt")
        val fragment = mockk<Fragment> {
            every { requireContext() } returns mockk {
                every { styledDimenPxOffset(any()) } returns DIMEN_INT
            }
        }
        val dimen = fragment.styledDimenPxOffset(RES_ID)
        assertEquals(DIMEN_INT, dimen)
        verify { fragment.styledDimenPxOffset(any()) }
    }

    @Test
    fun styledDimenPxOffset_shouldReturnDimenInInt_whenInView() {
        mockkStatic("com.hhy.util.resource.DimenResourcesKt")
        val view = mockk<View> {
            every { context } returns mockk {
                every { styledDimenPxOffset(any()) } returns DIMEN_INT
            }
        }
        val dimen = view.styledDimenPxOffset(RES_ID)
        assertEquals(DIMEN_INT, dimen)
        verify { view.styledDimenPxOffset(any()) }
    }

    @Test
    fun styledDimenPxOffset_shouldReturnDimenInInt_whenInGlobal() {
        mockkStatic("com.hhy.util.resource.DimenResourcesKt")
        val context = mockk<Context> {
            every { styledDimenPxOffset(any()) } returns DIMEN_INT
        }
        context.injectAsAppCtx()
        val dimen = appStyledDimenPxOffset(RES_ID)
        assertEquals(DIMEN_INT, dimen)
        verify { context.styledDimenPxOffset(any()) }
    }
}
