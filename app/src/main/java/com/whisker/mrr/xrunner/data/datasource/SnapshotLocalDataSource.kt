package com.whisker.mrr.xrunner.data.datasource

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import com.whisker.mrr.xrunner.domain.source.SnapshotLocalSource
import com.whisker.mrr.xrunner.utils.saveFile
import com.whisker.mrr.xrunner.utils.toByteArray
import com.whisker.mrr.xrunner.utils.XRunnerConstants
import javax.inject.Inject

class SnapshotLocalDataSource
@Inject constructor(private val context: Context, private val sharedPreferences: SharedPreferences) :
    SnapshotLocalSource {

    override fun saveSnapshotLocal(bitmap: Bitmap, fileName: String) : String {
        val byteArray = bitmap.toByteArray(Bitmap.CompressFormat.JPEG)
        context.saveFile( byteArray, fileName)

        val snapshotNames = sharedPreferences.getStringSet(XRunnerConstants.EXTRA_SNAPSHOT_NAMES_SET, mutableSetOf())
        snapshotNames!!.add(fileName)

        val editor = sharedPreferences.edit()
        editor.clear()
        editor.putStringSet(XRunnerConstants.EXTRA_SNAPSHOT_NAMES_SET, snapshotNames)
        editor.apply()

        return context.getFileStreamPath(fileName).absolutePath
    }

    override fun markSnapshotAsSent(fileName: String) {
        val snapshotNames = sharedPreferences.getStringSet(XRunnerConstants.EXTRA_SNAPSHOT_NAMES_SET, mutableSetOf())
        snapshotNames?.remove(fileName)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.putStringSet(XRunnerConstants.EXTRA_SNAPSHOT_NAMES_SET, snapshotNames)
        editor.apply()
    }
}