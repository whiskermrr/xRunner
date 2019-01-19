package com.whisker.mrr.xrunner.data.datasource

import com.google.firebase.storage.FirebaseStorage
import com.whisker.mrr.xrunner.data.common.DataConstants.REFERENCE_SNAPSHOT
import com.whisker.mrr.domain.source.SnapshotRemoteSource
import io.reactivex.Completable
import java.io.File
import java.io.FileInputStream

class SnapshotRemoteDataSource(private val firebaseStorage: FirebaseStorage) : SnapshotRemoteSource {

    override fun saveSnapshotRemote(filePath: String, fileName: String) : Completable {
        val stream = FileInputStream(File(filePath))
        val snapshotReference = firebaseStorage.reference.child(REFERENCE_SNAPSHOT + fileName)
        return Completable.create { emitter ->
            snapshotReference.putStream(stream).addOnCompleteListener { task ->
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