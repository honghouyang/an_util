package com.hhy.util.resource

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import com.hhy.util.appctx.appCtx

fun Context.identifier(name: String, defType: String?, defPackage: String): Int =
    resources.getIdentifier(name, defType, defPackage)

fun Fragment.identifier(name: String, defType: String, defPackage: String) =
    requireContext().identifier(name, defType, defPackage)

fun View.identifier(name: String, defType: String, defPackage: String) =
    context.identifier(name, defType, defPackage)

/**
 * Use this method for non configuration dependent resources when you don't have a [Context]
 * or when you're calling it from an Activity or a Fragment member (as the Context is not
 * initialized yet).
 *
 * For theme dependent resources, the application theme will be implicitly used.
 */
fun appIdentifier(name: String, defType: String, defPackage: String) =
    appCtx.identifier(name, defType, defPackage)
