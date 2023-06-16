package com.hhy.util.file

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import androidx.core.content.FileProvider
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.min

private const val TAG = "FileUri"

@Suppress("Deprecation")
object FileUri {

    private const val FILE_PROVIDER_AUTHORITY = ".fileProvider"

    /**
     * Obtain file path by uri.
     * need permission: RequiresPermission(permission.READ_EXTERNAL_STORAGE)
     * */
    fun getPathByUri(context: Context?, uri: Uri?): String? {
        if (context == null || uri == null) return ""
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(
                context,
                uri
            ) -> {
                getPathWithDocumentProvider(context, uri)
            }
            "content".equals(uri.scheme, ignoreCase = true) -> {
                when {
                    isGooglePhotosUri(uri) -> {
                        uri.lastPathSegment
                    }
                    isGoogleDriveUri(uri) -> {
                        getGoogleDriveFilePath(uri, context)
                    }
                    isHuaWeiUri(uri) -> {
                        val uriPath = getDataColumn(context, uri) ?: uri.toString()
                        when {
                            uriPath.startsWith("/root") -> {
                                uriPath.replace("/root".toRegex(), "")
                            }
                            else -> null
                        }
                    }
                    else -> {
                        return when {
                            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                                getFilePathForN(context, uri)
                            }
                            else -> {
                                getDataColumn(context, uri)
                            }
                        }
                    }
                }
            }
            "file".equals(uri.scheme, ignoreCase = true) -> {
                uri.path
            }
            else -> {
                getDataColumn(context, uri)
            }
        }
    }

    /** obtain path with document provider. */
    private fun getPathWithDocumentProvider(
        context: Context,
        uri: Uri
    ): String? {
        when {
            // LocalStorageProvider
            isLocalStorageDocument(context, uri) -> {
                return DocumentsContract.getDocumentId(uri)
            }
            // ExternalStorageProvider
            isExternalStorageDocument(uri) -> {
                return getPathWithExternalStorageDocument(uri, context)
            }
            // DownloadsProvider
            isDownloadsDocument(uri) -> {
                val id = DocumentsContract.getDocumentId(uri)
                if (id != null && id.startsWith("raw:")) {
                    return id.substring(4)
                }
                when {
                    Build.VERSION.SDK_INT < Build.VERSION_CODES.O -> {
                        val contentUriPrefixesToTry = arrayOf(
                            "content://downloads/public_downloads",
                            "content://downloads/my_downloads",
                            "content://downloads/all_downloads"
                        )
                        for (contentUriPrefix in contentUriPrefixesToTry) {
                            val contentUri =
                                ContentUris.withAppendedId(Uri.parse(contentUriPrefix), id.toLong())
                            try {
                                val path = getDataColumn(context, contentUri)
                                if (!path.isNullOrBlank()) return path
                            } catch (e: Exception) {
                                Log.e(TAG, "getDataColumn_exception $e")
                            }
                        }
                    }
                    else -> {
                        return getDataColumn(context, uri)
                    }
                }
            }
            // MediaProvider
            isMediaDocument(uri) -> {
                return getPathWithMediaDocument(uri, context)
            }
            // GoogleDriveProvider
            isGoogleDriveUri(uri) -> {
                return getGoogleDriveFilePath(uri, context)
            }
        }
        return null
    }

    /** obtain path with external storage document provider. */
    private fun getPathWithExternalStorageDocument(
        uri: Uri,
        context: Context
    ): String {
        val docId = DocumentsContract.getDocumentId(uri)
        val split = docId.split(":").toTypedArray()
        val type = split[0]
        when {
            "primary".equals(type, ignoreCase = true) -> {
                return when {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                        context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
                            .toString() + File.separator + split[1]
                    }
                    else -> {
                        Environment.getExternalStorageDirectory()
                            .toString() + File.separator + split[1]
                    }
                }
            }
            "home".equals(type, ignoreCase = true) -> {
                return when {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                        context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
                            .toString() + File.separator + "documents" + File.separator + split[1]
                    }
                    else -> {
                        Environment.getExternalStorageDirectory()
                            .toString() + File.separator + "documents" + File.separator + split[1]
                    }
                }
            }
            else -> {
                val sdcardPath =
                    Environment.getExternalStorageDirectory()
                        .toString() + File.separator + "documents" + File.separator + split[1]
                return when {
                    sdcardPath.startsWith("file://") -> {
                        sdcardPath.replace("file://", "")
                    }
                    else -> {
                        sdcardPath
                    }
                }
            }
        }
    }

    /** obtain path with media document provider. */
    private fun getPathWithMediaDocument(
        uri: Uri,
        context: Context
    ): String? {
        val docId = DocumentsContract.getDocumentId(uri)
        val split = docId.split(":").toTypedArray()
        val contentUri: Uri? = when (split[0]) {
            "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            "audio" -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            "download" -> when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                    MediaStore.Downloads.EXTERNAL_CONTENT_URI
                }
                else -> null
            }
            else -> null
        }
        val selection = "_id=?"
        val selectionArgs = arrayOf(split[1])
        return getDataColumn(context, contentUri, selection, selectionArgs)
    }

    /** ************************************ The Uri to check ************************************ */
    /** Whether the uri is a local one. */
    private fun isLocal(url: String?): Boolean {
        return url != null && !url.startsWith("http://") && !url.startsWith("https://")
    }

    /** Whether the uri authority is local. */
    private fun isLocalStorageDocument(context: Context?, uri: Uri): Boolean {
        return (context?.applicationContext?.packageName.toString() + FILE_PROVIDER_AUTHORITY)
            .equals(
                uri.authority,
                true
            )
    }

    /** Whether the uri authority is ExternalStorageProvider. */
    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalStorage.documents".equals(uri.authority, true)
    }

    /** Whether the uri authority is DownloadsProvider. */
    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents".equals(uri.authority, true)
    }

    /** Whether the uri authority is MediaProvider. */
    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents".equals(uri.authority, true)
    }

    /** Whether the uri authority is Google Photos. */
    private fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content".equals(uri.authority, true)
    }

    /** Whether the uri authority is Google Drive. */
    private fun isGoogleDriveUri(uri: Uri?): Boolean {
        return ("com.google.android.apps.docs.storage.legacy".equals(uri?.authority, true)) ||
            ("com.google.android.apps.docs.storage".equals(uri?.authority, true))
    }

    /**
     * Whether the uri authority is HuaWei Device.
     * content://com.huawei.hidisk.fileProvider/root/storage/emulated/0/Android/data/com.xxx.xxx/
     * */
    private fun isHuaWeiUri(uri: Uri?): Boolean {
        return "com.huawei.hidisk$FILE_PROVIDER_AUTHORITY".equals(uri?.authority, true)
    }

    /** ************************************ The Uri to check ************************************ */

    /** obtain the value of the data column for this Uri. */
    private fun getDataColumn(
        context: Context,
        uri: Uri?,
        selection: String? = null,
        selectionArgs: Array<String>? = null,
    ): String? {
        if (uri == null) return null
        var cursor: Cursor? = null
        val column = MediaStore.Images.Media.DATA
        val projection = arrayOf(column)
        try {
            cursor = context.contentResolver.query(
                uri, projection, selection, selectionArgs, null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(columnIndex)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "getDataColumn $e")
        } finally {
            cursor?.close()
        }
        return null
    }

    /**
     * obtain file path.
     * version >= N
     * */
    private fun getFilePathForN(context: Context, uri: Uri): String? {
        val name = uri.toString().substringAfterLast("/")
        val file = File(context.filesDir, name)
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            var read: Int
            val maxBufferSize = 1 * 1024 * 1024
            val bytesAvailable = inputStream!!.available()
            val bufferSize = min(bytesAvailable, maxBufferSize)
            val buffers = ByteArray(bufferSize)
            while (inputStream.read(buffers).also { read = it } != -1) {
                outputStream.write(buffers, 0, read)
            }
            inputStream.close()
            outputStream.close()
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
        }
        return file.path
    }

    /** Obtain Google Drive file path. */
    private fun getGoogleDriveFilePath(uri: Uri, context: Context): String? {
        context.contentResolver.query(uri, null, null, null, null)?.use { c: Cursor ->
            val nameIndex: Int = c.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (!c.moveToFirst()) {
                return uri.toString()
            }
            val name: String = c.getString(nameIndex)
            val file = File(context.cacheDir, name)
            var inputStream: InputStream? = null
            var outputStream: FileOutputStream? = null
            try {
                inputStream = context.contentResolver.openInputStream(uri)
                outputStream = FileOutputStream(file)
                var read = 0
                val maxBufferSize = 1 * 1024 * 1024
                val bytesAvailable: Int = inputStream?.available() ?: 0
                val bufferSize = bytesAvailable.coerceAtMost(maxBufferSize)
                val buffers = ByteArray(bufferSize)
                while (inputStream?.read(buffers)?.also { read = it } != -1) {
                    outputStream.write(buffers, 0, read)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                inputStream?.close()
                outputStream?.close()
            }
            return file.path
        }
        return uri.toString()
    }

    /** Obtain bitmap by file. */
    fun getBitmapByFile(file: File?): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val bis = BufferedInputStream(FileInputStream(file?.path))
            bitmap = BitmapFactory.decodeStream(bis)
            bis.close()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, e.toString())
        }
        return bitmap
    }

    /** Obtain file by uri. */
    fun getFileByUri(context: Context?, uri: Uri?): File? {
        if (context == null || uri == null) return null
        val path = getPathByUri(context, uri)
        if (path != null && isLocal(path)) {
            return getFileByPath(path)
        }
        return null
    }

    /** Obtain file by path. */
    fun getFileByPath(path: String?): File? {
        if (path == null) return null
        if (isLocal(path)) {
            return File(path)
        }
        return null
    }

    /** Obtain file name by uri. */
    fun getFileNameByUri(context: Context?, uri: Uri?): String? {
        if (context == null || uri == null) return ""
        val mimeType = context.contentResolver.getType(uri)
        var name: String? = null
        if (mimeType == null) {
            val path = getPathByUri(context, uri)
            if (path != null) {
                val file = File(path)
                name = file.name
            }
        } else {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            if (cursor != null) {
                val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                cursor.moveToFirst()
                name = cursor.getString(index)
                cursor.close()
            }
        }
        return name
    }

    /** Return a content URI for a given file.
     * isOriginal(true): content://xxx
     * isOriginal(false): file:///xxx
     */
    fun getUriByFile(context: Context, file: File?, isOriginal: Boolean = false): Uri? {
        val result: Uri = when {
            isOriginal -> {
                Uri.fromFile(file)
            }
            else -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    val authority =
                        context.applicationContext?.packageName.toString() + FILE_PROVIDER_AUTHORITY
                    FileProvider.getUriForFile(context, authority, file ?: return null)
                } else {
                    Uri.fromFile(file)
                }
            }
        }
        return result
    }

    /**
     * Return content://xxx
     */
    fun getShareUri(context: Context, file: File?): Uri? =
        getUriByFile(context, file, isOriginal = false)

    /**
     * Return file:///xxx
     */
    fun getOriginalUri(context: Context, file: File?): Uri? =
        getUriByFile(context, file, isOriginal = true)

    /** Obtain image file memory size. */
    fun getImageFileMemoryByteCount(
        imageWidth: Int,
        imageHeight: Int,
        bitmapConfig: Bitmap.Config
    ): Long {
        // one pixel occupies the bit
        val pixelBit = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                when (bitmapConfig) {
                    Bitmap.Config.ALPHA_8 -> 8
                    Bitmap.Config.RGB_565 -> 16
                    Bitmap.Config.ARGB_4444 -> 16
                    Bitmap.Config.ARGB_8888 -> 32
                    else -> 64
                }
            }
            else -> {
                when (bitmapConfig) {
                    Bitmap.Config.ALPHA_8 -> 8
                    Bitmap.Config.RGB_565 -> 16
                    Bitmap.Config.ARGB_4444 -> 16
                    Bitmap.Config.ARGB_8888 -> 32
                    else -> 0
                }
            }
        }
        // picture color depth
        val colorDepthByte = pixelBit / 8
        val byteCount = imageWidth * imageHeight * colorDepthByte
        return byteCount.toLong()
    }

    /** Obtain file to Bitmap memory size. */
    fun getFileToBitmapMemorySize(file: File?): Long {
        var memorySize: Long? = null
        try {
            val bis = BufferedInputStream(FileInputStream(file?.path))
            BitmapFactory.Options().apply {
                inJustDecodeBounds = true
                BitmapFactory.decodeStream(bis, null, this)
                memorySize = getImageFileMemoryByteCount(
                    outWidth,
                    outHeight,
                    inPreferredConfig,
                )
                inJustDecodeBounds = false
            }
            bis.close()
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e(TAG, e.toString())
        }
        return memorySize ?: 0
    }

    /** Obtain bitmap memory size. */
    fun getBitmapMemorySize(bitmap: Bitmap?): Long {
        if (bitmap == null || bitmap.byteCount <= 0) return 0
        val byteCount: Long = when {
            // API 19
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> {
                bitmap.allocationByteCount.toLong()
            }
            // API 12
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1 -> {
                bitmap.byteCount.toLong()
            }
            // Other
            else -> {
                (bitmap.rowBytes * bitmap.height).toLong()
            }
        }
        return byteCount
    }

    /** Obtain file size. */
    fun getFileSize(file: File?): String {
        if (file == null || !file.isExist() || !file.isFile) return "-1"
        return byteToSize(file.calSize())
    }

    /** Convert byte to size. */
    fun byteToSize(byteCount: Long): String {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        val result = when (byteCount) {
            in (0..1024) -> {
                // B
                "${df.format(byteCount / 1f)} B"
            }
            in ((1 + 1024)..(1024 * 1024)) -> {
                // KB
                "${df.format(byteCount / 1024f)} KB"
            }
            in ((1 + (1024 * 1024))..(1024 * 1024 * 1024)) -> {
                // MB
                "${df.format(byteCount / (1024f * 1024f))} MB"
            }
            else -> {
                // GB
                "${df.format((byteCount / (1024f * 1024f * 1024f)))} GB"
            }
        }.toString()
        return result
    }

    /** delete directory. */
    fun deleteDir(dir: File?): Boolean {
        if (dir == null || !dir.exists()) return false
        if (dir.isDirectory) {
            val list = dir.list()
            if (list != null && list.isNotEmpty()) {
                for (children in list) {
                    val success = deleteDir(File(dir, children))
                    if (!success) {
                        return false
                    }
                }
            }
        }
        return dir.delete()
    }
}
