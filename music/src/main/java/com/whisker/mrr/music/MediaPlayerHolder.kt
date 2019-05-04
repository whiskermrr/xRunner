package com.whisker.mrr.music

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import com.whisker.mrr.domain.model.Song

class MediaPlayerHolder(private val context: Context) {


    private var mediaPlayer: MediaPlayer? = null
    private var listener: MediaPlayer.OnCompletionListener? = null

    fun initializeMediaPlayer() {
        if(mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
        } else {
            mediaPlayer?.reset()
        }
    }

    fun setOnCompletionListener(listener: MediaPlayer.OnCompletionListener) {
        this.listener = listener
    }

    fun playSong(song: Song) {
        mediaPlayer?.apply {
            reset()
            setDataSource(context, Uri.parse(song.data))
            setOnPreparedListener { player ->
                player.start()
            }
            setOnCompletionListener(listener)
            prepareAsync()
        }
    }

    fun play() {
        mediaPlayer?.apply {
            if(!isPlaying) {
                start()
            }
        }
    }

    fun pause() {
        mediaPlayer?.apply {
            if(isPlaying) {
                pause()
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