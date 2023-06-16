package com.hhy.util.resource

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.AnyRes
import androidx.annotation.AttrRes
import com.hhy.util.thread.isMainThread

@AnyRes
fun Context.resolveThemeAttribute(
    @AttrRes attrRes: Int,
    resolveRefs: Boolean = true
): Int = if (isMainThread) {
    if (theme.resolveAttribute(attrRes, uiThreadConfinedCachedTypeValue, resolveRefs).not()) {
        throw Resources.NotFoundException(
            "Couldn't resolve attribute resource #0x" + Integer.toHexString(attrRes) +
                " from the theme of this Context."
        )
    }
    uiThreadConfinedCachedTypeValue.resourceId
} else {
    synchronized(cachedTypeValue) {
        if (theme.resolveAttribute(attrRes, cachedTypeValue, resolveRefs).not()) {
            throw Resources.NotFoundException(
                "Couldn't resolve attribute resource #0x" + Integer.toHexString(attrRes) +
                    " from the theme of this Context."
            )
        }
        cachedTypeValue.resourceId
    }
}

private val uiThreadConfinedCachedTypeValue = TypedValue()
private val cachedTypeValue = TypedValue()
