package com.hhy.util.compress

import android.graphics.Bitmap
import com.hhy.util.compress.strategy.FormatStrategy
import com.hhy.util.compress.strategy.LimitMemoryStrategy
import com.hhy.util.compress.strategy.LimitSizeStrategy
import com.hhy.util.compress.strategy.QualityStrategy
import com.hhy.util.compress.strategy.SampleSizeStrategy
import com.hhy.util.compress.strategy.Strategy

class Compression {
    /** list of compression strategies. */
    internal val strategies: MutableList<Strategy> = mutableListOf()

    fun addStrategies(strategy: Strategy) {
        strategies.add(strategy)
    }
}

/** Default Compression. */
fun Compression.default() {
    sampleSize(width = 200, height = 200)
    format(Bitmap.CompressFormat.JPEG)
}

/** Quality Compression. */
fun Compression.quality(quality: Int) = addStrategies(QualityStrategy(quality))

/** Format Compression. */
fun Compression.format(format: Bitmap.CompressFormat) = addStrategies(FormatStrategy(format))

/** SampleSize Compression. */
fun Compression.sampleSize(
    width: Int = 1080,
    height: Int = 1920,
    bitmapConfig: Bitmap.Config? = Bitmap.Config.RGB_565,
) = addStrategies(SampleSizeStrategy(width, height, bitmapConfig))

/** Limit memory Compression. */
fun Compression.memoryLimit(
    maxSize: Long,
    sizeType: SizeType = SizeType.B,
    bitmapConfig: Bitmap.Config? = Bitmap.Config.RGB_565,
    resultFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
) = addStrategies(LimitMemoryStrategy(maxSize, sizeType, bitmapConfig, resultFormat))

/** Limit size Compression. */
fun Compression.sizeLimit(
    maxSize: Long,
    sizeType: SizeType = SizeType.B,
    step: Int,
    bitmapConfig: Bitmap.Config? = Bitmap.Config.RGB_565,
    resultFormat: Bitmap.CompressFormat,
) = addStrategies(LimitSizeStrategy(maxSize, sizeType, step, bitmapConfig, resultFormat))
