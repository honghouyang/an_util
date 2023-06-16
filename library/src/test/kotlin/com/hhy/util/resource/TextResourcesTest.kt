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
class TextResourcesTest {
    companion object {
        const val RES_ID = 1
        const val QUANTITY = 2
        val CHARS: CharSequence = "Hello, Java!"
        const val STRS = "Hello, Kotlin!"
        val CHARS_ARRAY = arrayOf<CharSequence>()
        val STRS_ARRAY = arrayOf<String>()
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
    fun txt_shouldReturnCharSequence_whenInContext() {
        val context = mockk<Context> {
            every { resources.getText(any()) } returns CHARS
        }
        assertEquals(CHARS, context.txt(RES_ID))
        verify { context.resources.getText(any()) }
    }

    @Test
    fun txt_shouldReturnCharSequence_whenInFragment() {
        mockkStatic("com.hhy.util.resource.TextResourcesKt")
        val fragment = mockk<Fragment> {
            every { requireContext() } returns mockk {
                every { txt(any()) } returns CHARS
            }
        }
        val chars = fragment.txt(RES_ID)
        assertEquals(CHARS, chars)
        verify { fragment.txt(any()) }
    }

    @Test
    fun txt_shouldReturnCharSequence_whenInView() {
        mockkStatic("com.hhy.util.resource.TextResourcesKt")
        val view = mockk<View> {
            every { context } returns mockk {
                every { txt(any()) } returns CHARS
            }
        }
        val chars = view.txt(RES_ID)
        assertEquals(CHARS, chars)
        verify { view.txt(any()) }
    }

    @Test
    fun txt_shouldReturnCharSequence_whenInGlobal() {
        mockkStatic("com.hhy.util.resource.TextResourcesKt")
        val context = mockk<Context> {
            every { txt(any()) } returns CHARS
        }
        context.injectAsAppCtx()
        val chars = appTxt(RES_ID)
        assertEquals(CHARS, chars)
        verify { context.txt(any()) }
    }

    @Test
    fun strArgs_shouldReturnString_whenInContext() {
        val context = mockk<Context> {
            every { resources.getString(any(), *anyVararg()) } returns STRS
        }
        assertEquals(STRS, context.str(RES_ID, ""))
        verify { context.resources.getString(any(), *anyVararg()) }
    }

    @Test
    fun strArgs_shouldReturnString_whenInFragment() {
        mockkStatic("com.hhy.util.resource.TextResourcesKt")
        val fragment = mockk<Fragment> {
            every { requireContext() } returns mockk {
                every { str(any(), *anyVararg()) } returns STRS
            }
        }
        val strs = fragment.str(RES_ID, "")
        assertEquals(STRS, strs)
        verify { fragment.str(any(), *anyVararg()) }
    }

    @Test
    fun strArgs_shouldReturnString_whenInView() {
        mockkStatic("com.hhy.util.resource.TextResourcesKt")
        val view = mockk<View> {
            every { context } returns mockk {
                every { str(any(), *anyVararg()) } returns STRS
            }
        }
        val strs = view.str(RES_ID, "")
        assertEquals(STRS, strs)
        verify { view.str(any(), *anyVararg()) }
    }

    @Test
    fun strArgs_shouldReturnString_whenInGlobal() {
        mockkStatic("com.hhy.util.resource.TextResourcesKt")
        val context = mockk<Context> {
            every { str(any(), *anyVararg()) } returns STRS
        }
        context.injectAsAppCtx()
        val strs = appStr(RES_ID, "")
        assertEquals(STRS, strs)
        verify { context.str(any(), *anyVararg()) }
    }

    @Test
    fun str_shouldReturnString_whenInContext() {
        val context = mockk<Context> {
            every { resources.getString(any()) } returns STRS
        }
        assertEquals(STRS, context.str(RES_ID))
        verify { context.resources.getString(any()) }
    }

    @Test
    fun str_shouldReturnString_whenInFragment() {
        mockkStatic("com.hhy.util.resource.TextResourcesKt")
        val fragment = mockk<Fragment> {
            every { requireContext() } returns mockk {
                every { str(any()) } returns STRS
            }
        }
        val strs = fragment.str(RES_ID)
        assertEquals(STRS, strs)
        verify { fragment.str(any()) }
    }

    @Test
    fun str_shouldReturnString_whenInView() {
        mockkStatic("com.hhy.util.resource.TextResourcesKt")
        val view = mockk<View> {
            every { context } returns mockk {
                every { str(any()) } returns STRS
            }
        }
        val strs = view.str(RES_ID)
        assertEquals(STRS, strs)
        verify { view.str(any()) }
    }

    @Test
    fun str_shouldReturnString_whenInGlobal() {
        mockkStatic("com.hhy.util.resource.TextResourcesKt")
        val context = mockk<Context> {
            every { str(any()) } returns STRS
        }
        context.injectAsAppCtx()
        val strs = appStr(RES_ID)
        assertEquals(STRS, strs)
        verify { context.str(any()) }
    }

    @Test
    fun qtyTxt_shouldReturnCharSequence_whenInContext() {
        val context = mockk<Context> {
            every { resources.getQuantityText(any(), any()) } returns CHARS
        }
        assertEquals(CHARS, context.qtyTxt(RES_ID, QUANTITY))
        verify { context.resources.getQuantityText(any(), any()) }
    }

    @Test
    fun qtyTxt_shouldReturnCharSequence_whenInFragment() {
        mockkStatic("com.hhy.util.resource.TextResourcesKt")
        val fragment = mockk<Fragment> {
            every { requireContext() } returns mockk {
                every { qtyTxt(any(), any()) } returns CHARS
            }
        }
        val chars = fragment.qtyTxt(RES_ID, QUANTITY)
        assertEquals(CHARS, chars)
        verify { fragment.qtyTxt(any(), any()) }
    }

    @Test
    fun qtyTxt_shouldReturnCharSequence_whenInView() {
        mockkStatic("com.hhy.util.resource.TextResourcesKt")
        val view = mockk<View> {
            every { context } returns mockk {
                every { qtyTxt(any(), any()) } returns CHARS
            }
        }
        val chars = view.qtyTxt(RES_ID, QUANTITY)
        assertEquals(CHARS, chars)
        verify { view.qtyTxt(any(), any()) }
    }

    @Test
    fun qtyTxt_shouldReturnCharSequence_whenInGlobal() {
        mockkStatic("com.hhy.util.resource.TextResourcesKt")
        val context = mockk<Context> {
            every { qtyTxt(any(), any()) } returns CHARS
        }
        context.injectAsAppCtx()
        val chars = appQtyTxt(RES_ID, QUANTITY)
        assertEquals(CHARS, chars)
        verify { context.qtyTxt(any(), any()) }
    }

    @Test
    fun qtyStr_shouldReturnString_whenInContext() {
        val context = mockk<Context> {
            every { resources.getQuantityString(any(), any()) } returns STRS
        }
        assertEquals(STRS, context.qtyStr(RES_ID, QUANTITY))
        verify { context.resources.getQuantityString(any(), any()) }
    }

    @Test
    fun qtyStr_shouldReturnString_whenInFragment() {
        mockkStatic("com.hhy.util.resource.TextResourcesKt")
        val fragment = mockk<Fragment> {
            every { requireContext() } returns mockk {
                every { qtyStr(any(), any()) } returns STRS
            }
        }
        val strs = fragment.qtyStr(RES_ID, QUANTITY)
        assertEquals(STRS, strs)
        verify { fragment.qtyStr(any(), any()) }
    }

    @Test
    fun qtyStr_shouldReturnString_whenInView() {
        mockkStatic("com.hhy.util.resource.TextResourcesKt")
        val view = mockk<View> {
            every { context } returns mockk {
                every { qtyStr(any(), any()) } returns STRS
            }
        }
        val strs = view.qtyStr(RES_ID, QUANTITY)
        assertEquals(STRS, strs)
        verify { view.qtyStr(any(), any()) }
    }

    @Test
    fun qtyStr_shouldReturnString_whenInGlobal() {
        mockkStatic("com.hhy.util.resource.TextResourcesKt")
        val context = mockk<Context> {
            every { qtyStr(any(), any()) } returns STRS
        }
        context.injectAsAppCtx()
        val strs = appQtyStr(RES_ID, QUANTITY)
        assertEquals(STRS, strs)
        verify { context.qtyStr(any(), any()) }
    }

    @Test
    fun qtyStrArgs_shouldReturnString_whenInContext() {
        val context = mockk<Context> {
            every { resources.getQuantityString(any(), any(), *anyVararg()) } returns STRS
        }
        assertEquals(STRS, context.qtyStr(RES_ID, QUANTITY, ""))
        verify { context.resources.getQuantityString(any(), any(), *anyVararg()) }
    }

    @Test
    fun qtyStrArgs_shouldReturnString_whenInFragment() {
        mockkStatic("com.hhy.util.resource.TextResourcesKt")
        val fragment = mockk<Fragment> {
            every { requireContext() } returns mockk {
                every { qtyStr(any(), any(), *anyVararg()) } returns STRS
            }
        }
        val strs = fragment.qtyStr(RES_ID, QUANTITY, "")
        assertEquals(STRS, strs)
        verify { fragment.qtyStr(any(), any(), *anyVararg()) }
    }

    @Test
    fun qtyStrArgs_shouldReturnString_whenInView() {
        mockkStatic("com.hhy.util.resource.TextResourcesKt")
        val view = mockk<View> {
            every { context } returns mockk {
                every { qtyStr(any(), any(), *anyVararg()) } returns STRS
            }
        }
        val strs = view.qtyStr(RES_ID, QUANTITY, "")
        assertEquals(STRS, strs)
        verify { view.qtyStr(any(), any(), *anyVararg()) }
    }

    @Test
    fun qtyStrArgs_shouldReturnString_whenInGlobal() {
        mockkStatic("com.hhy.util.resource.TextResourcesKt")
        val context = mockk<Context> {
            every { qtyStr(any(), any(), any()) } returns STRS
        }
        context.injectAsAppCtx()
        val strs = appQtyStr(RES_ID, QUANTITY, "")
        assertEquals(STRS, strs)
        verify { context.qtyStr(any(), any(), *anyVararg()) }
    }

    @Test
    fun txtArray_shouldReturnCharSequenceArray_whenInContext() {
        val context = mockk<Context> {
            every { resources.getTextArray(any()) } returns CHARS_ARRAY
        }
        assertEquals(CHARS_ARRAY, context.txtArray(RES_ID))
        verify { context.resources.getTextArray(any()) }
    }

    @Test
    fun txtArray_shouldReturnCharSequenceArray_whenInFragment() {
        mockkStatic("com.hhy.util.resource.TextResourcesKt")
        val fragment = mockk<Fragment> {
            every { requireContext() } returns mockk {
                every { txtArray(any()) } returns CHARS_ARRAY
            }
        }
        val charsArray = fragment.txtArray(RES_ID)
        assertEquals(CHARS_ARRAY, charsArray)
        verify { fragment.txtArray(any()) }
    }

    @Test
    fun txtArray_shouldReturnCharSequenceArray_whenInView() {
        mockkStatic("com.hhy.util.resource.TextResourcesKt")
        val view = mockk<View> {
            every { context } returns mockk {
                every { txtArray(any()) } returns CHARS_ARRAY
            }
        }
        val charsArray = view.txtArray(RES_ID)
        assertEquals(CHARS_ARRAY, charsArray)
        verify { view.txtArray(any()) }
    }

    @Test
    fun txtArray_shouldReturnCharSequenceArray_whenInGlobal() {
        mockkStatic("com.hhy.util.resource.TextResourcesKt")
        val context = mockk<Context> {
            every { txtArray(any()) } returns CHARS_ARRAY
        }
        context.injectAsAppCtx()
        val charsArray = appTxtArray(RES_ID)
        assertEquals(CHARS_ARRAY, charsArray)
        verify { context.txtArray(any()) }
    }

    @Test
    fun strArray_shouldReturnStringArray_whenInContext() {
        val context = mockk<Context> {
            every { resources.getStringArray(any()) } returns STRS_ARRAY
        }
        assertEquals(STRS_ARRAY, context.strArray(RES_ID))
        verify { context.resources.getStringArray(any()) }
    }

    @Test
    fun strArray_shouldReturnStringArray_whenInFragment() {
        mockkStatic("com.hhy.util.resource.TextResourcesKt")
        val fragment = mockk<Fragment> {
            every { requireContext() } returns mockk {
                every { strArray(any()) } returns STRS_ARRAY
            }
        }
        val strsArray = fragment.strArray(RES_ID)
        assertEquals(STRS_ARRAY, strsArray)
        verify { fragment.strArray(any()) }
    }

    @Test
    fun strArray_shouldReturnStringArray_whenInView() {
        mockkStatic("com.hhy.util.resource.TextResourcesKt")
        val view = mockk<View> {
            every { context } returns mockk {
                every { strArray(any()) } returns STRS_ARRAY
            }
        }
        val strsArray = view.strArray(RES_ID)
        assertEquals(STRS_ARRAY, strsArray)
        verify { view.strArray(any()) }
    }

    @Test
    fun strArray_shouldReturnStringArray_whenInGlobal() {
        mockkStatic("com.hhy.util.resource.TextResourcesKt")
        val context = mockk<Context> {
            every { strArray(any()) } returns STRS_ARRAY
        }
        context.injectAsAppCtx()
        val strsArray = appStrArray(RES_ID)
        assertEquals(STRS_ARRAY, strsArray)
        verify { context.strArray(any()) }
    }

    @Test
    fun styledTxt_shouldReturnCharSequence_whenInContext() {
        val context = spyk<Context>()
        mockkStatic("com.hhy.util.resource.StyledAttributesKt")
        every { context.resolveThemeAttribute(any()) } returns RES_ID

        mockkStatic("com.hhy.util.resource.TextResourcesKt")
        every { context.txt(any()) } returns CHARS

        assertEquals(CHARS, context.styledTxt(RES_ID))
        verify { context.txt(any()) }
    }

    @Test
    fun styledTxt_shouldReturnCharSequence_whenInFragment() {
        mockkStatic("com.hhy.util.resource.TextResourcesKt")
        val fragment = mockk<Fragment> {
            every { requireContext() } returns mockk {
                every { styledTxt(any()) } returns CHARS
            }
        }
        val chars = fragment.styledTxt(RES_ID)
        assertEquals(CHARS, chars)
        verify { fragment.styledTxt(any()) }
    }

    @Test
    fun styledTxt_shouldReturnCharSequence_whenInView() {
        mockkStatic("com.hhy.util.resource.TextResourcesKt")
        val view = mockk<View> {
            every { context } returns mockk {
                every { styledTxt(any()) } returns CHARS
            }
        }
        val chars = view.styledTxt(RES_ID)
        assertEquals(CHARS, chars)
        verify { view.styledTxt(any()) }
    }

    @Test
    fun styledTxt_shouldReturnCharSequence_whenInGlobal() {
        mockkStatic("com.hhy.util.resource.TextResourcesKt")
        val context = mockk<Context> {
            every { styledTxt(any()) } returns CHARS
        }
        context.injectAsAppCtx()
        val chars = appStyledTxt(RES_ID)
        assertEquals(CHARS, chars)
        verify { context.styledTxt(any()) }
    }

    @Test
    fun styledStr_shouldReturnString_whenInContext() {
        val context = spyk<Context>()
        mockkStatic("com.hhy.util.resource.StyledAttributesKt")
        every { context.resolveThemeAttribute(any()) } returns RES_ID

        mockkStatic("com.hhy.util.resource.TextResourcesKt")
        every { context.str(any()) } returns STRS

        assertEquals(STRS, context.styledStr(RES_ID))
        verify { context.str(any()) }
    }

    @Test
    fun styledStr_shouldReturnString_whenInFragment() {
        mockkStatic("com.hhy.util.resource.TextResourcesKt")
        val fragment = mockk<Fragment> {
            every { requireContext() } returns mockk {
                every { styledStr(any()) } returns STRS
            }
        }
        val strs = fragment.styledStr(RES_ID)
        assertEquals(STRS, strs)
        verify { fragment.styledStr(any()) }
    }

    @Test
    fun styledStr_shouldReturnString_whenInView() {
        mockkStatic("com.hhy.util.resource.TextResourcesKt")
        val view = mockk<View> {
            every { context } returns mockk {
                every { styledStr(any()) } returns STRS
            }
        }
        val strs = view.styledStr(RES_ID)
        assertEquals(STRS, strs)
        verify { view.styledStr(any()) }
    }

    @Test
    fun styledStr_shouldReturnString_whenInGlobal() {
        mockkStatic("com.hhy.util.resource.TextResourcesKt")
        val context = mockk<Context> {
            every { styledStr(any()) } returns STRS
        }
        context.injectAsAppCtx()
        val strs = appStyledStr(RES_ID)
        assertEquals(STRS, strs)
        verify { context.styledStr(any()) }
    }

    @Test
    fun styledStrArgs_shouldReturnString_whenInContext() {
        val context = spyk<Context>()
        mockkStatic("com.hhy.util.resource.StyledAttributesKt")
        every { context.resolveThemeAttribute(any()) } returns RES_ID

        mockkStatic("com.hhy.util.resource.TextResourcesKt")
        every { context.str(any(), *anyVararg()) } returns STRS

        assertEquals(STRS, context.styledStr(RES_ID, ""))
        verify { context.str(any(), *anyVararg()) }
    }

    @Test
    fun styledStrArgs_shouldReturnString_whenInFragment() {
        mockkStatic("com.hhy.util.resource.TextResourcesKt")
        val fragment = mockk<Fragment> {
            every { requireContext() } returns mockk {
                every { styledStr(any(), *anyVararg()) } returns STRS
            }
        }
        val strs = fragment.styledStr(RES_ID, "")
        assertEquals(STRS, strs)
        verify { fragment.styledStr(any(), *anyVararg()) }
    }

    @Test
    fun styledStrArgs_shouldReturnString_whenInView() {
        mockkStatic("com.hhy.util.resource.TextResourcesKt")
        val view = mockk<View> {
            every { context } returns mockk {
                every { styledStr(any(), *anyVararg()) } returns STRS
            }
        }
        val strs = view.styledStr(RES_ID, "")
        assertEquals(STRS, strs)
        verify { view.styledStr(any(), *anyVararg()) }
    }

    @Test
    fun styledStrArgs_shouldReturnString_whenInGlobal() {
        mockkStatic("com.hhy.util.resource.TextResourcesKt")
        val context = mockk<Context> {
            every { styledStr(any(), *anyVararg()) } returns STRS
        }
        context.injectAsAppCtx()
        val strs = appStyledStr(RES_ID, "")
        assertEquals(STRS, strs)
        verify { context.styledStr(any(), *anyVararg()) }
    }

    @Test
    fun styledTxtArray_shouldReturnCharSequenceArray_whenInContext() {
        val context = spyk<Context>()
        mockkStatic("com.hhy.util.resource.StyledAttributesKt")
        every { context.resolveThemeAttribute(any()) } returns RES_ID

        mockkStatic("com.hhy.util.resource.TextResourcesKt")
        every { context.txtArray(any()) } returns CHARS_ARRAY

        assertEquals(CHARS_ARRAY, context.styledTxtArray(RES_ID))
        verify { context.txtArray(any()) }
    }

    @Test
    fun styledTxtArray_shouldReturnCharSequenceArray_whenInFragment() {
        mockkStatic("com.hhy.util.resource.TextResourcesKt")
        val fragment = mockk<Fragment> {
            every { requireContext() } returns mockk {
                every { styledTxtArray(any()) } returns CHARS_ARRAY
            }
        }
        val charsArray = fragment.styledTxtArray(RES_ID)
        assertEquals(CHARS_ARRAY, charsArray)
        verify { fragment.styledTxtArray(any()) }
    }

    @Test
    fun styledTxtArray_shouldReturnCharSequenceArray_whenInView() {
        mockkStatic("com.hhy.util.resource.TextResourcesKt")
        val view = mockk<View> {
            every { context } returns mockk {
                every { styledTxtArray(any()) } returns CHARS_ARRAY
            }
        }
        val charsArray = view.styledTxtArray(RES_ID)
        assertEquals(CHARS_ARRAY, charsArray)
        verify { view.styledTxtArray(any()) }
    }

    @Test
    fun styledTxtArray_shouldReturnCharSequenceArray_whenInGlobal() {
        mockkStatic("com.hhy.util.resource.TextResourcesKt")
        val context = mockk<Context> {
            every { styledTxtArray(any()) } returns CHARS_ARRAY
        }
        context.injectAsAppCtx()
        val charsArray = appStyledTxtArray(RES_ID)
        assertEquals(CHARS_ARRAY, charsArray)
        verify { context.styledTxtArray(any()) }
    }
}
