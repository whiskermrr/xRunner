package com.whisker.mrr.music

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.whisker.mrr.domain.manager.MusicManager
import com.whisker.mrr.domain.model.Song
import io.reactivex.Completable
import io.reactivex.Single

class MusicDataManager(private val context: Context) : MusicManager {

    private lateinit var musicService: MusicService
    private var isServiceBounded = false

    private lateinit var serviceConnection: ServiceConnection

    override fun setSongs(songs: List<Song>) : Completable {
        if(isServiceBounded) {
            musicService.setSongs(songs)
        } else {
            initMusicService(songs)
        }
        return Completable.complete()
    }

    private fun initMusicService(songs: List<Song>) {
        serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val binder = service as MusicService.MusicBinder
                musicService = binder.getService()
                musicService.setSongs(songs)
                isServiceBounded = true
            }
            override fun onServiceDisconnected(name: ComponentName?) {
                isServiceBounded = false
            }
        }
        Intent(context, MusicService::class.java).also { intent ->
            context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun nextSong() : Single<Song> {
        return musicService.nextSong()
    }

    override fun previousSong() : Single<Song> {
        return musicService.previousSong()
    }

    override fun play() : Completable {
        musicService.play()
        return Completable.complete()
    }

    override fun stop() : Completable {
        if(isServiceBounded) {
            context.unbindService(serviceConnection)
            isServiceBounded = false
        }
        return Completable.complete()
    }

    override fun pause() : Completable {
        musicService.pause()
        return Completable.complete()
    }

    override fun seekTo(time: Int) : Completable {
        musicService.seekTo(time)
        return Completable.complete()
    }
}