package com.whisker.mrr.domain.manager

import com.whisker.mrr.domain.model.Song
import io.reactivex.Completable
import io.reactivex.Single

interface MusicManager {
    fun setSongs(songs: List<Song>) : Completable
    fun nextSong() : Single<Song>
    fun previousSong() : Single<Song>
    fun currentSong() : Single<Song>
    fun start()
    fun stop()
    fun pause()
    fun seekTo(time: Int)
}