package com.hhy.util.compress.strategy

import android.graphics.Bitmap
import com.hhy.util.compress.CompressUtil
import java.io.File

internal class SampleSizeStrategy(
    private val width: Int,
    private val height: Int,
    private val bitmapConfig: Bitmap.Config? = null,
) : Strategy {
    private var isCompress = false
    override fun isCompressed(): Boolean {
        return isCompress
    }

    override fun compress(imageFile: File): File? {
        val bitmap = CompressUtil.compressSampleSize(
            imageFile,
            width,
            height,
            bitmapConfig,
        )
        val rotationResult = CompressUtil.determineImageRotation(imageFile, bitmap)
        val result = CompressUtil.compressBitmapByQualityOrFormat(
            imageFile = imageFile,
            bitmap = rotationResult,
        )
        isCompress = true
        return result
    }
}
