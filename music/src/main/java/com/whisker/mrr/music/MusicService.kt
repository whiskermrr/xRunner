package com.whisker.mrr.music

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.whisker.mrr.domain.model.Song

class MusicService : Service() {

    companion object {
        const val CHANNEL_ID: String = "channel_01"
        const val NOTIFICATION_TITLE: String = "Readable title"
    }

    private lateinit var mediaPlayerHandler: MediaPlayerHolder
    private val binder = MusicBinder()

    override fun onBind(intent: Intent?): IBinder? {
        return binder
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

            val notification = NotificationCompat.Builder(this,
                CHANNEL_ID
            )
            .setContentTitle("")
            .setContentText("")
            .build()

            startForeground(2, notification)
        }
        mediaPlayerHandler = MediaPlayerHolder(applicationContext)
        mediaPlayerHandler.initializeMediaPlayer()
    }

    fun playSong(song: Song) {
        mediaPlayerHandler.playSong(song)
    }

    fun play() {
        mediaPlayerHandler.play()
    }

    fun pause() {
        mediaPlayerHandler.pause()
    }

    fun seekTo(position: Int) {
        mediaPlayerHandler.seekTo(position)
    }

    fun setOnCompletionListener(listener: MediaPlayer.OnCompletionListener) {
        mediaPlayerHandler.setOnCompletionListener(listener)
    }

    inner class MusicBinder : Binder() {
        fun getService() : MusicService = this@MusicService
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayerHandler.stop()
    }
}