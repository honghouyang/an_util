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
class PrimitiveResourcesTest {
    companion object {
        const val RES_ID = 1
        const val BOOL = true
        const val INT = 10
        val INT_ARRAY = intArrayOf(2)
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
    fun bool_shouldReturnBool_whenInContext() {
        val context = mockk<Context> {
            every { resources.getBoolean(any()) } returns BOOL
        }
        assertEquals(BOOL, context.bool(RES_ID))
        verify { context.resources.getBoolean(any()) }
    }

    @Test
    fun bool_shouldReturnBool_whenInFragment() {
        mockkStatic("com.hhy.util.resource.PrimitiveResourcesKt")
        val fragment = mockk<Fragment> {
            every { requireContext() } returns mockk {
                every { bool(any()) } returns BOOL
            }
        }
        val bool = fragment.bool(RES_ID)
        assertEquals(BOOL, bool)
        verify { fragment.bool(any()) }
    }

    @Test
    fun bool_shouldReturnBool_whenInView() {
        mockkStatic("com.hhy.util.resource.PrimitiveResourcesKt")
        val view = mockk<View> {
            every { context } returns mockk {
                every { bool(any()) } returns BOOL
            }
        }
        val bool = view.bool(RES_ID)
        assertEquals(BOOL, bool)
        verify { view.bool(any()) }
    }

    @Test
    fun bool_shouldReturnBool_whenInGlobal() {
        mockkStatic("com.hhy.util.resource.PrimitiveResourcesKt")
        val context = mockk<Context> {
            every { bool(any()) } returns BOOL
        }
        context.injectAsAppCtx()
        val bool = appBool(RES_ID)
        assertEquals(BOOL, bool)
        verify { context.bool(any()) }
    }

    @Test
    fun int_shouldReturnInt_whenInContext() {
        val context = mockk<Context> {
            every { resources.getInteger(any()) } returns INT
        }
        assertEquals(INT, context.int(RES_ID))
        verify { context.resources.getInteger(any()) }
    }

    @Test
    fun int_shouldReturnInt_whenInFragment() {
        mockkStatic("com.hhy.util.resource.PrimitiveResourcesKt")
        val fragment = mockk<Fragment> {
            every { requireContext() } returns mockk {
                every { int(any()) } returns INT
            }
        }
        val int = fragment.int(RES_ID)
        assertEquals(INT, int)
        verify { fragment.int(any()) }
    }

    @Test
    fun int_shouldReturnInt_whenInView() {
        mockkStatic("com.hhy.util.resource.PrimitiveResourcesKt")
        val view = mockk<View> {
            every { context } returns mockk {
                every { int(any()) } returns INT
            }
        }
        val int = view.int(RES_ID)
        assertEquals(INT, int)
        verify { view.int(any()) }
    }

    @Test
    fun int_shouldReturnInt_whenInGlobal() {
        mockkStatic("com.hhy.util.resource.PrimitiveResourcesKt")
        val context = mockk<Context> {
            every { int(any()) } returns INT
        }
        context.injectAsAppCtx()
        val int = appInt(RES_ID)
        assertEquals(INT, int)
        verify { context.int(any()) }
    }

    @Test
    fun intArray_shouldReturnIntArray_whenInContext() {
        val context = mockk<Context> {
            every { resources.getIntArray(any()) } returns INT_ARRAY
        }
        assertEquals(INT_ARRAY, context.intArray(RES_ID))
        verify { context.resources.getIntArray(any()) }
    }

    @Test
    fun intArray_shouldReturnIntArray_whenInFragment() {
        mockkStatic("com.hhy.util.resource.PrimitiveResourcesKt")
        val fragment = mockk<Fragment> {
            every { requireContext() } returns mockk {
                every { intArray(any()) } returns INT_ARRAY
            }
        }
        val array = fragment.intArray(RES_ID)
        assertEquals(INT_ARRAY, array)
        verify { fragment.intArray(any()) }
    }

    @Test
    fun intArray_shouldReturnIntArray_whenInView() {
        mockkStatic("com.hhy.util.resource.PrimitiveResourcesKt")
        val view = mockk<View> {
            every { context } returns mockk {
                every { intArray(any()) } returns INT_ARRAY
            }
        }
        val array = view.intArray(RES_ID)
        assertEquals(INT_ARRAY, array)
        verify { view.intArray(any()) }
    }

    @Test
    fun intArray_shouldReturnIntArray_whenInGlobal() {
        mockkStatic("com.hhy.util.resource.PrimitiveResourcesKt")
        val context = mockk<Context> {
            every { intArray(any()) } returns INT_ARRAY
        }
        context.injectAsAppCtx()
        val array = appIntArray(RES_ID)
        assertEquals(INT_ARRAY, array)
        verify { context.intArray(any()) }
    }

    @Test
    fun styledBool_shouldReturnBool_whenInContext() {
        val context = spyk<Context>()
        mockkStatic("com.hhy.util.resource.StyledAttributesKt")
        every { context.resolveThemeAttribute(any()) } returns RES_ID

        mockkStatic("com.hhy.util.resource.PrimitiveResourcesKt")
        every { context.bool(any()) } returns BOOL

        assertEquals(BOOL, context.styledBool(RES_ID))
        verify { context.bool(any()) }
    }

    @Test
    fun styledBool_shouldReturnBool_whenInFragment() {
        mockkStatic("com.hhy.util.resource.PrimitiveResourcesKt")
        val fragment = mockk<Fragment> {
            every { requireContext() } returns mockk {
                every { styledBool(any()) } returns BOOL
            }
        }
        val bool = fragment.styledBool(RES_ID)
        assertEquals(BOOL, bool)
        verify { fragment.styledBool(any()) }
    }

    @Test
    fun styledBool_shouldReturnBool_whenInView() {
        mockkStatic("com.hhy.util.resource.PrimitiveResourcesKt")
        val view = mockk<View> {
            every { context } returns mockk {
                every { styledBool(any()) } returns BOOL
            }
        }
        val bool = view.styledBool(RES_ID)
        assertEquals(BOOL, bool)
        verify { view.styledBool(any()) }
    }

    @Test
    fun styledBool_shouldReturnBool_whenInGlobal() {
        mockkStatic("com.hhy.util.resource.PrimitiveResourcesKt")
        val context = mockk<Context> {
            every { styledBool(any()) } returns BOOL
        }
        context.injectAsAppCtx()
        val bool = appStyledBool(RES_ID)
        assertEquals(BOOL, bool)
        verify { context.styledBool(any()) }
    }

    @Test
    fun styledInt_shouldReturnInt_whenInContext() {
        val context = spyk<Context>()
        mockkStatic("com.hhy.util.resource.StyledAttributesKt")
        every { context.resolveThemeAttribute(any()) } returns RES_ID

        mockkStatic("com.hhy.util.resource.PrimitiveResourcesKt")
        every { context.int(any()) } returns INT

        assertEquals(INT, context.styledInt(RES_ID))
        verify { context.styledInt(any()) }
    }

    @Test
    fun styledInt_shouldReturnInt_whenInFragment() {
        mockkStatic("com.hhy.util.resource.PrimitiveResourcesKt")
        val fragment = mockk<Fragment> {
            every { requireContext() } returns mockk {
                every { styledInt(any()) } returns INT
            }
        }
        val int = fragment.styledInt(RES_ID)
        assertEquals(INT, int)
        verify { fragment.styledInt(any()) }
    }

    @Test
    fun styledInt_shouldReturnInt_whenInView() {
        mockkStatic("com.hhy.util.resource.PrimitiveResourcesKt")
        val fragment = mockk<View> {
            every { context } returns mockk {
                every { styledInt(any()) } returns INT
            }
        }
        val int = fragment.styledInt(RES_ID)
        assertEquals(INT, int)
        verify { fragment.styledInt(any()) }
    }

    @Test
    fun styledInt_shouldReturnInt_whenInGlobal() {
        mockkStatic("com.hhy.util.resource.PrimitiveResourcesKt")
        val context = mockk<Context> {
            every { styledInt(any()) } returns INT
        }
        context.injectAsAppCtx()
        val int = appStyledInt(RES_ID)
        assertEquals(INT, int)
        verify { context.styledInt(any()) }
    }
}
