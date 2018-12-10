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

        val snapshotNames = sharedPreferences.getStringSet(xRunnerConstants.EXTRA_SNAPSHOT_NAMES_SET, mutableSetOf())
        snapshotNames!!.add(fileName)

        val editor = sharedPreferences.edit()
        editor.clear()
        editor.putStringSet(xRunnerConstants.EXTRA_SNAPSHOT_NAMES_SET, snapshotNames)
        editor.apply()

        return context.getFileStreamPath(fileName).absolutePath
    }

    fun markSnapshotAsSent(fileName: String) {
        val snapshotNames = sharedPreferences.getStringSet(xRunnerConstants.EXTRA_SNAPSHOT_NAMES_SET, mutableSetOf())
        snapshotNames?.remove(fileName)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.putStringSet(xRunnerConstants.EXTRA_SNAPSHOT_NAMES_SET, snapshotNames)
        editor.apply()
    }
}