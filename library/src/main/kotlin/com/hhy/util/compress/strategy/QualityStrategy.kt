package com.hhy.util.compress.strategy

import com.hhy.util.compress.CompressUtil
import java.io.File

internal class QualityStrategy(
    private val quality: Int,
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
            quality = quality,
        )
        isCompress = true
        return result
    }
}
