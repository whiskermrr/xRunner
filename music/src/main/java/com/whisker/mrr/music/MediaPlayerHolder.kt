package com.whisker.mrr.music

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import com.whisker.mrr.domain.model.Song
import io.reactivex.Single

class MediaPlayerHolder(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var songs: List<Song>
    private var currentPlayerPosition = -1

    fun initializeMediaPlayer() {
        if(mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
        } else {
            mediaPlayer?.reset()
        }
    }

    fun setPlaylist(songs: List<Song>) {
        this.songs = songs
        currentPlayerPosition = 0
    }

    private fun playSong(song: Song) {
        mediaPlayer?.apply {
            reset()
            setDataSource(context, Uri.parse(song.data))
            setOnPreparedListener { player ->
                player.start()
            }
            prepareAsync()
        }
    }

    fun nextSong() : Single<Song> {
        if(currentPlayerPosition < songs.size) {
            currentPlayerPosition++
        } else {
            currentPlayerPosition = 0
        }
        val song = songs[currentPlayerPosition]
        playSong(song)
        return Single.just(song)
    }

    fun previousSong() : Single<Song> {
        if(currentPlayerPosition == 0) {
            currentPlayerPosition = songs.size - 1
        } else {
            currentPlayerPosition--
        }
        val song = songs[currentPlayerPosition]
        playSong(song)
        return Single.just(song)
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