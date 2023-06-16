package com.hhy.util.compress

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.content.PermissionChecker
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.rule.GrantPermissionRule
import com.hhy.util.appctx.injectAsAppCtx
import com.hhy.util.file.FileUri
import com.hhy.util.file.calSize
import com.hhy.util.file.isExist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import kotlin.system.measureTimeMillis
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
@Suppress("Deprecation")
@RunWith(AndroidJUnit4::class)
@MediumTest
@PermissionChecker.PermissionResult
class CompressorTest {
    private lateinit var mContext: Context
    private val greaterThan10PrefixURI =
        "content://com.android.providers.downloads.documents/document/raw%3A%2Fstorage%2Femulated" +
            "%2F0%2FDownload%2F"
    private val androidLessThanOrEqualsTo10PrefixURI =
        "content://com.android.providers.downloads.documents/document/raw%3A%2Fstorage%2Femulated" +
            "%2F0%2FDownload%2F"

    /** is delete cache files.*/
    private var isDeleteResultFile = false

    /** grant temporary permission */
    @get:Rule
    var mRuntimePermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        WRITE_EXTERNAL_STORAGE,
    )

    @Before
    fun setUp() {
        mContext = ApplicationProvider.getApplicationContext()
        mContext.injectAsAppCtx()

        // 测试文件目录：外部存储 sdcard/download/xxx.jpg
        // 1. 先本地准备资源图片若干张，统一命名为 test1.jpg, test2.jpg, test3.jpg...
        // 2. 通过 Device File Explorer 在 sdcard/Download/ 上传准备好的资源图片
        // 3. 压缩成功执行，通过 Device File Explorer 查看压缩结果
        // 4. 压缩结果文件: /sdcard/Android/data/com.hhy.util.test/cache/compressor/xxx.webp
        // *. 使用压缩库缓存位置: /sdcard/Android/data/com.xxx.xxx/cache/compressor/xxx.jpg
    }

    @After
    fun tearDown() {
        // delete cache dir or file
        if (isDeleteResultFile) deleteCacheDir()
    }

    @Test
    fun compressSyncTest() {
        val uri1: Uri = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            // version <= 10
            Uri.parse("${androidLessThanOrEqualsTo10PrefixURI}test1.jpg")
        } else {
            // version > 10
            Uri.parse("${greaterThan10PrefixURI}test1.jpg")
        }
        val imageFile1 = FileUri.getFileByUri(mContext, uri1)
        compressSync(imageFile1)
    }

    @Test
    fun compressAsyncTest() {
        val uri2: Uri = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            // version <= 10
            Uri.parse("${androidLessThanOrEqualsTo10PrefixURI}test2.jpg")
        } else {
            // version > 10
            Uri.parse("${greaterThan10PrefixURI}test2.jpg")
        }
        compressAsync(uri2)
    }

    @Test
    fun bulkCompressTest() {
        runBlocking {
            val uri1: Uri?
            val uri2: Uri?
            val uri3: Uri?
            val uri4: Uri?
            val uri5: Uri?
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                // version <= 10
                uri1 = Uri.parse("${androidLessThanOrEqualsTo10PrefixURI}test1.jpg")
                uri2 = Uri.parse("${androidLessThanOrEqualsTo10PrefixURI}test2.jpg")
                uri3 = Uri.parse("${androidLessThanOrEqualsTo10PrefixURI}test3.jpg")
                uri4 = Uri.parse("${androidLessThanOrEqualsTo10PrefixURI}test4.jpg")
                uri5 = Uri.parse("${androidLessThanOrEqualsTo10PrefixURI}test5.jpg")
            } else {
                // version > 10
                uri1 = Uri.parse("${greaterThan10PrefixURI}test1.jpg")
                uri2 = Uri.parse("${greaterThan10PrefixURI}test2.jpg")
                uri3 = Uri.parse("${greaterThan10PrefixURI}test3.jpg")
                uri4 = Uri.parse("${greaterThan10PrefixURI}test4.jpg")
                uri5 = Uri.parse("${greaterThan10PrefixURI}test5.jpg")
            }
            bulkCompressTest(arrayOf(uri1, uri2, uri3, uri4, uri5))
        }
    }

    private fun compressSync(imageFile: File?) {
        runBlocking {
            val format = Bitmap.CompressFormat.WEBP
            val maxSize = 102400L
            val step = 10
            val bitmapConfig = Bitmap.Config.RGB_565
            val fileName = "test_size_limit_sync"
            val result = Compressor.compressFormFile(
                context = mContext,
                coroutineContext = Dispatchers.IO,
                imageFile = imageFile,
                resultName = fileName,
                compression = {
                    sizeLimit(
                        maxSize = maxSize,
                        step = step,
                        bitmapConfig = bitmapConfig,
                        resultFormat = format,
                    )
                },
                logSwitch = true
            )
            assertResult(result)
        }
    }

    private fun compressAsync(uri: Uri) {
        runBlocking {
            launch {
                val format = Bitmap.CompressFormat.WEBP
                val bitmapConfig = Bitmap.Config.RGB_565
                val fileName = "test_combination_async"
                val result = Compressor.compressFormUri(
                    context = mContext,
                    coroutineContext = Dispatchers.IO,
                    fileUri = uri,
                    resultName = fileName,
                    compression = {
                        sampleSize(
                            width = 720,
                            height = 1280,
                            bitmapConfig = bitmapConfig,
                        )
                        quality(80)
                        format(format)
                    },
                    logSwitch = true
                )
                assertResult(result)
            }
        }
    }

    private fun bulkCompressTest(uris: Array<Uri>) {
        val uri1 = uris[0]
        val uri2 = uris[1]
        val uri3 = uris[2]
        val uri4 = uris[3]
        val uri5 = uris[4]
        runBlocking(Dispatchers.Main) {
            // 异步并发执行
            val duration = measureTimeMillis {
                val job1 = async(Dispatchers.IO) {
                    val format = Bitmap.CompressFormat.JPEG
                    val fileName = "job1_test"
                    val result = Compressor.compressFormUri(
                        context = mContext,
                        coroutineContext = coroutineContext,
                        fileUri = uri1,
                        resultName = fileName,
                        compression = {
                            quality(70)
                            format(format)
                        },
                        logSwitch = true
                    )
                    assertResult(result)
                }
                val job2 = async(Dispatchers.IO) {
                    val format = Bitmap.CompressFormat.JPEG
                    val bitmapConfig = Bitmap.Config.RGB_565
                    val fileName = "job2_test"
                    val result = Compressor.compressFormUri(
                        context = mContext,
                        coroutineContext = coroutineContext,
                        fileUri = uri2,
                        resultName = fileName,
                        compression = {
                            sampleSize(
                                width = 200,
                                height = 200,
                                bitmapConfig = bitmapConfig,
                            )
                            quality(90)
                            format(format)
                        },
                        logSwitch = true
                    )
                    assertResult(result)
                }
                val job3 = async(Dispatchers.IO) {
                    val format = Bitmap.CompressFormat.WEBP
                    val bitmapConfig = Bitmap.Config.RGB_565
                    val fileName = "job3_test"
                    val result = Compressor.compressFormUri(
                        context = mContext,
                        coroutineContext = coroutineContext,
                        fileUri = uri3,
                        resultName = fileName,
                        compression = {
                            sampleSize(
                                width = 400,
                                height = 400,
                                bitmapConfig = bitmapConfig,
                            )
                            quality(90)
                            format(format)
                        },
                        logSwitch = true
                    )
                    assertResult(result)
                }
                val job4 = async(Dispatchers.IO) {
                    val format = Bitmap.CompressFormat.WEBP
                    val bitmapConfig = Bitmap.Config.ARGB_8888
                    val fileName = "job4_test"
                    val result = Compressor.compressFormUri(
                        context = mContext,
                        coroutineContext = coroutineContext,
                        fileUri = uri4,
                        resultName = fileName,
                        compression = {
                            sampleSize(
                                width = 800,
                                height = 800,
                                bitmapConfig = bitmapConfig,
                            )
                            quality(80)
                            format(format)
                        },
                        logSwitch = true
                    )
                    assertResult(result)
                }
                val job5 = async(Dispatchers.IO) {
                    val format = Bitmap.CompressFormat.WEBP
                    val maxSize = 1024102L
                    val bitmapConfig = Bitmap.Config.RGB_565
                    val fileName = "job5_test"
                    val result = Compressor.compressFormUri(
                        context = mContext,
                        coroutineContext = coroutineContext,
                        fileUri = uri5,
                        resultName = fileName,
                        compression = {
                            memoryLimit(
                                maxSize = maxSize,
                                bitmapConfig = bitmapConfig,
                                resultFormat = format,
                            )
                        },
                        logSwitch = true
                    )
                    assertResult(result)
                }
                job1.await()
                job2.await()
                job3.await()
                job4.await()
                job5.await()
            }
            Log.i(TAG, "compress duration ==>> $duration ms")
        }
    }

    private fun deleteCacheDir() {
        val directory = File(CompressUtil.cacheDirPath(mContext))
        val success = FileUri.deleteDir(directory)
        assertTrue(!directory.isExist() || success)
        assertTrue(!directory.isExist())
    }

    private fun assertResult(result: File?) {
        assertFile(result)
        assertBitmap(result?.toBitmap())
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
