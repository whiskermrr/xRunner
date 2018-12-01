package com.whisker.mrr.xrunner.data.datasource

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import com.whisker.mrr.xrunner.utils.BitmapUtils
import com.whisker.mrr.xrunner.utils.FileUtils
import com.whisker.mrr.xrunner.utils.xRunnerConstants
import javax.inject.Inject

class SnapshotLocalSource
@Inject constructor(private val context: Context, private val sharedPreferences: SharedPreferences) {

    fun saveSnapshotLocal(bitmap: Bitmap, fileName: String) : String {
        val byteArray = BitmapUtils.convertBitmapToByteArray(bitmap, Bitmap.CompressFormat.JPEG)
        FileUtils.saveFile(context, byteArray, fileName)
        return context.getFileStreamPath(fileName).absolutePath
    }

    fun cacheSnapshotLocal(bitmap: Bitmap, fileName: String) {
        val byteArray = BitmapUtils.convertBitmapToByteArray(bitmap, Bitmap.CompressFormat.JPEG)
        FileUtils.saveFile(context, byteArray, fileName)

        val snapshotNames = sharedPreferences.getStringSet(xRunnerConstants.EXTRA_SNAPSHOT_NAMES_SET, mutableSetOf())
        snapshotNames!!.add(fileName)

        val editor = sharedPreferences.edit()
        editor.putStringSet(xRunnerConstants.EXTRA_SNAPSHOT_NAMES_SET, snapshotNames)
        editor.apply()
    }

    fun removeSnapshotFromLocal(fileName: String) {
        val filePath = context.getFileStreamPath(fileName).absolutePath
        FileUtils.deleteFile(filePath)
    }
}