package com.whisker.mrr.xrunner.infrastructure

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.whisker.mrr.xrunner.utils.FileUtils
import com.whisker.mrr.xrunner.utils.xRunnerConstants.EXTRA_SNAPSHOT_NAMES_SET
import com.whisker.mrr.xrunner.utils.xRunnerConstants.XRUNNER_SHARED_PREFERENCES
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable

class SnapshotSyncService : IntentService(SnapshotSyncService::class.java.simpleName) {

    private var sharedPreferences: SharedPreferences =
        applicationContext.getSharedPreferences(XRUNNER_SHARED_PREFERENCES, Context.MODE_PRIVATE)

    private var disposables: CompositeDisposable = CompositeDisposable()



    override fun onHandleIntent(intent: Intent) {
        val snapshotNames = sharedPreferences.getStringSet(EXTRA_SNAPSHOT_NAMES_SET, mutableSetOf())
        val firebaseStorage = FirebaseStorage.getInstance()

        val completableList = ArrayList<Completable>()

        for(fileName in snapshotNames!!) {
            completableList.add(
                Completable.create { emitter ->
                    val snapshotReference = firebaseStorage.reference.child("snapshots/$fileName")
                    val fileUri = Uri.fromFile(FileUtils.getFile(applicationContext, fileName))
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
            )
        }

        disposables.add(
            Completable.concat(completableList)
                .subscribe{
                    // TODO: eventbus sync finished
                }
        )
    }
}