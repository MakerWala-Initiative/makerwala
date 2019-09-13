package com.uvtech.makerwala.helpers

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity

class PermissionHelper {

    private var activity: AppCompatActivity? = null

    companion object {
        private var permissionHelper: PermissionHelper? = null

        fun getInstance(appCompatActivity: AppCompatActivity): PermissionHelper {
            if (permissionHelper == null)
                permissionHelper = PermissionHelper(appCompatActivity)
            return permissionHelper!!
        }

        private const val REQUEST_CODE = 1000
    }

    constructor(activity: AppCompatActivity) {
        this.activity = activity
    }

    // return true this permission is granted,
    // otherwise false
    fun checkPermission(permission: String): Boolean {
        if (ContextCompat.checkSelfPermission(activity as Context, permission)
                == PackageManager.PERMISSION_GRANTED) {
            return true
        } else {
            ActivityCompat.requestPermissions(activity!!,
                    arrayOf(permission), REQUEST_CODE)
            return false
        }
    }

    fun checkPermissions(permission: Array<String>): Boolean {
        var result = 0

        for (i in 0 until permission.size) {
            if (ContextCompat.checkSelfPermission(activity as Context, permission[i]) == PackageManager.PERMISSION_GRANTED) {
                result++
            }
        }

        return if (permission.size == result) {
            true
        } else {
            ActivityCompat.requestPermissions(activity!!,
                    permission, REQUEST_CODE)
            false
        }
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray,
                                   onPermissionHelper: OnPermissionHelper) {
        when (requestCode) {
            REQUEST_CODE -> {
                var result = 0

                for (i in 0 until grantResults.size) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        result++
                    }
                }

                if (grantResults.size == result) {
                    onPermissionHelper.permissionApprove()
                }
            }
        }
    }

    interface OnPermissionHelper {
        fun permissionApprove()
    }
}