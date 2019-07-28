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
import io.reactivex.subjects.BehaviorSubject

class MusicDataManager(private val context: Context) : MusicManager, MediaPlayer.OnCompletionListener {

    private val currentSongSubject: BehaviorSubject<Song> = BehaviorSubject.create()
    private val isMusicPlayingSubject: BehaviorSubject<Boolean> = BehaviorSubject.create()
    private lateinit var musicService: MusicService
    private var isServiceBounded = false
    private lateinit var songs: List<Song>
    private var currentPlayerPosition = -1

    private lateinit var serviceConnection: ServiceConnection

    override fun setSongs(songs: List<Song>, isStartPlaying: Boolean, currentPosition: Int) : Completable {
        return Completable.fromAction {
            this.songs = songs
            currentPlayerPosition = currentPosition
            if(this.songs.isNotEmpty()) {
                currentSongSubject.onNext(songs[currentPosition])
            }
        }.andThen(
            if(isStartPlaying) {
                if(isServiceBounded) {
                    Completable.fromAction { playSong(this.songs[currentPosition]) }
                } else {
                    play()
                }
            } else {
                Completable.fromAction { stop() }
            }
        )
    }

    private fun initMusicService() : Completable {
        return Completable.create { emitter ->
            serviceConnection = object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                    val binder = service as MusicService.MusicBinder
                    musicService = binder.getService()
                    musicService.setOnCompletionListener(this@MusicDataManager)
                    musicService.playSong(songs[currentPlayerPosition])
                    isServiceBounded = true
                    emitter.onComplete()
                }
                override fun onServiceDisconnected(name: ComponentName?) {
                    isServiceBounded = false
                }
            }
            Intent(context, MusicService::class.java).also { intent ->
                context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
            }
        }
    }

    override fun nextSong() : Completable {
        return Completable.fromAction {
            val song = getNextSong()
            playSong(song)
        }
    }

    override fun previousSong() : Completable {
        return Completable.fromAction {
            val song = getPreviousSong()
            playSong(song)
        }
    }

    private fun playSong(song: Song) {
        currentSongSubject.onNext(song)
        musicService.playSong(song)
        isMusicPlayingSubject.onNext(true)
    }

    override fun play() : Completable {
        isMusicPlayingSubject.onNext(true)
        return when {
            isServiceBounded -> Completable.fromAction { musicService.play() }
            ::songs.isInitialized -> initMusicService()
            else -> Completable.error(UninitializedPropertyAccessException("Songs are not initialized."))
        }
    }

    override fun stop() {
        if(isServiceBounded) {
            context.unbindService(serviceConnection)
            isMusicPlayingSubject.onNext(false)
            isServiceBounded = false
        }
    }

    override fun pause() : Completable {
        return Completable.fromAction {
            musicService.pause()
            isMusicPlayingSubject.onNext(false)
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
            currentPlayerPosition--
        }
        return songs[currentPlayerPosition]
    }

    override fun isMusicPlaying(): Flowable<Boolean> {
        return isMusicPlayingSubject.toFlowable(BackpressureStrategy.LATEST)
    }

    override fun onCompletion(mp: MediaPlayer?) {
        val song = getNextSong()
        playSong(song)
    }
}