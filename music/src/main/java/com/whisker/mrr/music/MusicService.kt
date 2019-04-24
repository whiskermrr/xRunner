package com.whisker.mrr.music

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder

class MusicService : Service() {

    private var musicPlayer: MediaPlayer? = null
    private val binder = MusicBinder()

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        musicPlayer = MediaPlayer()

    }

    inner class MusicBinder : Binder() {
        fun getService() : MusicService = this@MusicService
    }

    override fun onDestroy() {
        super.onDestroy()
        musicPlayer?.release()
        musicPlayer = null
    }
}