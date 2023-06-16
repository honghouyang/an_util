package com.hhy.util.sample.compress

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hhy.util.compress.Compressor
import com.hhy.util.compress.sizeLimit
import com.hhy.util.compress.toBitmap
import com.hhy.util.file.FileUri
import com.hhy.util.sample.Const
import com.hhy.util.sample.PermissionHelper
import com.hhy.util.sample.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.EasyPermissions
import java.io.File

@Suppress("Deprecation")
/** compress demo */
class CompressTestActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    companion object {
        const val TAG = "CompressTestActivity"
    }

    private lateinit var btnCompressing: Button
    private lateinit var ivOriginal1: ImageView
    private lateinit var ivCompressed1: ImageView
    private lateinit var ivOriginal2: ImageView
    private lateinit var ivCompressed2: ImageView
    private lateinit var ivOriginal3: ImageView
    private lateinit var ivCompressed3: ImageView

    private val uriTest1: Uri = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
        // version <= 10
        Uri.parse("${Const.androidLessThanOrEqualsTo10PrefixURI}test1.jpg")
    } else {
        // version > 10
        Uri.parse("${Const.greaterThan10PrefixURI}test1.jpg")
    }
    private val uriTest2: Uri = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
        // version <= 10
        Uri.parse("${Const.androidLessThanOrEqualsTo10PrefixURI}test2.jpg")
    } else {
        // version > 10
        Uri.parse("${Const.greaterThan10PrefixURI}test2.jpg")
    }
    private val uriTest3: Uri = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
        // version <= 10
        Uri.parse("${Const.androidLessThanOrEqualsTo10PrefixURI}test5.jpg")
    } else {
        // version > 10
        Uri.parse("${Const.greaterThan10PrefixURI}test5.jpg")
    }
    private var originalImageFile1: File? = null
    private var originalImageFile2: File? = null
    private var originalImageFile3: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compress)
        init()
        initOriginal()
    }

    private fun init() {
        ivOriginal1 = findViewById(R.id.iv_original1)
        ivCompressed1 = findViewById(R.id.iv_compressed1)
        ivOriginal2 = findViewById(R.id.iv_original2)
        ivCompressed2 = findViewById(R.id.iv_compressed2)
        ivOriginal3 = findViewById(R.id.iv_original3)
        ivCompressed3 = findViewById(R.id.iv_compressed3)
        btnCompressing = findViewById(R.id.btn_compressing)
        btnCompressing.setOnClickListener {
            doEvent()
        }
    }

    private fun initOriginal() {
        originalImageFile1 = FileUri.getFileByUri(this, uriTest1)
        ivOriginal1.setImageBitmap(originalImageFile1?.toBitmap())
        originalImageFile2 = FileUri.getFileByUri(this, uriTest2)
        ivOriginal2.setImageBitmap(originalImageFile2?.toBitmap())
        originalImageFile3 = FileUri.getFileByUri(this, uriTest3)
        ivOriginal3.setImageBitmap(originalImageFile3?.toBitmap())

        ivCompressed1.setImageURI(null)
        ivCompressed2.setImageURI(null)
        ivCompressed3.setImageURI(null)
    }

    private fun doEvent() {
        if (PermissionHelper.checkStoragePermission(this)) {
            val imageFile1 = FileUri.getFileByUri(this, uriTest1)
            val imageFile2 = FileUri.getFileByUri(this, uriTest2)
            val imageFile3 = FileUri.getFileByUri(this, uriTest3)
            val context = this
            CoroutineScope(Dispatchers.Main).launch {
                compressSync(imageFile1, context, ivCompressed1)
                compressSync(imageFile2, context, ivCompressed2)
                compressSync(imageFile3, context, ivCompressed3)
            }
        } else {
            PermissionHelper.requestStoragePermission(this)
        }
    }

    private suspend fun compressSync(imageFile: File?, context: Context, imageView: ImageView) {
        val format = Bitmap.CompressFormat.WEBP
        val maxSize = 102400L
        val step = 10
        val bitmapConfig = Bitmap.Config.RGB_565
        val fileName = "test_size_limit_sync"
        flow {
            val result = Compressor.compressFormFile(
                context = context,
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
            emit(result)
        }.flowOn(Dispatchers.IO).onStart {
            Log.d(TAG, "Compress-onStart")
        }.onEach { file ->
            Log.d(TAG, "Compress-success")
            imageView.setImageBitmap(file?.toBitmap())
        }.onCompletion {
            Log.d(TAG, "Compress-onCompletion")
        }.collect()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        @Suppress("DEPRECATION")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults,
            this
        )
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        println("onPermissionsGranted ${perms.size}")
        if (perms.size == 2 &&
            perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
            perms.contains(Manifest.permission.READ_EXTERNAL_STORAGE)
        ) {
            doEvent()
        } else {
            Toast.makeText(this, "请申请权限", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        // onPermissionsDenied
    }
}
