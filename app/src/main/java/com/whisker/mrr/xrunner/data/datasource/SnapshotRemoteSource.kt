package com.whisker.mrr.xrunner.data.datasource

import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import io.reactivex.Completable

class SnapshotRemoteSource(private val firebaseStorage: FirebaseStorage) {

    fun saveSnapshotRemote(filePath: String) : Completable {
        Log.e("SNAPSHOT REMOTE SOURCE", filePath)
        return Completable.complete()
    }
}