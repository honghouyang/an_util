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
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@SmallTest
class RawResourcesTest {
    companion object {
        const val RES_ID = 1
        const val CONTENT = "Hello, Kotlin!"
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
    fun rawStr_shouldReadContent_whenInFragment() {
        mockkStatic("com.hhy.util.resource.RawResourcesKt")
        val fragment = mockk<Fragment> {
            every { requireContext() } returns mockk {
                every { rawStr(any()) } returns CONTENT
            }
        }
        val content = fragment.rawStr(RES_ID)
        assertEquals(CONTENT, content)
        verify { fragment.rawStr(any()) }
    }

    @Test
    fun rawStr_shouldReadContent_whenInView() {
        mockkStatic("com.hhy.util.resource.RawResourcesKt")
        val view = mockk<View> {
            every { context } returns mockk {
                every { rawStr(any()) } returns CONTENT
            }
        }
        val content = view.rawStr(RES_ID)
        assertEquals(CONTENT, content)
        verify { view.rawStr(any()) }
    }

    @Test
    fun rawStr_shouldReadContent_whenInGlobal() {
        mockkStatic("com.hhy.util.resource.RawResourcesKt")
        val context = mockk<Context> {
            every { rawStr(any()) } returns CONTENT
        }
        context.injectAsAppCtx()
        val content = appRawStr(RES_ID)
        assertEquals(CONTENT, content)
        verify { context.rawStr(any()) }
    }
}
