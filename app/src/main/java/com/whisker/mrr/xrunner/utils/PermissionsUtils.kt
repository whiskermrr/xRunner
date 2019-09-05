package com.whisker.mrr.xrunner.utils

import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

object PermissionsUtils {

    val REQUEST_CODE: Int = 1001

    fun checkPermission(fragment: Fragment, permission: String) : Boolean {
        return checkPermission(fragment, permission, REQUEST_CODE)
    }

    fun checkPermission(fragment: Fragment, permission: String, requestCode: Int) : Boolean {
        if(!isPermissionGranted(fragment, permission)) {
            if(fragment.shouldShowRequestPermissionRationale(permission)) {

            }
            fragment.requestPermissions(arrayOf(permission), requestCode)
        } else {
            return true
        }
        return false
    }

    fun checkPermissions(fragment: Fragment, permissions: Array<String>) : Boolean {
        return checkPermissions(fragment, permissions, REQUEST_CODE)
    }

    fun checkPermissions(fragment: Fragment, permissions: Array<String>, requestCode: Int) : Boolean {
        val notGranted: MutableList<String> = mutableListOf()
        for(permission in permissions) {
            if(!isPermissionGranted(fragment, permission)) {
                notGranted.add(permission)
            }
        }
        if(notGranted.isNotEmpty()) {
            fragment.requestPermissions(notGranted.toTypedArray(), requestCode)
        } else {
            return true
        }
        return false
    }

    private fun isPermissionGranted(fragment: Fragment, permission: String) : Boolean {
        return ContextCompat.checkSelfPermission(fragment.requireContext(), permission) == PackageManager.PERMISSION_GRANTED
    }
}