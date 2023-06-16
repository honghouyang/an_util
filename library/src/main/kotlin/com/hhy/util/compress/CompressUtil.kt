package com.hhy.util.compress

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Rect
import android.media.ExifInterface
import android.util.Log
import com.hhy.util.appctx.appCtx
import com.hhy.util.file.FileUri
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.Locale

internal object CompressUtil {
    /** cache file prefix. */
    val filePrefix = "compress_${System.currentTimeMillis()}_"

    /** compress dir. */
    private val compressDir = "${File.separator}compressor${File.separator}"

    /** app cache dir. */
    private fun appCacheDir(context: Context): String =
        context.externalCacheDir?.path ?: appCtx.cacheDir.path

    /** create cache file path. */
    fun cacheDirPath(context: Context): String {
        return appCacheDir(context) + compressDir
    }

    /** compress to bitmap with SampleSize. */
    fun compressSampleSize(
        imageFile: File,
        width: Int,
        height: Int,
        bitmapConfig: Bitmap.Config? = null,
    ): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
                BitmapFactory.decodeFile(imageFile.absolutePath, this)
                inSampleSize = calculateInSampleSize(this, width, height)
                inPreferredConfig = bitmapConfig ?: Bitmap.Config.ARGB_8888
                inJustDecodeBounds = false
            }
            bitmap = BitmapFactory.decodeFile(imageFile.absolutePath, options)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, e.toString())
        }
        return bitmap
    }

    /** Calculate bitmap SampleSize. */
    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        width: Int,
        height: Int
    ): Int {
        val bitmapWidth: Int = options.outWidth
        val bitmapHeight: Int = options.outHeight
        var sampleSize = 1
        if (bitmapWidth > width || bitmapHeight > height) {
            sampleSize = 2
            while (bitmapWidth / sampleSize > width || bitmapHeight / sampleSize > height) {
                sampleSize *= 2
            }
        }
        return sampleSize
    }

    /** File compressed to memory limit. */
    fun compressToMemoryLimit(
        imageFile: File,
        memorySize: Long,
        sizeType: SizeType,
        bitmapConfig: Bitmap.Config? = Bitmap.Config.ARGB_8888,
    ): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
                BitmapFactory.decodeFile(imageFile.absolutePath, this)
                val limitByteCount = sizeToVByteCount(memorySize, sizeType)
                inSampleSize = calculateInSampleSizeByMemoryLimit(this, limitByteCount)
                inPreferredConfig = bitmapConfig
                inJustDecodeBounds = false
            }
            bitmap = BitmapFactory.decodeFile(imageFile.absolutePath, options)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, e.toString())
        }
        return bitmap
    }

    private fun sizeToVByteCount(memorySize: Long, sizeType: SizeType): Long {
        return when (sizeType) {
            SizeType.B -> {
                memorySize
            }
            SizeType.KB -> {
                memorySize * 1024
            }
            SizeType.MB -> {
                memorySize * 1024 * 1024
            }
            SizeType.GB -> {
                memorySize * 1024 * 1024
            }
        }
    }

    /** Calculate bitmap SampleSize by memory limit. */
    private fun calculateInSampleSizeByMemoryLimit(
        options: BitmapFactory.Options,
        limitCount: Long,
    ): Int {
        val bitmapWidth: Int = options.outWidth
        val bitmapHeight: Int = options.outHeight
        val bitmapConfig: Bitmap.Config = options.inPreferredConfig
        var inSampleSize = 1
        var bitmapUsedMemory = FileUri.getImageFileMemoryByteCount(
            bitmapWidth,
            bitmapHeight,
            bitmapConfig,
        )
        while ((bitmapUsedMemory) > limitCount) {
            inSampleSize *= 2
            val bitmapResultWidth = bitmapWidth / inSampleSize
            val bitmapResultHeight = bitmapHeight / inSampleSize
            bitmapUsedMemory = FileUri.getImageFileMemoryByteCount(
                bitmapResultWidth,
                bitmapResultHeight,
                bitmapConfig,
            )
        }
        return inSampleSize
    }

    /** File compressed by size limit. */
    fun compressBitmapBySizeLimit(
        imageFile: File,
        bitmap: Bitmap?,
        step: Int,
        maxSize: Long,
        sizeType: SizeType,
        bitmapConfig: Bitmap.Config?,
        resultFormat: Bitmap.CompressFormat?,
    ): File? {
        val format = resultFormat ?: imageFile.compressFormat()
        val result = if (resultFormat == imageFile.compressFormat()) {
            imageFile
        } else {
            File(
                imageFile.absolutePath.substringBeforeLast(".") +
                    ".${format.extension()}"
            )
        }
        val outputStream = ByteArrayOutputStream()

        val options: BitmapFactory.Options?
        try {
            options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
                BitmapFactory.decodeFile(imageFile.absolutePath, this)
                inPreferredConfig = bitmapConfig
                inJustDecodeBounds = false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, e.toString())
            return null
        }
        imageFile.delete()
        try {
            val ratio = 2
            var targetWidth = options.outWidth / ratio
            var targetHeight = options.outHeight / ratio

            var quality = 90
            var resultBitmap = generateScaledBitmap(
                bitmap = bitmap,
                targetWidth = targetWidth,
                targetHeight = targetHeight,
                outputStream = outputStream,
                resultFormat = format,
                quality = quality,
                bitmapConfig = bitmapConfig,
            )
            var count = 0
            val maxByteCount = sizeToVByteCount(maxSize, sizeType)
            while (outputStream.size() > maxByteCount && count <= 5) {
                targetWidth /= ratio
                targetHeight /= ratio
                count++
                quality -= step
                outputStream.reset()
                resultBitmap = generateScaledBitmap(
                    bitmap = resultBitmap,
                    targetWidth = targetWidth,
                    targetHeight = targetHeight,
                    outputStream = outputStream,
                    resultFormat = format,
                    quality = quality,
                    bitmapConfig = bitmapConfig,
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, e.toString())
        } finally {
            FileOutputStream(result).run {
                write(outputStream.toByteArray())
                flush()
                close()
            }
        }
        return result
    }

    private fun generateScaledBitmap(
        bitmap: Bitmap?,
        targetWidth: Int,
        targetHeight: Int,
        outputStream: ByteArrayOutputStream,
        bitmapConfig: Bitmap.Config? = null,
        resultFormat: Bitmap.CompressFormat,
        quality: Int,
    ): Bitmap? {
        if (bitmap == null) return null
        val result =
            Bitmap.createBitmap(targetWidth, targetHeight, bitmapConfig ?: Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)
        val rect = Rect(0, 0, result.width, result.height)
        canvas.drawBitmap(bitmap, null, rect, null)
        if (!bitmap.isRecycled) {
            bitmap.recycle()
        }
        result.compress(resultFormat, quality, outputStream)
        return result
    }

    /** File compressed to bitmap with quality or format. */
    fun compressBitmapByQualityOrFormat(
        imageFile: File,
        bitmap: Bitmap?,
        quality: Int = 100,
        resultFormat: Bitmap.CompressFormat? = imageFile.compressFormat(),
    ): File? {
        var result: File? = null
        if (bitmap != null) {
            result = if (resultFormat == imageFile.compressFormat()) {
                imageFile
            } else {
                File(
                    "${imageFile.absolutePath.substringBeforeLast(".")}." +
                        "${resultFormat?.extension()}"
                )
            }
            imageFile.delete()
            result.parentFile?.mkdirs()
            var fileOutputStream: FileOutputStream? = null
            try {
                fileOutputStream = FileOutputStream(result.absolutePath)
                bitmap.compress(resultFormat, quality, fileOutputStream)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e(TAG, e.toString())
            } finally {
                fileOutputStream?.run {
                    flush()
                    close()
                }
            }
        } else {
            imageFile.delete()
        }
        return result
    }

    /** Load bitmap with image file. */
    fun loadBitmapByConfig(
        imageFile: File,
        bitmapConfig: Bitmap.Config? = null,
    ): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
                BitmapFactory.decodeFile(imageFile.absolutePath, this)
                inPreferredConfig = bitmapConfig ?: Bitmap.Config.ARGB_8888
                inJustDecodeBounds = false
            }
            bitmap = BitmapFactory.decodeFile(imageFile.absolutePath, options)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, e.toString())
        }
        return determineImageRotation(imageFile, bitmap)
    }

    /** Determine the image rotation angle. */
    fun determineImageRotation(imageFile: File?, bitmap: Bitmap?): Bitmap? {
        if (imageFile == null || bitmap == null) return null
        @Suppress("Deprecation")
        if (imageFile.compressFormat() == Bitmap.CompressFormat.WEBP) return bitmap
        val exif = ExifInterface(imageFile.absolutePath)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)
        val matrix = Matrix()
        when (orientation) {
            6 -> matrix.postRotate(90f)
            3 -> matrix.postRotate(180f)
            8 -> matrix.postRotate(270f)
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
}

