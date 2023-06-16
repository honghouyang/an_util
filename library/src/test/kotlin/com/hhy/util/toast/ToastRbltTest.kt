package com.hhy.util.toast

import android.content.Context
import android.widget.Toast
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.hhy.util.appctx.appCtx
import com.hhy.util.appctx.injectAsAppCtx
import com.hhy.util.resource.appIdentifier
import com.hhy.util.resource.appStr
import io.mockk.MockKAnnotations
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.shadows.ShadowToast
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
@SmallTest
class ToastRbltTest {
    companion object {
        const val MSG = "Hello, Kotlin!"
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
    fun createToast_shouldReturnToast_whenMsgIsString() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val toast = context.createToast(MSG, Toast.LENGTH_SHORT)
        assertEquals(Toast.LENGTH_SHORT, toast.duration)
        toast.show()
        assertSame(toast, ShadowToast.getLatestToast())
        assertEquals(MSG, ShadowToast.getTextOfLatestToast())
        assertTrue {
            ShadowToast.showedToast(MSG)
        }
    }

    @Test
    fun createToast_shouldReturnToast_whenMsgIsRes() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val resId = appIdentifier("util_hi_kotlin", "string", appCtx.packageName)
        val toast = context.createToast(resId, Toast.LENGTH_SHORT)
        assertEquals(Toast.LENGTH_SHORT, toast.duration)
        toast.show()
        val msg = appStr(resId)
        assertSame(toast, ShadowToast.getLatestToast())
        assertEquals(msg, ShadowToast.getTextOfLatestToast())
        assertTrue {
            ShadowToast.showedToast(msg)
        }
    }
}
