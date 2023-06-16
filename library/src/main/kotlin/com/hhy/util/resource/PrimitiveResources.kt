package com.hhy.util.resource

import android.content.Context
import android.view.View
import androidx.annotation.ArrayRes
import androidx.annotation.AttrRes
import androidx.annotation.BoolRes
import androidx.annotation.IntegerRes
import androidx.fragment.app.Fragment
import com.hhy.util.appctx.appCtx

fun Context.bool(@BoolRes boolResId: Int): Boolean = resources.getBoolean(boolResId)

fun Fragment.bool(@BoolRes boolResId: Int) = requireContext().bool(boolResId)

fun View.bool(@BoolRes boolResId: Int) = context.bool(boolResId)

/**
 * Use this method for non configuration dependent resources when you don't have a [Context]
 * or when you're calling it from an Activity or a Fragment member (as the Context is not
 * initialized yet).
 *
 * For theme dependent resources, the application theme will be implicitly used.
 */
fun appBool(@BoolRes boolResId: Int) = appCtx.bool(boolResId)

fun Context.int(@IntegerRes intResId: Int): Int = resources.getInteger(intResId)

fun Fragment.int(@IntegerRes intResId: Int) = requireContext().int(intResId)

fun View.int(@IntegerRes intResId: Int) = context.int(intResId)

/**
 * Use this method for non configuration dependent resources when you don't have a [Context]
 * or when you're calling it from an Activity or a Fragment member (as the Context is not
 * initialized yet).
 *
 * For theme dependent resources, the application theme will be implicitly used.
 */
fun appInt(@IntegerRes intResId: Int) = appCtx.int(intResId)

fun Context.intArray(@ArrayRes intArrayResId: Int): IntArray =
    resources.getIntArray(intArrayResId)

fun Fragment.intArray(@ArrayRes intArrayResId: Int) = requireContext().intArray(intArrayResId)

fun View.intArray(@ArrayRes intArrayResId: Int) = context.intArray(intArrayResId)

/**
 * Use this method for non configuration dependent resources when you don't have a [Context]
 * or when you're calling it from an Activity or a Fragment member (as the Context is not
 * initialized yet).
 *
 * For theme dependent resources, the application theme will be implicitly used.
 */
fun appIntArray(@ArrayRes intArrayResId: Int) = appCtx.intArray(intArrayResId)

// Styled resources below

fun Context.styledBool(@AttrRes attr: Int): Boolean = bool(resolveThemeAttribute(attr))

fun Fragment.styledBool(@AttrRes attr: Int) = requireContext().styledBool(attr)

fun View.styledBool(@AttrRes attr: Int) = context.styledBool(attr)

/**
 * Use this method for non configuration dependent resources when you don't have a [Context]
 * or when you're calling it from an Activity or a Fragment member (as the Context is not
 * initialized yet).
 *
 * For theme dependent resources, the application theme will be implicitly used.
 */
fun appStyledBool(@AttrRes attr: Int) = appCtx.styledBool(attr)

fun Context.styledInt(@AttrRes attr: Int): Int = int(resolveThemeAttribute(attr))

fun Fragment.styledInt(@AttrRes attr: Int) = requireContext().styledInt(attr)

fun View.styledInt(@AttrRes attr: Int) = context.styledInt(attr)

/**
 * Use this method for non configuration dependent resources when you don't have a [Context]
 * or when you're calling it from an Activity or a Fragment member (as the Context is not
 * initialized yet).
 *
 * For theme dependent resources, the application theme will be implicitly used.
 */
fun appStyledInt(@AttrRes attr: Int) = appCtx.styledInt(attr)
