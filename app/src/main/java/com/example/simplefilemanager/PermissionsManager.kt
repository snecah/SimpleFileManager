package com.example.simplefilemanager

import android.app.Activity
import android.content.Context
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker


class PermissionsManager {
    fun checkSelfPermission(context: Context, permission: String): Int {
        return PermissionChecker.checkSelfPermission(context, permission)
    }

    fun requestPermissions(
        activity: Activity?, permissions: Array<String?>, requestCode: Int
    ) {
        if (activity == null) {
            return
        }
        ActivityCompat.requestPermissions(activity, permissions, requestCode)
    }
}