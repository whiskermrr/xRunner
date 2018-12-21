package com.whisker.mrr.xrunner.domain.source

import android.graphics.Bitmap

interface SnapshotLocalSource {

    fun saveSnapshotLocal(bitmap: Bitmap, fileName: String) : String
    fun markSnapshotAsSent(fileName: String)
}