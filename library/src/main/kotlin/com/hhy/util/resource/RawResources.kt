package com.hhy.util.resource

import android.content.Context
import android.view.View
import androidx.annotation.RawRes
import androidx.fragment.app.Fragment
import com.hhy.util.appctx.appCtx

fun Context.rawStr(@RawRes rawResId: Int) =
    resources.openRawResource(rawResId).bufferedReader().use { it.readText() }

fun Fragment.rawStr(@RawRes rawResId: Int) = requireContext().rawStr(rawResId)

fun View.rawStr(@RawRes rawResId: Int) = context.rawStr(rawResId)

/**
 * Use this method for non configuration dependent resources when you don't have a [Context]
 * or when you're calling it from an Activity or a Fragment member (as the Context is not
 * initialized yet).
 *
 * For theme dependent resources, the application theme will be implicitly used.
 */
fun appRawStr(@RawRes rawResId: Int) = appCtx.rawStr(rawResId)
