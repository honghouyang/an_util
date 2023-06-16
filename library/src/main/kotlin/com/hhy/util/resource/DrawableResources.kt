package com.hhy.util.resource

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.hhy.util.appctx.appCtx

fun Context.drawable(@DrawableRes drawableResId: Int): Drawable? =
    ResourcesCompat.getDrawable(resources, drawableResId, theme)

fun Fragment.drawable(@DrawableRes drawableResId: Int) = requireContext().drawable(drawableResId)

fun View.drawable(@DrawableRes drawableResId: Int) = context.drawable(drawableResId)

/**
 * Use this method for non configuration dependent resources when you don't have a [Context]
 * or when you're calling it from an Activity or a Fragment member (as the Context is not
 * initialized yet).
 *
 * For theme dependent resources, the application theme will be implicitly used.
 */
fun appDrawable(@DrawableRes drawableResId: Int) = appCtx.drawable(drawableResId)

// Styled resources below

fun Context.styledDrawable(@AttrRes attr: Int): Drawable? = drawable(resolveThemeAttribute(attr))

fun Fragment.styledDrawable(@AttrRes attr: Int) = requireContext().styledDrawable(attr)

fun View.styledDrawable(@AttrRes attr: Int) = context.styledDrawable(attr)

/**
 * Use this method for non configuration dependent resources when you don't have a [Context]
 * or when you're calling it from an Activity or a Fragment member (as the Context is not
 * initialized yet).
 *
 * For theme dependent resources, the application theme will be implicitly used.
 */
fun appStyledDrawable(@AttrRes attr: Int) = appCtx.styledDrawable(attr)
