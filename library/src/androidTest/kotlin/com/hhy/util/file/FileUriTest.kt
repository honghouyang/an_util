package com.hhy.util.file

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.hhy.util.appctx.injectAsAppCtx
import com.hhy.util.compress.CompressUtil
import com.hhy.util.compress.toBitmap
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private const val TAG = "FileUriTest"

@RunWith(AndroidJUnit4::class)
@SmallTest
class FileUriTest {
    lateinit var mContext: Context

    companion object {
        const val greaterThan10PrefixURI = "content://com.android.providers.downloads.documents/d" +
            "ocument/raw%3A%2Fstorage%2Femulated%2F0%2FDownload%2F"
        const val androidLessThanOrEqualsTo10PrefixURI =
            "content://com.android.providers.downloads.documents/document/raw%3A%2Fstorage%2Femul" +
                "ated%2F0%2FDownload%2F"
    }

    @Before
    fun setUp() {
        mContext = ApplicationProvider.getApplicationContext()
        mContext.injectAsAppCtx()
    }

    @After
    fun tearDown() {
    }

    private fun getTestUri(name: String): Uri {
        return when {
            Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q -> {
                // version <= 10
                Uri.parse("${androidLessThanOrEqualsTo10PrefixURI}$name")
            }
            else -> {
                // version > 10
                Uri.parse("${greaterThan10PrefixURI}$name")
            }
        }
    }

    @Test
    fun getPathByUri() {
        val uri = getTestUri("test1.jpg")
        // val uri = Uri.parse("content://com.hhy.i18n.saas.courier.debug.fileProvider/app_util_ext_pub/Pictures/20220419_10_27_541307755785.jpg")
        val path = FileUri.getPathByUri(mContext, uri)
        Log.i(TAG, "uri: $uri")
        Log.i(TAG, "path: $path")
        assertTrue(!path.isNullOrEmpty())
    }

    @Test
    fun getBitmapByFile() {
        val file = getFileTest()
        assertFile(file)
        val bitmap = FileUri.getBitmapByFile(file)
        assertBitmap(bitmap)
    }

    @Test
    fun getFile() {
        val file = getFileTest()
        assertFile(file)
    }

    @Test
    fun getFileSize() {
        val file = getFileTest()
        assertFile(file)
        val size = FileUri.getFileSize(file)
        assertTrue {
            size.contains("B") ||
                size.contains("KB") ||
                size.contains("MB") ||
                size.contains("GB")
        }
    }

    @Test
    fun byteToSize() {
        val file = getFileTest()
        assertFile(file)
        val size = FileUri.byteToSize(file?.calSize() ?: 0)
        assertTrue {
            size.contains("B") ||
                size.contains("KB") ||
                size.contains("MB") ||
                size.contains("GB")
        }
    }

    @Test
    fun getImageFileMemoryByteCount() {
        val bitmapWidth = 1080
        val bitmapHeight = 1920
        val bitmapConfig = Bitmap.Config.ARGB_8888
        val bitmapUsedMemory = FileUri.getImageFileMemoryByteCount(
            bitmapWidth,
            bitmapHeight,
            bitmapConfig,
        )
        assertTrue(bitmapUsedMemory != 0L)
    }

    @Test
    fun getFileToBitmapMemorySize() {
        val file = getFileTest()
        assertFile(file)
        val size = FileUri.getFileToBitmapMemorySize(file)
        assertTrue(size != 0L)
    }

    @Test
    fun getBitmapMemorySize() {
        val file = getFileTest()
        assertFile(file)
        val bitmap = file?.toBitmap()
        assertBitmap(bitmap)
        val size = FileUri.getBitmapMemorySize(bitmap)
        assertTrue(size != 0L)
    }

    private fun getFileTest(): File? {
        val uri = getTestUri("test2.jpg")
        return FileUri.getFileByUri(mContext, uri)
    }

    @Test
    fun getFileByPathTest() {
        val file = getFileTest()
        assertFile(file)
        val path = file?.absolutePath
        val newFile = FileUri.getFileByPath(path)
        assertFile(newFile)
    }

    @Test
    fun getFileNameByUri() {
        val file = getFileTest()
        assertFile(file)
        val uri = FileUri.getUriByFile(mContext, file, false)
        Log.i(TAG, "getFileNameByUri: ${uri?.toString()}")
        assertTrue(ContentResolver.SCHEME_CONTENT.equals(uri?.scheme, ignoreCase = true))

        val newFileName = FileUri.getFileNameByUri(mContext, uri)
        assertEquals(file?.name, newFileName)
    }

    @Test
    fun getUriByFile() {
        val file = getFileTest()
        assertFile(file)
        val filePath = file?.absolutePath
        val uri = FileUri.getUriByFile(mContext, file, true)
        Log.i(TAG, "getUriByFile: ${uri?.toString()}")
        assertTrue(ContentResolver.SCHEME_FILE.equals(uri?.scheme, ignoreCase = true))
        val newPath = FileUri.getPathByUri(mContext, uri)
        assertEquals(filePath, newPath)
    }

    @Test
    fun getOriginalUri() {
        val file = getFileTest()
        assertFile(file)
        val uri = FileUri.getOriginalUri(mContext, file)
        Log.i(TAG, "getOriginalUri: ${uri?.toString()}")
        assertTrue(ContentResolver.SCHEME_FILE.equals(uri?.scheme, ignoreCase = true))
        val newFileName = FileUri.getFileNameByUri(mContext, uri)
        assertEquals(file?.name, newFileName)
    }

    @Test
    fun getShareUri() {
        val file = getFileTest()
        assertFile(file)
        val uri = FileUri.getShareUri(mContext, file)
        Log.i(TAG, "getShareUri: ${uri?.toString()}")
        assertTrue(ContentResolver.SCHEME_CONTENT.equals(uri?.scheme, ignoreCase = true))
        val newFileName = FileUri.getFileNameByUri(mContext, uri)
        assertEquals(file?.name, newFileName)
    }

    @Test
    fun deleteDir() {
        val directory = File(CompressUtil.cacheDirPath(mContext))
        val success = FileUri.deleteDir(directory)
        assertTrue(!directory.isExist() || success)
        assertTrue(!directory.isExist())
    }

    private fun assertFile(result: File?) {
        assertTrue { result.isExist() }
        assertTrue { result?.isFile == true }
        assertTrue { result?.calSize() != 0L }
    }

    private fun assertBitmap(bitmap: Bitmap?) {
        assertTrue { bitmap != null }
        assertTrue { FileUri.getBitmapMemorySize(bitmap) != 0L }
    }
}
