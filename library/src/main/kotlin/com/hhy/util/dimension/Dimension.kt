package com.hhy.util.dimension

import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import com.hhy.util.os.windowManager
import com.hhy.util.resource.appDimenPxOffset
import com.hhy.util.resource.appIdentifier

fun Context.dip(value: Int): Int = (value * resources.displayMetrics.density).toInt()
fun Context.dp(value: Int): Float = (value * resources.displayMetrics.density)

fun View.dip(value: Int) = context.dip(value)
fun View.dp(value: Int) = context.dp(value)

val screenHeight: Int
    get() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

val screenWidth: Int
    get() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }

val screenRealHeight: Int
    get() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getRealMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

val screenRealWidth: Int
    get() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getRealMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }

val navigationBarHeight: Int
    get() {
        val resourceId: Int = appIdentifier("navigation_bar_height", "dimen", "android")
        return appDimenPxOffset(resourceId)
    }

val statusBarHeight: Int
    get() {
        val resourceId: Int = appIdentifier("status_bar_height", "dimen", "android")
        return appDimenPxOffset(resourceId)
    }
