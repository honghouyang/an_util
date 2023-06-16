package com.hhy.util.resource

import android.content.Context
import android.view.View
import androidx.annotation.ArrayRes
import androidx.annotation.AttrRes
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.hhy.util.appctx.appCtx

fun Context.txt(@StringRes stringResId: Int): CharSequence = resources.getText(stringResId)

fun Fragment.txt(@StringRes stringResId: Int) = requireContext().txt(stringResId)

fun View.txt(@StringRes stringResId: Int) = context.txt(stringResId)

/**
 * Use this method for non configuration dependent resources when you don't have a [Context]
 * or when you're calling it from an Activity or a Fragment member (as the Context is not
 * initialized yet).
 *
 * For theme dependent resources, the application theme will be implicitly used.
 */
fun appTxt(@StringRes stringResId: Int) = appCtx.txt(stringResId)

fun Context.str(@StringRes stringResId: Int, vararg formatArgs: Any?): String =
    resources.getString(stringResId, *formatArgs)

fun Fragment.str(@StringRes stringResId: Int, vararg formatArgs: Any?) =
    requireContext().str(stringResId, *formatArgs)

fun View.str(@StringRes stringResId: Int, vararg formatArgs: Any?) =
    context.str(stringResId, *formatArgs)

/**
 * Use this method for non configuration dependent resources when you don't have a [Context]
 * or when you're calling it from an Activity or a Fragment member (as the Context is not
 * initialized yet).
 *
 * For theme dependent resources, the application theme will be implicitly used.
 */
fun appStr(@StringRes stringResId: Int, vararg formatArgs: Any?) =
    appCtx.str(stringResId, *formatArgs)

fun Context.str(@StringRes stringResId: Int): String = resources.getString(stringResId)

fun Fragment.str(@StringRes stringResId: Int) = requireContext().str(stringResId)

fun View.str(@StringRes stringResId: Int) = context.str(stringResId)

/**
 * Use this method for non configuration dependent resources when you don't have a [Context]
 * or when you're calling it from an Activity or a Fragment member (as the Context is not
 * initialized yet).
 *
 * For theme dependent resources, the application theme will be implicitly used.
 */
fun appStr(@StringRes stringResId: Int) = appCtx.str(stringResId)

fun Context.qtyTxt(@PluralsRes stringResId: Int, quantity: Int): CharSequence {
    return resources.getQuantityText(stringResId, quantity)
}

fun Fragment.qtyTxt(@PluralsRes stringResId: Int, quantity: Int) =
    requireContext().qtyTxt(stringResId, quantity)

fun View.qtyTxt(@PluralsRes stringResId: Int, quantity: Int) = context.qtyTxt(stringResId, quantity)

/**
 * Use this method for non configuration dependent resources when you don't have a [Context]
 * or when you're calling it from an Activity or a Fragment member (as the Context is not
 * initialized yet).
 *
 * For theme dependent resources, the application theme will be implicitly used.
 */
fun appQtyTxt(@PluralsRes stringResId: Int, quantity: Int) = appCtx.qtyTxt(stringResId, quantity)

fun Context.qtyStr(@PluralsRes stringResId: Int, quantity: Int): String {
    return resources.getQuantityString(stringResId, quantity)
}

fun Fragment.qtyStr(@PluralsRes stringResId: Int, quantity: Int) =
    requireContext().qtyStr(stringResId, quantity)

fun View.qtyStr(@PluralsRes stringResId: Int, quantity: Int) = context.qtyStr(stringResId, quantity)

/**
 * Use this method for non configuration dependent resources when you don't have a [Context]
 * or when you're calling it from an Activity or a Fragment member (as the Context is not
 * initialized yet).
 *
 * For theme dependent resources, the application theme will be implicitly used.
 */
fun appQtyStr(@PluralsRes stringResId: Int, quantity: Int) = appCtx.qtyStr(stringResId, quantity)

fun Context.qtyStr(@PluralsRes stringResId: Int, quantity: Int, vararg formatArgs: Any?): String =
    resources.getQuantityString(stringResId, quantity, *formatArgs)

fun Fragment.qtyStr(@PluralsRes stringResId: Int, quantity: Int, vararg formatArgs: Any?) =
    requireContext().qtyStr(stringResId, quantity, *formatArgs)

fun View.qtyStr(@PluralsRes stringResId: Int, quantity: Int, vararg formatArgs: Any?) =
    context.qtyStr(stringResId, quantity, *formatArgs)

