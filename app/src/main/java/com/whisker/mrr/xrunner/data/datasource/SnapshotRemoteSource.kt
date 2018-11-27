package com.whisker.mrr.xrunner.data.datasource

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Completable

class SnapshotRemoteSource(private val firestore: FirebaseFirestore) {

    fun saveSnapshotRemote(filePath: String) : Completable {
        Log.e("SNAPSHOT REMOTE SOURCE", filePath)
        return Completable.complete()
    }
}