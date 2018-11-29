package com.whisker.mrr.xrunner.data.datasource

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import com.whisker.mrr.xrunner.utils.BitmapUtils
import com.whisker.mrr.xrunner.utils.FileUtils
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class SnapshotLocalSource
@Inject constructor(private val context: Context, private val sharedPreferences: SharedPreferences) {

    companion object {
        const val EXTRA_SNAPSHOT_PATHS_SET = "extra_snapshot_paths_set"
    }

    fun saveSnapshotLocal(bitmap: Bitmap, fileName: String) : String {
        val byteArray = BitmapUtils.convertBitmapToByteArray(bitmap, Bitmap.CompressFormat.JPEG)
        FileUtils.saveFile(context, byteArray, fileName)
        return context.getFileStreamPath(fileName).absolutePath
    }

    fun cacheSnapshotLocal(bitmap: Bitmap, fileName: String) {
        val byteArray = BitmapUtils.convertBitmapToByteArray(bitmap, Bitmap.CompressFormat.JPEG)
        FileUtils.saveFile(context, byteArray, fileName)

        val snapshotPaths = sharedPreferences.getStringSet(EXTRA_SNAPSHOT_PATHS_SET, mutableSetOf())
        snapshotPaths!!.add(context.getFileStreamPath(fileName).absolutePath)

        sharedPreferences.edit()
            .putStringSet(EXTRA_SNAPSHOT_PATHS_SET, snapshotPaths)
            .apply()
    }

    fun removeSnapshotFromLocal(fileName: String) {
        FileUtils.deleteFile(context.getFileStreamPath(fileName).absolutePath)
    }
}