/**
 * Use this method for non configuration dependent resources when you don't have a [Context]
 * or when you're calling it from an Activity or a Fragment member (as the Context is not
 * initialized yet).
 *
 * For theme dependent resources, the application theme will be implicitly used.
 */
fun appQtyStr(@PluralsRes stringResId: Int, quantity: Int, vararg formatArgs: Any?) =
    appCtx.qtyStr(stringResId, quantity, *formatArgs)

fun Context.txtArray(@ArrayRes stringResId: Int): Array<out CharSequence> =
    resources.getTextArray(stringResId)

fun Fragment.txtArray(@ArrayRes stringResId: Int) = requireContext().txtArray(stringResId)

fun View.txtArray(@ArrayRes stringResId: Int) = context.txtArray(stringResId)

/**
 * Use this method for non configuration dependent resources when you don't have a [Context]
 * or when you're calling it from an Activity or a Fragment member (as the Context is not
 * initialized yet).
 *
 * For theme dependent resources, the application theme will be implicitly used.
 */
fun appTxtArray(@ArrayRes stringResId: Int) = appCtx.txtArray(stringResId)

fun Context.strArray(@ArrayRes stringResId: Int): Array<String> =
    resources.getStringArray(stringResId)

fun Fragment.strArray(@ArrayRes stringResId: Int) = requireContext().strArray(stringResId)

fun View.strArray(@ArrayRes stringResId: Int) = context.strArray(stringResId)

/**
 * Use this method for non configuration dependent resources when you don't have a [Context]
 * or when you're calling it from an Activity or a Fragment member (as the Context is not
 * initialized yet).
 *
 * For theme dependent resources, the application theme will be implicitly used.
 */
fun appStrArray(@ArrayRes stringResId: Int) = appCtx.strArray(stringResId)

// Styled resources below

fun Context.styledTxt(@AttrRes attr: Int): CharSequence? = txt(resolveThemeAttribute(attr))

fun Fragment.styledTxt(@AttrRes attr: Int) = requireContext().styledTxt(attr)

fun View.styledTxt(@AttrRes attr: Int) = context.styledTxt(attr)

/**
 * Use this method for non configuration dependent resources when you don't have a [Context]
 * or when you're calling it from an Activity or a Fragment member (as the Context is not
 * initialized yet).
 *
 * For theme dependent resources, the application theme will be implicitly used.
 */
fun appStyledTxt(@AttrRes attr: Int) = appCtx.styledTxt(attr)

fun Context.styledStr(@AttrRes attr: Int): String? = str(resolveThemeAttribute(attr))

fun Fragment.styledStr(@AttrRes attr: Int) = requireContext().styledStr(attr)

fun View.styledStr(@AttrRes attr: Int) = context.styledStr(attr)

/**
 * Use this method for non configuration dependent resources when you don't have a [Context]
 * or when you're calling it from an Activity or a Fragment member (as the Context is not
 * initialized yet).
 *
 * For theme dependent resources, the application theme will be implicitly used.
 */
fun appStyledStr(@AttrRes attr: Int) = appCtx.styledStr(attr)

fun Context.styledStr(@AttrRes attr: Int, vararg formatArgs: Any?): String? =
    str(resolveThemeAttribute(attr), *formatArgs)

fun Fragment.styledStr(@AttrRes attr: Int, vararg formatArgs: Any?) =
    requireContext().styledStr(attr, *formatArgs)

fun View.styledStr(@AttrRes attr: Int, vararg formatArgs: Any?) =
    context.styledStr(attr, *formatArgs)

/**
 * Use this method for non configuration dependent resources when you don't have a [Context]
 * or when you're calling it from an Activity or a Fragment member (as the Context is not
 * initialized yet).
 *
 * For theme dependent resources, the application theme will be implicitly used.
 */
fun appStyledStr(@AttrRes attr: Int, vararg formatArgs: Any?) = appCtx.styledStr(attr, *formatArgs)

fun Context.styledTxtArray(@AttrRes attr: Int): Array<out CharSequence>? =
    txtArray(resolveThemeAttribute(attr))

fun Fragment.styledTxtArray(@AttrRes attr: Int) = requireContext().styledTxtArray(attr)

fun View.styledTxtArray(@AttrRes attr: Int) = context.styledTxtArray(attr)

/**
 * Use this method for non configuration dependent resources when you don't have a [Context]
 * or when you're calling it from an Activity or a Fragment member (as the Context is not
 * initialized yet).
 *
 * For theme dependent resources, the application theme will be implicitly used.
 */
fun appStyledTxtArray(@AttrRes attr: Int) = appCtx.styledTxtArray(attr)
