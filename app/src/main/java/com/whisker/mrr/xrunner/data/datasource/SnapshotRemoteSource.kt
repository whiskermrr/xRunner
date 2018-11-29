package com.whisker.mrr.xrunner.data.datasource

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import io.reactivex.Completable
import java.io.File

class SnapshotRemoteSource(private val firebaseStorage: FirebaseStorage) {

    fun saveSnapshotRemote(filePath: String, fileName: String) : Completable {
        val fileUri = Uri.fromFile(File(filePath))
        val snapshotReference = firebaseStorage.reference.child("snapshots/$fileName")
        return Completable.create { emitter ->
            snapshotReference.putFile(fileUri).addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    emitter.onComplete()
                } else {
                    emitter.onError(task.exception ?: Throwable())
                }
            }.addOnFailureListener {
                emitter.onError(it)
            }
        }
    }
}