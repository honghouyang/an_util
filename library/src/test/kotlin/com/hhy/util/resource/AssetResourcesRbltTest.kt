package com.hhy.util.resource

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.hhy.util.appctx.appCtx
import com.hhy.util.appctx.injectAsAppCtx
import com.hhy.util.file.isExist
import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
@SmallTest
class AssetResourcesRbltTest {
    companion object {
        const val CONTENT = "Hello, Kotlin!"
        const val ASSET_UTIL = "util"
        const val ASSET_NONE = "none"
        const val ASSET_COPY_1 = "copy1"
        const val ASSET_COPY_2 = "copy2"
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        ApplicationProvider.getApplicationContext<Context>().injectAsAppCtx()
    }

    @After
    fun tearDown() {
        unmockkAll()

        File(ASSET_COPY_1).deleteOnExit()
    }

    @Test
    fun assetStr_shouldReadContent_whenInContext() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        context.injectAsAppCtx()
        val content = appCtx.assetStr(ASSET_UTIL)
        assertEquals(CONTENT, content)
    }

    @Test
    fun assetStr_shouldThrowException_whenAssetNotExist() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        context.injectAsAppCtx()
        assertFailsWith<Exception> { appCtx.assetStr(ASSET_NONE) }
    }

    @Test
    fun assetCopiedTo_shouldCopyFile_whenInContext() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        context.injectAsAppCtx()
        ASSET_UTIL.assetCopiedTo(ASSET_COPY_1)
        assertTrue(File(ASSET_COPY_1).isExist())
        assertEquals(File(ASSET_COPY_1).bufferedReader().readText(), CONTENT)

        File(ASSET_COPY_1).deleteOnExit()
    }

    @Test
    fun assetCopiedTo_shouldThrowException_whenAssetNotExist() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        context.injectAsAppCtx()
        assertFailsWith<Exception> { ASSET_NONE.assetCopiedTo(ASSET_COPY_2) }
        assertFalse(File(ASSET_COPY_2).isExist())
    }
}
