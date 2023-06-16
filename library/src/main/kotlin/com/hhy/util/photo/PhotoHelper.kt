package com.hhy.util.photo

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.os.EnvironmentCompat
import androidx.fragment.app.Fragment
import com.hhy.util.appctx.appCtx
import com.hhy.util.file.FileUri
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

@Suppress("Deprecation")
object PhotoHelper {
    const val PHOTO_CAMERA = 1001 // camera
    const val PHOTO_ALBUM = 1002 // album
    const val PHOTO_CAMERA_ERROR = 1003 // error

    // 用于保存拍照图片的uri（手动指定了uri之后，data就会为空）
    var mCameraResultUri: Uri? = null

    // 是否是Android 10以上手机
    fun isAndroidQ(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }

    /** Check for Sdcard. */
    fun checkForSdcard(): Boolean {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
    }

    /**
     * Open system album.
     * Permissions required [WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE]
     * */
    fun openAlbum(fragment: Fragment) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        fragment.startActivityForResult(intent, PHOTO_ALBUM)
    }

    /**
     * Open system camera.
     * Permissions required [CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE]
     * */
    fun openCamera(fragment: Fragment) {
        val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        when {
            captureIntent.resolveActivity(appCtx.packageManager) != null -> {
                mCameraResultUri =
                    createCameraUri(fragment.requireActivity(), createCameraOriginalImage())
                when {
                    mCameraResultUri != null -> {
                        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraResultUri)
                        captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                        fragment.startActivityForResult(captureIntent, PHOTO_CAMERA)
                    }
                    else -> {
                        fragment.startActivityForResult(captureIntent, PHOTO_CAMERA_ERROR)
                    }
                }
            }
            else -> {
                fragment.startActivityForResult(captureIntent, PHOTO_CAMERA_ERROR)
            }
        }
    }

    /**
     * create camera uri
     * Save the photo after taking the photo
     * */
    private fun createCameraUri(context: Context, file: File?): Uri? {
        return if (isAndroidQ()) {
            when {
                checkForSdcard() -> {
                    appCtx.contentResolver.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        ContentValues()
                    )
                }
                else -> {
                    appCtx.contentResolver.insert(
                        MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                        ContentValues()
                    )
                }
            }
        } else {
            FileUri.getUriByFile(context, file)
        }
    }

    /** create camera original image */
    private fun createCameraOriginalImage(): File? {
        val format = "yyyyMMdd_HH_mm_ss"
        val timeStamp = SimpleDateFormat(format).format(Date())
        val imageName: String = "CAMERA_" + timeStamp + "_"
        // getExternalFilesDir(Environment.DIRECTORY_PICTURES): /storage/emulated/0/Android/data/com.xxx.xxx/app_util_ext_pub/Pictures
        val storageDir: File? = appCtx.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (storageDir?.exists() == false) storageDir.mkdir()
        val tempFile = File.createTempFile(imageName, ".jpg", storageDir)
        return when {
            Environment.MEDIA_MOUNTED != EnvironmentCompat.getStorageState(tempFile) -> {
                null
            }
            else -> tempFile
        }
    }
}
