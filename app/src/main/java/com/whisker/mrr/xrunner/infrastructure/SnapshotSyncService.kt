package com.whisker.mrr.xrunner.infrastructure

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.google.firebase.storage.FirebaseStorage
import com.whisker.mrr.domain.bus.RxBus
import com.whisker.mrr.domain.bus.event.SyncEvent
import com.whisker.mrr.xrunner.utils.getFile
import com.whisker.mrr.xrunner.utils.XRunnerConstants
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SnapshotSyncService : Service() {

    companion object {
        const val ACTION_SYNC_SNAPSHOTS: String = "com.whisker.mrr.action.SYNC_SNAPSHOTS"
        const val CHANNEL_ID: String = "channel_01"
        const val NOTIFICATION_TITLE: String = "Just title"
    }

    private var disposables: CompositeDisposable = CompositeDisposable()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                NOTIFICATION_TITLE,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(channel)

            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("")
                .setContentText("")
                .build()

            startForeground(1, notification)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(intent?.action == ACTION_SYNC_SNAPSHOTS) {
            syncSnapshots()
        } else {
            stopSelf()
        }
        return START_NOT_STICKY
    }

    private fun syncSnapshots() {
        sharedPreferences = applicationContext.getSharedPreferences(XRunnerConstants.XRUNNER_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val snapshotNames = sharedPreferences.getStringSet(XRunnerConstants.EXTRA_SNAPSHOT_NAMES_SET, mutableSetOf())
        if(snapshotNames!!.isEmpty()) {
            stopSelf()
            return
        }

        RxBus.publish(SyncEvent(true))
        val firebaseStorage = FirebaseStorage.getInstance()
        val completableList = ArrayList<Completable>()

        for(fileName in snapshotNames) {
            completableList.add(
                Completable.create { emitter ->
                    val snapshotReference = firebaseStorage.reference.child("snapshots/$fileName")
                    val fileUri = Uri.fromFile(applicationContext.getFile(fileName))
                    snapshotReference.putFile(fileUri).addOnCompleteListener { task ->
                        if(task.isSuccessful) {
                            snapshotNames.remove(fileName)
                            emitter.onComplete()
                        } else {
                            emitter.onError(task.exception ?: Throwable())
                        }
                    }.addOnFailureListener {
                        emitter.onError(it)
                    }
                }.doOnComplete {
                    val editor = sharedPreferences.edit()
                    editor.clear()
                    editor.putStringSet(XRunnerConstants.EXTRA_SNAPSHOT_NAMES_SET, snapshotNames)
                    editor.apply()
                }
            )
        }

        disposables.add(
            Completable.concat(completableList)
                .subscribeOn(Schedulers.io())
                .subscribe( {
                    stopSelf()
                },
                {
                    it.printStackTrace()
                    stopSelf()
                })
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        RxBus.publish(SyncEvent(false))
        disposables.dispose()
    }
}