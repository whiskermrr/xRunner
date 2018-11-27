package com.whisker.mrr.xrunner.data.datasource

import android.content.Context
import android.graphics.Bitmap
import com.whisker.mrr.xrunner.utils.BitmapUtils
import com.whisker.mrr.xrunner.utils.FileUtils
import io.reactivex.Single
import javax.inject.Inject

class SnapshotLocalSource @Inject constructor(private val context: Context) {

    fun saveSnapshotLocal(bitmap: Bitmap, fileName: String) : Single<String> {
        val byteArray = BitmapUtils.convertBitmapToByteArray(bitmap, Bitmap.CompressFormat.JPEG)
        FileUtils.saveFile(context, byteArray, fileName)
        return Single.just(context.getFileStreamPath(fileName).absolutePath)
    }

    fun removeSnapshotFromLocal(fileName: String) {
        FileUtils.deleteFile(context.getFileStreamPath(fileName).absolutePath)
    }
}