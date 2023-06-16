package com.hhy.util.photo

import android.util.Log
import org.junit.Test

private const val TAG = "PhotoHelperTest"

class PhotoHelperTest {
    @Test
    fun constTest() {
        PhotoHelper.PHOTO_CAMERA
        PhotoHelper.PHOTO_ALBUM
        PhotoHelper.PHOTO_CAMERA_ERROR
    }

    @Test
    fun isAndroidQ() {
        val isAndroid = PhotoHelper.isAndroidQ()
        Log.i(TAG, "isAndroidQ $isAndroid")
    }

    @Test
    fun checkForSdcard() {
        val checkForSdcard = PhotoHelper.checkForSdcard()
        Log.i(TAG, "checkForSdcard $checkForSdcard")
    }
}
