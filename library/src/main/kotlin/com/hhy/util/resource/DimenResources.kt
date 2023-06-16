package com.hhy.util.resource

import android.content.Context
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.DimenRes
import androidx.fragment.app.Fragment
import com.hhy.util.appctx.appCtx

fun Context.dimen(@DimenRes dimenResId: Int): Float = resources.getDimension(dimenResId)

fun Fragment.dimen(@DimenRes dimenResId: Int) = requireContext().dimen(dimenResId)

fun View.dimen(@DimenRes dimenResId: Int) = context.dimen(dimenResId)

/**
 * Use this method for non configuration dependent resources when you don't have a [Context]
 * or when you're calling it from an Activity or a Fragment member (as the Context is not
 * initialized yet).
 *
 * For theme dependent resources, the application theme will be implicitly used.
 */
fun appDimen(@DimenRes dimenResId: Int) = appCtx.dimen(dimenResId)

fun Context.dimenPxSize(@DimenRes dimenResId: Int): Int =
    resources.getDimensionPixelSize(dimenResId)

fun Fragment.dimenPxSize(@DimenRes dimenResId: Int) =
    requireContext().dimenPxSize(dimenResId)

fun View.dimenPxSize(@DimenRes dimenResId: Int) = context.dimenPxSize(dimenResId)

/**
 * Use this method for non configuration dependent resources when you don't have a [Context]
 * or when you're calling it from an Activity or a Fragment member (as the Context is not
 * initialized yet).
 *
 * For theme dependent resources, the application theme will be implicitly used.
 */
fun appDimenPxSize(@DimenRes dimenResId: Int) = appCtx.dimenPxSize(dimenResId)

fun Context.dimenPxOffset(@DimenRes dimenResId: Int): Int =
    resources.getDimensionPixelOffset(dimenResId)

fun Fragment.dimenPxOffset(@DimenRes dimenResId: Int) =
    requireContext().dimenPxOffset(dimenResId)

fun View.dimenPxOffset(@DimenRes dimenResId: Int) = context.dimenPxOffset(dimenResId)

/**
 * Use this method for non configuration dependent resources when you don't have a [Context]
 * or when you're calling it from an Activity or a Fragment member (as the Context is not
 * initialized yet).
 *
 * For theme dependent resources, the application theme will be implicitly used.
 */
fun appDimenPxOffset(@DimenRes dimenResId: Int) = appCtx.dimenPxOffset(dimenResId)

// Styled resources below

fun Context.styledDimen(@AttrRes attr: Int): Float = dimen(resolveThemeAttribute(attr))

fun Fragment.styledDimen(@AttrRes attr: Int) = requireContext().styledDimen(attr)

fun View.styledDimen(@AttrRes attr: Int) = context.styledDimen(attr)

/**
 * Use this method for non configuration dependent resources when you don't have a [Context]
 * or when you're calling it from an Activity or a Fragment member (as the Context is not
 * initialized yet).
 *
 * For theme dependent resources, the application theme will be implicitly used.
 */
fun appStyledDimen(@AttrRes attr: Int) = appCtx.styledDimen(attr)

fun Context.styledDimenPxSize(@AttrRes attr: Int): Int = dimenPxSize(resolveThemeAttribute(attr))

fun Fragment.styledDimenPxSize(@AttrRes attr: Int) = requireContext().styledDimenPxSize(attr)

fun View.styledDimenPxSize(@AttrRes attr: Int) = context.styledDimenPxSize(attr)

/**
 * Use this method for non configuration dependent resources when you don't have a [Context]
 * or when you're calling it from an Activity or a Fragment member (as the Context is not
 * initialized yet).
 *
 * For theme dependent resources, the application theme will be implicitly used.
 */
fun appStyledDimenPxSize(@AttrRes attr: Int) = appCtx.styledDimenPxSize(attr)

fun Context.styledDimenPxOffset(@AttrRes attr: Int): Int =
    dimenPxOffset(resolveThemeAttribute(attr))

fun Fragment.styledDimenPxOffset(@AttrRes attr: Int) =
    requireContext().styledDimenPxOffset(attr)

fun View.styledDimenPxOffset(@AttrRes attr: Int) = context.styledDimenPxOffset(attr)

/**
 * Use this method for non configuration dependent resources when you don't have a [Context]
 * or when you're calling it from an Activity or a Fragment member (as the Context is not
 * initialized yet).
 *
 * For theme dependent resources, the application theme will be implicitly used.
 */
fun appStyledDimenPxOffset(@AttrRes attr: Int) = appCtx.styledDimenPxOffset(attr)
