package com.whisker.mrr.xrunner.infrastructure.source

import android.content.Context
import android.content.SharedPreferences
import com.whisker.mrr.domain.source.SnapshotLocalSource
import com.whisker.mrr.xrunner.utils.saveFile
import com.whisker.mrr.xrunner.utils.XRunnerConstants
import javax.inject.Inject

class SnapshotLocalDataSource
@Inject constructor(private val context: Context, private val sharedPreferences: SharedPreferences) :
    SnapshotLocalSource {

    override fun saveSnapshotLocal(bitmap: ByteArray, fileName: String) : String {
        context.saveFile( bitmap, fileName)

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