enum class SizeType {
    B,
    KB,
    MB,
    GB,
}

/** Return bitmap format extension. */
fun Bitmap.CompressFormat.extension() = when (this) {
    Bitmap.CompressFormat.PNG -> "png"
    Bitmap.CompressFormat.JPEG -> "jpg"
    @Suppress("Deprecation") Bitmap.CompressFormat.WEBP -> "webp"
    else -> "jpg"
}

/** Return file compress format. */
fun File.compressFormat() = when (extension.lowercase(Locale.getDefault())) {
    "png" -> Bitmap.CompressFormat.PNG
    "jpg" -> Bitmap.CompressFormat.JPEG
    "webp" -> @Suppress("Deprecation") Bitmap.CompressFormat.WEBP
    else -> Bitmap.CompressFormat.JPEG
}

fun File.toBitmap(): Bitmap? {
    return FileUri.getBitmapByFile(this)
}

/** Copy file to cache path. */
fun File.createCacheFile(
    context: Context,
    fileName: String?,
    fileFormat: Bitmap.CompressFormat? = Bitmap.CompressFormat.JPEG,
): File {
    val fileSuffix = if (fileName != null && fileFormat != null) {
        "$fileName.${fileFormat.extension()}"
    } else {
        name
    }
    val path = CompressUtil.cacheDirPath(context) + CompressUtil.filePrefix + fileSuffix
    return copyTo(File(path), true)
}
