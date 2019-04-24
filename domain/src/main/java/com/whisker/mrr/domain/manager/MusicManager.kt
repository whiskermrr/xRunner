package com.whisker.mrr.domain.manager

import com.whisker.mrr.domain.model.Song
import io.reactivex.Completable
import io.reactivex.Single

interface MusicManager {
    fun setSongs(songs: List<Song>) : Completable
    fun nextSong() : Single<Song>
    fun previousSong() : Single<Song>
    fun play() : Completable
    fun stop() : Completable
    fun pause() : Completable
    fun seekTo(time: Int) : Completable
}