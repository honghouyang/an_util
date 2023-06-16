package com.hhy.util.resource

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.hhy.util.appctx.appCtx
import com.hhy.util.appctx.injectAsAppCtx
import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
@SmallTest
class RawResourcesRbltTest {
    companion object {
        const val CONTENT = "Hello, Kotlin!"
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        ApplicationProvider.getApplicationContext<Context>().injectAsAppCtx()
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun rawStr_shouldReadContent_whenInContext() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        context.injectAsAppCtx()
        val resId = appIdentifier("util", "raw", appCtx.packageName)
        val content = context.rawStr(resId)
        assertEquals(CONTENT, content)
    }
}
