package com.hhy.util.compress.strategy

import android.graphics.Bitmap
import com.hhy.util.compress.CompressUtil
import com.hhy.util.compress.SizeType
import com.hhy.util.compress.compressFormat
import java.io.File

internal class LimitMemoryStrategy(
    private val maxSize: Long,
    private val sizeType: SizeType,
    private val bitmapConfig: Bitmap.Config? = null,
    private val resultFormat: Bitmap.CompressFormat? = null,
) : Strategy {
    private var isCompress = false
    override fun isCompressed(): Boolean {
        return isCompress
    }

    override fun compress(imageFile: File): File? {
        val bitmap = CompressUtil.compressToMemoryLimit(
            imageFile,
            maxSize,
            sizeType,
            bitmapConfig,
        )
        val rotationResult = CompressUtil.determineImageRotation(imageFile, bitmap)
        val result = CompressUtil.compressBitmapByQualityOrFormat(
            imageFile = imageFile,
            bitmap = rotationResult,
            resultFormat = resultFormat ?: imageFile.compressFormat()
        )
        isCompress = true
        return result
    }
}
