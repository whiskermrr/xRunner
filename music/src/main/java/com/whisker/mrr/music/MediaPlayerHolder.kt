package com.whisker.mrr.music

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import com.whisker.mrr.domain.model.Song
import io.reactivex.Completable

class MediaPlayerHolder(private val context: Context) {


    private var mediaPlayer: MediaPlayer? = null

    fun initializeMediaPlayer() {
        if(mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
        } else {
            mediaPlayer?.reset()
        }
    }

    fun playSong(song: Song) : Completable {
        return Completable.fromAction {
            mediaPlayer?.apply {
                reset()
                setDataSource(context, Uri.parse(song.data))
                setOnPreparedListener { player ->
                    player.start()
                }
                prepareAsync()
            }
        }
    }

    fun play() : Completable {
        return Completable.fromAction {
            mediaPlayer?.apply {
                if(!isPlaying) {
                    start()
                }
            }
        }
    }

    fun pause() : Completable {
        return Completable.fromAction {
            mediaPlayer?.apply {
                if(isPlaying) {
                    pause()
                }
            }
        }
    }

    fun seekTo(position: Int) {
        mediaPlayer.apply {
            seekTo(position)
        }
    }

    fun stop() {
        mediaPlayer?.apply {
            if(isPlaying) {
                pause()
            }
        }
        mediaPlayer?.release()
        mediaPlayer = null
    }
}