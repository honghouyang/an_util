package com.hhy.util.compress

import android.Manifest
import android.content.Context
import android.net.Uri
import android.util.Log
import com.hhy.util.file.FileUri
import com.hhy.util.file.isExist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import kotlin.coroutines.CoroutineContext
import kotlin.system.measureTimeMillis

const val TAG = "Compressor"

object Compressor {
    /**
     * compress form image file.
     * Permissions required [WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE]
     * */
    suspend fun compressFormFile(
        context: Context?,
        coroutineContext: CoroutineContext = Dispatchers.IO,
        imageFile: File?,
        resultName: String? = null,
        compression: Compression.() -> Unit = {
            default()
        },
        logSwitch: Boolean = false,
    ) = withContext(coroutineContext) {
        if (checkContext(context)) return@withContext null
        if (checkPermission(context)) return@withContext null
        if (checkImageFile(imageFile)) return@withContext null
        // create cache file
        val result: File? = createCacheFile(context!!, imageFile, resultName)
        if (checkCacheFile(result)) return@withContext null
        return@withContext compressExecution(imageFile, compression, result, logSwitch)
    }

    /**
     * compress form uri.
     * Permissions required [WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE]
     * */
    suspend fun compressFormUri(
        context: Context?,
        coroutineContext: CoroutineContext = Dispatchers.IO,
        fileUri: Uri?,
        resultName: String? = null,
        compression: Compression.() -> Unit = {
            default()
        },
        logSwitch: Boolean = false,
    ) = withContext(coroutineContext) {
        if (checkContext(context)) return@withContext null
        if (checkPermission(context)) return@withContext null
        if (checkFileUri(fileUri)) return@withContext null
        // obtain image file by uri
        val imageFile = obtainImageFileByUri(context, fileUri)
        if (checkImageFile(imageFile)) return@withContext null
        val result: File? = createCacheFile(context!!, imageFile, resultName)
        if (checkCacheFile(result)) return@withContext null
        return@withContext compressExecution(imageFile, compression, result, logSwitch)
    }

    private fun compressExecution(
        imageFile: File?,
        compression: Compression.() -> Unit,
        result: File?,
        logSwitch: Boolean
    ): File? {
        val name = imageFile?.name
        val path = imageFile?.absolutePath
        val oldSize = FileUri.getFileSize(imageFile)
        var compressResult = result
        val duration = measureTimeMillis {
            val strategies = Compression().apply(compression).strategies
            strategies.forEach { strategy ->
                // Sequential execution
                while (!strategy.isCompressed()) {
                    compressResult = strategy.compress(compressResult!!)
                }
            }
        }
        // await compress result
        val newSize = FileUri.getFileSize(compressResult)
        val memorySize = FileUri.byteToSize(FileUri.getFileToBitmapMemorySize(compressResult))
        val resultPath = compressResult?.absolutePath
        if (logSwitch) {
            logPrint(name, path, duration, oldSize, newSize, memorySize, resultPath)
        }
        return compressResult
    }

    private fun checkContext(context: Context?): Boolean {
        if (context == null) {
            Log.e(TAG, "Compression failed!, Please check context is null.")
            return true
        }
        return false
    }

    private fun checkPermission(context: Context?): Boolean {
        val hasPermission = EasyPermissions.hasPermissions(
            context!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        if (!hasPermission) {
            Log.e(TAG, "Compression failed! Please check permission denied.")
            return true
        }
        return false
    }

    private fun checkFileUri(fileUri: Uri?): Boolean {
        if (fileUri == null) {
            Log.e(TAG, "Compression failed!, Please check file uri is null.")
            return true
        }
        return false
    }

    private fun obtainImageFileByUri(
        context: Context?,
        fileUri: Uri?
    ) = FileUri.getFileByUri(context, fileUri)

    private fun checkImageFile(imageFile: File?): Boolean {
        if (imageFile == null || (!imageFile.isExist()) || (!imageFile.isFile)) {
            Log.e(TAG, "Compression failed!, Please check imageFile is null.")
            return true
        }
        return false
    }

    private fun createCacheFile(
        context: Context,
        imageFile: File?,
        resultName: String?
    ) = imageFile?.createCacheFile(context, resultName)

    private fun checkCacheFile(result: File?): Boolean {
        if (result == null || (!result.isExist()) || (!result.isFile)) {
            Log.e(TAG, "Compression failed! create cache file failed")
            return true
        }
        return false
    }

    private fun logPrint(
        fileName: String?,
        filePath: String?,
        duration: Long,
        fileOldSize: String?,
        fileNewSize: String,
        memorySize: String,
        resultPath: String?,
    ) {
        Log.i(TAG, "******** compress success:$filePath********")
        Log.i(TAG, "******** compress thread: ${Thread.currentThread().name} ********")
        Log.i(TAG, "compress【$fileName】: duration      ==>> $duration ms")
        Log.i(TAG, "compress【$fileName】: old file      ==>> $fileOldSize")
        Log.i(TAG, "compress【$fileName】: new file      ==>> $fileNewSize")
        Log.i(TAG, "compress【$fileName】: memory        ==>> $memorySize")
        Log.i(TAG, "compress【$fileName】: resultPath    ==>> $resultPath")
    }
}
