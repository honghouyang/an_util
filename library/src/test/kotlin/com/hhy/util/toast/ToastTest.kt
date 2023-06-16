package com.hhy.util.toast

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.test.filters.SmallTest
import com.hhy.util.appctx.injectAsAppCtx
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertSame

@SmallTest
class ToastTest {
    companion object {
        const val MSG = "Hello, Kotlin!"
        const val RES_ID = 17
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
    fun ctxExtFragment_shouldReturnCurContext_whenContextNotNull() {
        val contextX = mockk<Context>()
        val fragment = mockk<Fragment> {
            every { context } returns contextX
        }
        assertSame(contextX, fragment.ctx)
        verify { fragment.context }
    }

    @Test
    fun ctxExtFragment_shouldReturnAppCtx_whenContextNull() {
        val contextX = mockk<Context>()
        val fragment = mockk<Fragment> {
            every { context } returns null
        }
        contextX.injectAsAppCtx()
        assertSame(contextX, fragment.ctx)
        verify { fragment.context }
    }

    @Test
    fun toastWithChars_shouldShow_whenInContext() {
        mockkStatic("com.hhy.util.toast.ToastKt")
        val toast = mockk<Toast> {
            justRun { show() }
        }
        every { any<Context>().createToast(any<CharSequence>(), any()) } returns toast
        val context = mockk<Context>()
        context.toast(MSG)
        verify { context.createToast(any<CharSequence>(), any()) }
        verify { toast.show() }
    }

    @Test
    fun toastWithChars_shouldShow_whenInFragment() {
        mockkStatic("com.hhy.util.toast.ToastKt")
        justRun { any<Context>().toast(any<CharSequence>()) }
        val contextX = mockk<Context>()
        val fragment = mockk<Fragment> {
            every { context } returns contextX
        }
        fragment.toast(MSG)
        verify { fragment.context }
        verify { contextX.toast(any<CharSequence>()) }
    }

    @Test
    fun toastWithChars_shouldShow_whenInView() {
        mockkStatic("com.hhy.util.toast.ToastKt")
        justRun { any<Context>().toast(any<CharSequence>()) }
        val contextX = mockk<Context>()
        val view = mockk<View> {
            every { context } returns contextX
        }
        view.toast(MSG)
        verify { view.context }
        verify { contextX.toast(any<CharSequence>()) }
    }

    @Test
    fun toastWithChars_shouldShow_whenInGlobal() {
        mockkStatic("com.hhy.util.toast.ToastKt")
        justRun { any<Context>().toast(any<CharSequence>()) }
        val contextX = mockk<Context>()
        contextX.injectAsAppCtx()
        toast(MSG)
        verify { contextX.toast(any<CharSequence>()) }
    }

    @Test
    fun toastWithResId_shouldShow_whenInContext() {
        mockkStatic("com.hhy.util.toast.ToastKt")
        val toast = mockk<Toast> {
            justRun { show() }
        }
        every { any<Context>().createToast(any<Int>(), any()) } returns toast
        val context = mockk<Context>()
        context.toast(RES_ID)
        verify { context.createToast(any<Int>(), any()) }
        verify { toast.show() }
    }

    @Test
    fun toastWithResId_shouldShow_whenInFragment() {
        mockkStatic("com.hhy.util.toast.ToastKt")
        justRun { any<Context>().toast(any<Int>()) }
        val contextX = mockk<Context>()
        val fragment = mockk<Fragment> {
            every { context } returns contextX
        }
        fragment.toast(RES_ID)
        verify { fragment.context }
        verify { contextX.toast(any<Int>()) }
    }

    @Test
    fun toastWithResId_shouldShow_whenInView() {
        mockkStatic("com.hhy.util.toast.ToastKt")
        justRun { any<Context>().toast(any<Int>()) }
        val contextX = mockk<Context>()
        val view = mockk<View> {
            every { context } returns contextX
        }
        view.toast(RES_ID)
        verify { view.context }
        verify { contextX.toast(any<Int>()) }
    }

    @Test
    fun toastWithResId_shouldShow_whenInGlobal() {
        mockkStatic("com.hhy.util.toast.ToastKt")
        justRun { any<Context>().toast(any<Int>()) }
        val contextX = mockk<Context>()
        contextX.injectAsAppCtx()
        toast(RES_ID)
        verify { contextX.toast(any<Int>()) }
    }

    @Test
    fun longToastWithChars_shouldShow_whenInContext() {
        mockkStatic("com.hhy.util.toast.ToastKt")
        val toast = mockk<Toast> {
            justRun { show() }
        }
        every { any<Context>().createToast(any<CharSequence>(), any()) } returns toast
        val context = mockk<Context>()
        context.longToast(MSG)
        verify { context.createToast(any<CharSequence>(), any()) }
        verify { toast.show() }
    }

    @Test
    fun longToastWithChars_shouldShow_whenInFragment() {
        mockkStatic("com.hhy.util.toast.ToastKt")
        justRun { any<Context>().longToast(any<CharSequence>()) }
        val contextX = mockk<Context>()
        val fragment = mockk<Fragment> {
            every { context } returns contextX
        }
        fragment.longToast(MSG)
        verify { fragment.context }
        verify { contextX.longToast(any<CharSequence>()) }
    }

    @Test
    fun longToastWithChars_shouldShow_whenInView() {
        mockkStatic("com.hhy.util.toast.ToastKt")
        justRun { any<Context>().longToast(any<CharSequence>()) }
        val contextX = mockk<Context>()
        val view = mockk<View> {
            every { context } returns contextX
        }
        view.longToast(MSG)
        verify { view.context }
        verify { contextX.longToast(any<CharSequence>()) }
    }

    @Test
    fun longToastWithChars_shouldShow_whenInGlobal() {
        mockkStatic("com.hhy.util.toast.ToastKt")
        justRun { any<Context>().longToast(any<CharSequence>()) }
        val contextX = mockk<Context>()
        contextX.injectAsAppCtx()
        longToast(MSG)
        verify { contextX.longToast(any<CharSequence>()) }
    }

    @Test
    fun longToastWithResId_shouldShow_whenInContext() {
        mockkStatic("com.hhy.util.toast.ToastKt")
        val toast = mockk<Toast> {
            justRun { show() }
        }
        every { any<Context>().createToast(any<Int>(), any()) } returns toast
        val context = mockk<Context>()
        context.longToast(RES_ID)
        verify { context.createToast(any<Int>(), any()) }
        verify { toast.show() }
    }

    @Test
    fun longToastWithResId_shouldShow_whenInFragment() {
        mockkStatic("com.hhy.util.toast.ToastKt")
        justRun { any<Context>().longToast(any<Int>()) }
        val contextX = mockk<Context>()
        val fragment = mockk<Fragment> {
            every { context } returns contextX
        }
        fragment.longToast(RES_ID)
        verify { fragment.context }
        verify { contextX.longToast(any<Int>()) }
    }

    @Test
    fun longToastWithResId_shouldShow_whenInView() {
        mockkStatic("com.hhy.util.toast.ToastKt")
        justRun { any<Context>().longToast(any<Int>()) }
        val contextX = mockk<Context>()
        val view = mockk<View> {
            every { context } returns contextX
        }
        view.longToast(RES_ID)
        verify { view.context }
        verify { contextX.longToast(any<Int>()) }
    }

    @Test
    fun longToastWithResId_shouldShow_whenInGlobal() {
        mockkStatic("com.hhy.util.toast.ToastKt")
        justRun { any<Context>().longToast(any<Int>()) }
        val contextX = mockk<Context>()
        contextX.injectAsAppCtx()
        longToast(RES_ID)
        verify { contextX.longToast(any<Int>()) }
    }
}
