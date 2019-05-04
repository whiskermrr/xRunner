package com.whisker.mrr.music

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.IBinder
import com.whisker.mrr.domain.manager.MusicManager
import com.whisker.mrr.domain.model.Song
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject

class MusicDataManager(private val context: Context) : MusicManager, MediaPlayer.OnCompletionListener {

    private val currentSongSubject: PublishSubject<Song> = PublishSubject.create()
    private lateinit var musicService: MusicService
    private var isServiceBounded = false
    private lateinit var songs: List<Song>
    private var currentPlayerPosition = -1

    private lateinit var serviceConnection: ServiceConnection

    override fun setSongs(songs: List<Song>) : Completable {
        this.songs = songs
        currentPlayerPosition = 0
        if(this.songs.isNotEmpty()) {
            currentSongSubject.onNext(songs[0])
        }
        return Completable.complete()
    }

    private fun initMusicService() {
        serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val binder = service as MusicService.MusicBinder
                musicService = binder.getService()
                musicService.setOnCompletionListener(this@MusicDataManager)
                musicService.play()
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

    override fun nextSong() : Completable {
        return Completable.fromAction {
            val song = getNextSong()
            currentSongSubject.onNext(song)
            musicService.playSong(song)
        }
    }

    override fun previousSong() : Completable {
        return Completable.fromAction {
            val song = getPreviousSong()
            currentSongSubject.onNext(song)
            musicService.playSong(song)
        }
    }

    override fun play() : Completable {
        when {
            isServiceBounded -> return Completable.fromAction { musicService.play() }
            ::songs.isInitialized -> initMusicService()
            else -> return Completable.error(UninitializedPropertyAccessException("Songs are not initialized."))
        }
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
        return Completable.fromAction {
            musicService.pause()
        }
    }

    override fun seekTo(time: Int) : Completable {
        musicService.seekTo(time)
        return Completable.complete()
    }

    override fun currentSong(): Flowable<Song> {
        return currentSongSubject.toFlowable(BackpressureStrategy.LATEST)
    }

    private fun getNextSong() : Song {
        if(currentPlayerPosition < songs.size - 1) {
            currentPlayerPosition++
        } else {
            currentPlayerPosition = 0
        }
        return songs[currentPlayerPosition]
    }

    private fun getPreviousSong() : Song {
        if(currentPlayerPosition == 0) {
            currentPlayerPosition = songs.size - 1
        } else {
            currentPlayerPosition++
        }
        return songs[currentPlayerPosition]
    }

    override fun onCompletion(mp: MediaPlayer?) {
        val song = getNextSong()
        currentSongSubject.onNext(song)
        musicService.playSong(song)
    }
}