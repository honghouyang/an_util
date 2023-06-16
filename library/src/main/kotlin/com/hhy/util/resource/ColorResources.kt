package com.hhy.util.resource

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.hhy.util.appctx.appCtx

@SuppressLint("NewApi")
@ColorInt
fun Context.color(@ColorRes colorRes: Int): Int = ContextCompat.getColor(this, colorRes)

fun Fragment.color(@ColorRes colorRes: Int) = requireContext().color(colorRes)

fun View.color(@ColorRes colorRes: Int) = context.color(colorRes)

/**
 * Use this method for non configuration dependent resources when you don't have a [Context]
 * or when you're calling it from an Activity or a Fragment member (as the Context is not
 * initialized yet).
 *
 * For theme dependent resources, the application theme will be implicitly used.
 */
fun appColor(@ColorRes colorRes: Int) = appCtx.color(colorRes)

fun Context.colorSL(@ColorRes colorRes: Int): ColorStateList? =
    ResourcesCompat.getColorStateList(resources, colorRes, theme)

fun Fragment.colorSL(@ColorRes colorRes: Int) = requireContext().colorSL(colorRes)

fun View.colorSL(@ColorRes colorRes: Int) = context.colorSL(colorRes)

/**
 * Use this method for non configuration dependent resources when you don't have a [Context]
 * or when you're calling it from an Activity or a Fragment member (as the Context is not
 * initialized yet).
 *
 * For theme dependent resources, the application theme will be implicitly used.
 */
fun appColorSL(@ColorRes colorRes: Int) = appCtx.colorSL(colorRes)

// Styled resources below

@ColorInt
fun Context.styledColor(@AttrRes attr: Int): Int = color(resolveThemeAttribute(attr))

fun Fragment.styledColor(@AttrRes attr: Int) = requireContext().styledColor(attr)

fun View.styledColor(@AttrRes attr: Int) = context.styledColor(attr)

/**
 * Use this method for non configuration dependent resources when you don't have a [Context]
 * or when you're calling it from an Activity or a Fragment member (as the Context is not
 * initialized yet).
 *
 * For theme dependent resources, the application theme will be implicitly used.
 */
fun appStyledColor(@AttrRes attr: Int) = appCtx.styledColor(attr)

fun Context.styledColorSL(@AttrRes attr: Int): ColorStateList? =
    colorSL(resolveThemeAttribute(attr))

fun Fragment.styledColorSL(@AttrRes attr: Int) = requireContext().styledColorSL(attr)

fun View.styledColorSL(@AttrRes attr: Int) = context.styledColorSL(attr)

/**
 * Use this method for non configuration dependent resources when you don't have a [Context]
 * or when you're calling it from an Activity or a Fragment member (as the Context is not
 * initialized yet).
 *
 * For theme dependent resources, the application theme will be implicitly used.
 */
fun appStyledColorSL(@AttrRes attr: Int) = appCtx.styledColorSL(attr)
