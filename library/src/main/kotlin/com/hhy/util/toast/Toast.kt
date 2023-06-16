package com.hhy.util.toast

import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.hhy.util.appctx.appCtx
import com.hhy.util.os.layoutInflater
import com.hhy.util.os.osVerCode
import com.hhy.util.os.windowManager
import com.hhy.util.resource.txt
import kotlin.LazyThreadSafetyMode.NONE

internal fun Context.createToast(text: CharSequence, duration: Int): Toast {
    val ctx = if (osVerCode == Build.VERSION_CODES.N_MR1) SafeToastCtx(this) else this
    return Toast.makeText(ctx, text, duration)
}

internal fun Context.createToast(@StringRes resId: Int, duration: Int): Toast {
    return createToast(txt(resId), duration)
}

fun Context.toast(@StringRes msgResId: Int) =
    createToast(msgResId, Toast.LENGTH_SHORT).show()

fun Fragment.toast(@StringRes msgResId: Int) = ctx.toast(msgResId)

fun View.toast(@StringRes msgResId: Int) = context.toast(msgResId)

fun toast(@StringRes msgResId: Int) = appCtx.toast(msgResId)

fun Context.toast(msg: CharSequence) = createToast(msg, Toast.LENGTH_SHORT).show()

fun Fragment.toast(msg: CharSequence) = ctx.toast(msg)

fun View.toast(msg: CharSequence) = context.toast(msg)

fun toast(msg: CharSequence) = appCtx.toast(msg)

fun Context.longToast(@StringRes msgResId: Int) =
    createToast(msgResId, Toast.LENGTH_LONG).show()

fun Fragment.longToast(@StringRes msgResId: Int) = ctx.longToast(msgResId)

fun View.longToast(@StringRes msgResId: Int) = context.longToast(msgResId)

fun longToast(@StringRes msgResId: Int) = appCtx.longToast(msgResId)

fun Context.longToast(msg: CharSequence) = createToast(msg, Toast.LENGTH_LONG).show()

fun Fragment.longToast(msg: CharSequence) = ctx.longToast(msg)

fun View.longToast(msg: CharSequence) = context.longToast(msg)

fun longToast(msg: CharSequence) = appCtx.longToast(msg)

internal val Fragment.ctx: Context
    get() = context ?: appCtx

/**
 * Avoids [WindowManager.BadTokenException] on API 25.
 */
private class SafeToastCtx(ctx: Context) : ContextWrapper(ctx) {
    private val toastWindowManager by lazy(NONE) { ToastWindowManager(baseContext.windowManager) }

    private val toastLayoutInflater by lazy(NONE) {
        baseContext.layoutInflater.cloneInContext(this)
    }

    override fun getApplicationContext(): Context = SafeToastCtx(baseContext.applicationContext)

    override fun getSystemService(name: String): Any = when (name) {
        Context.LAYOUT_INFLATER_SERVICE -> toastLayoutInflater
        Context.WINDOW_SERVICE -> toastWindowManager
        else -> super.getSystemService(name)
    }

    private class ToastWindowManager(private val base: WindowManager) : WindowManager by base {
        override fun addView(view: View?, params: ViewGroup.LayoutParams?) {
            try {
                base.addView(view, params)
            } catch (e: WindowManager.BadTokenException) {
                Log.e("SafeToast", "Couldn't add Toast to WindowManager", e)
            }
        }
    }
}
