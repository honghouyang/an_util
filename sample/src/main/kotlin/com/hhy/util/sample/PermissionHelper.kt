package com.hhy.util.sample

import android.Manifest
import android.app.Activity
import android.content.Context
import com.hhy.util.appctx.appCtx
import pub.devrel.easypermissions.EasyPermissions

object PermissionHelper {
    private const val REQUEST_STORAGE_PERMISSION_CODE = 0x0000002

    /** Check for storage permission. */
    fun checkStoragePermission(context: Context): Boolean {
        return EasyPermissions.hasPermissions(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    /** Apply for storage permission. */
    fun requestStoragePermission(activity: Activity) {
        EasyPermissions.requestPermissions(
            activity,
            appCtx.getString(R.string.authentication_page_permission_tip),
            REQUEST_STORAGE_PERMISSION_CODE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }
}
