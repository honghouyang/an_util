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
class AssetResourcesTest {
    companion object {
        const val CONTENT = "Hello, Kotlin!"
        const val ASSET_UTIL = "util"
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
    fun assetStr_shouldReadContent_whenInFragment() {
        mockkStatic("com.hhy.util.resource.AssetResourcesKt")
        val fragment = mockk<Fragment> {
            every { requireContext() } returns mockk {
                every { assetStr(any()) } returns CONTENT
            }
        }
        val content = fragment.assetStr(ASSET_UTIL)
        assertEquals(CONTENT, content)
        verify { fragment.assetStr(any()) }
    }

    @Test
    fun assetStr_shouldReadContent_whenInView() {
        mockkStatic("com.hhy.util.resource.AssetResourcesKt")
        val view = mockk<View> {
            every { context } returns mockk {
                every { assetStr(any()) } returns CONTENT
            }
        }
        val content = view.assetStr(ASSET_UTIL)
        assertEquals(CONTENT, content)
        verify { view.assetStr(any()) }
    }

    @Test
    fun assetStr_shouldReadContent_whenInGlobal() {
        mockkStatic("com.hhy.util.resource.AssetResourcesKt")
        val context = mockk<Context> {
            every { assetStr(any()) } returns CONTENT
        }
        context.injectAsAppCtx()
        val content = appAssetStr(ASSET_UTIL)
        assertEquals(CONTENT, content)
        verify { context.assetStr(any()) }
    }
}
