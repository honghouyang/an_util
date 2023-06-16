package com.hhy.util.resource

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import com.hhy.util.appctx.appCtx
import java.io.File

fun Context.assetStr(fileName: String) =
    assets.open(fileName).bufferedReader().use { it.readText() }

fun Fragment.assetStr(fileName: String) = requireContext().assetStr(fileName)

fun View.assetStr(fileName: String) = context.assetStr(fileName)

/**
 * Use this method for non configuration dependent resources when you don't have a [Context]
 * or when you're calling it from an Activity or a Fragment member (as the Context is not
 * initialized yet).
 *
 * For theme dependent resources, the application theme will be implicitly used.
 */
fun appAssetStr(fileName: String) = appCtx.assetStr(fileName)

fun String.assetCopiedTo(targetFilePath: String) {
    val stream = appCtx.assets.open(this)
    stream.buffered(1024)
        .reader(Charsets.UTF_8)
        .use {
            File(targetFilePath).writeText(it.readText())
        }
}
