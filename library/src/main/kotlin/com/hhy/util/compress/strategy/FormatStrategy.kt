package com.hhy.util.compress.strategy

import android.graphics.Bitmap
import com.hhy.util.compress.CompressUtil

import java.io.File

internal class FormatStrategy(
    private val resultFormat: Bitmap.CompressFormat,
) : Strategy {
    private var isCompress = false
    override fun isCompressed(): Boolean {
        return isCompress
    }

    override fun compress(imageFile: File): File? {
        val bitmap = CompressUtil.loadBitmapByConfig(imageFile)
        val result = CompressUtil.compressBitmapByQualityOrFormat(
            imageFile = imageFile,
            bitmap = bitmap,
            resultFormat = resultFormat,
        )
        isCompress = true
        return result
    }
}
