package com.hhy.util.compress.strategy

import android.graphics.Bitmap
import com.hhy.util.compress.CompressUtil
import com.hhy.util.compress.SizeType
import com.hhy.util.compress.compressFormat
import java.io.File

internal class LimitSizeStrategy(
    private val maxSize: Long,
    private val sizeType: SizeType,
    private val step: Int,
    private val bitmapConfig: Bitmap.Config? = null,
    private val resultFormat: Bitmap.CompressFormat? = null,
) : Strategy {
    private var isCompress = false
    override fun isCompressed(): Boolean {
        return isCompress
    }

    override fun compress(imageFile: File): File? {
        val bitmap = CompressUtil.loadBitmapByConfig(imageFile, bitmapConfig)
        val result = CompressUtil.compressBitmapBySizeLimit(
            imageFile = imageFile,
            bitmap = bitmap,
            maxSize = maxSize,
            step = step,
            sizeType = sizeType,
            bitmapConfig = bitmapConfig,
            resultFormat = resultFormat ?: imageFile.compressFormat(),
        )
        isCompress = true
        return result
    }
}
