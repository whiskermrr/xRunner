package com.whisker.mrr.xrunner.data.datasource

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.whisker.mrr.xrunner.data.datasource.common.DataConstants.REFERENCE_SNAPSHOT
import com.whisker.mrr.xrunner.domain.source.SnapshotRemoteSource
import io.reactivex.Completable
import java.io.File

class SnapshotRemoteDataSource(private val firebaseStorage: FirebaseStorage) : SnapshotRemoteSource {

    override fun saveSnapshotRemote(filePath: String, fileName: String) : Completable {
        val fileUri = Uri.fromFile(File(filePath))
        val snapshotReference = firebaseStorage.reference.child(REFERENCE_SNAPSHOT + fileName)
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