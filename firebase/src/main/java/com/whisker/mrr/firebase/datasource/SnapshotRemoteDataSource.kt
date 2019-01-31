package com.whisker.mrr.firebase.datasource

import com.google.firebase.storage.FirebaseStorage
import com.whisker.mrr.firebase.common.DataConstants.REFERENCE_SNAPSHOT
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

    override fun saveListOfSnapshotsRemote(files: MutableList<Pair<String, String>>): List<Completable> {
        val completableList = ArrayList<Completable>()

        for(file in files) {
            completableList.add(
                Completable.create { emitter ->
                    val snapshotReference = firebaseStorage.reference.child("snapshots/${file.first}")
                    val fileStream = FileInputStream(File(file.second))
                    snapshotReference.putStream(fileStream).addOnCompleteListener { task ->
                        if(task.isSuccessful) {
                            files.remove(file)
                            emitter.onComplete()
                        } else {
                            emitter.onError(task.exception ?: Throwable())
                        }
                    }.addOnFailureListener {
                        emitter.onError(it)
                    }
                }
            )
        }

        return completableList
    }
}