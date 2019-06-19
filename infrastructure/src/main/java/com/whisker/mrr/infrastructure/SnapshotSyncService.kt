package com.whisker.mrr.infrastructure

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.whisker.mrr.domain.common.bus.RxBus
import com.whisker.mrr.domain.common.bus.event.SyncEvent
import com.whisker.mrr.data.source.SnapshotLocalSource
import com.whisker.mrr.data.source.SnapshotRemoteSource
import dagger.android.AndroidInjection
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SnapshotSyncService : Service() {

    companion object {
        const val ACTION_SYNC_SNAPSHOTS: String = "com.whisker.mrr.action.SYNC_SNAPSHOTS"
        const val CHANNEL_ID: String = "channel_01"
        const val NOTIFICATION_TITLE: String = "Just title"
    }

    private var disposables: CompositeDisposable = CompositeDisposable()
    @Inject lateinit var snapshotLocalSource: SnapshotLocalSource
    @Inject lateinit var snapshotRemoteSource: SnapshotRemoteSource

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                NOTIFICATION_TITLE,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(channel)

            val notification = NotificationCompat.Builder(this,
                CHANNEL_ID
            )
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
        val snapshots = snapshotLocalSource.getNotSentSnapshotsPaths()
        if(snapshots.isEmpty()) {
            stopSelf()
            return
        }

        RxBus.publish(SyncEvent(true))
        val completableList = snapshotRemoteSource.saveListOfSnapshotsRemote(snapshots.toMutableList())

        disposables.add(
            Completable.concat(completableList)
                .subscribeOn(Schedulers.io())
                .subscribe( {
                    snapshotLocalSource.replaceNotSentSnapshotsPaths(snapshots.map { it.second }.toSet())
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