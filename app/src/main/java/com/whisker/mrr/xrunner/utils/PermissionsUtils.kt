package com.whisker.mrr.xrunner.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.whisker.mrr.xrunner.R

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

    fun onRequestPermissionDenied(fragment: Fragment, permissions: Array<String>, rationaleMessage: String? = null) {
        var isShowRationale = false
        for(permission in permissions) {
            if(fragment.shouldShowRequestPermissionRationale(permission)) {
                isShowRationale = true
                break
            }
        }

        val message = rationaleMessage ?: R.string.permission_required_info.toString()

        showPermissionDialog(fragment, permissions, message, isShowRationale)
    }

    fun showPermissionDialog(fragment: Fragment, permissions: Array<String>, rationaleMessage: String, isShowRationale: Boolean) {
        val dialogBuilder = AlertDialog.Builder(fragment.requireContext())
        dialogBuilder.setMessage(rationaleMessage)
        dialogBuilder.setCancelable(true)

        if(isShowRationale) {
            dialogBuilder.setPositiveButton(R.string.button_ok) { _, _ ->
                okAction(fragment, permissions)
            }
        } else {
            dialogBuilder.setPositiveButton(R.string.button_settings) { _, _ ->
                settingsAction(fragment)
            }
        }
    }

    private fun okAction(fragment: Fragment, permissions: Array<String>) {
        if(fragment.isAdded) {
            fragment.requestPermissions(permissions, REQUEST_CODE)
        }
    }

    private fun settingsAction(fragment: Fragment) {
        val settingsIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", fragment.requireContext().packageName, null)
        settingsIntent.data = uri
        fragment.startActivityForResult(settingsIntent, 8938)
    }

    fun isPermissionGranted(fragment: Fragment, permission: String) : Boolean {
        return ContextCompat.checkSelfPermission(fragment.requireContext(), permission) == PackageManager.PERMISSION_GRANTED
    }
}