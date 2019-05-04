package com.whisker.mrr.domain.manager

import com.whisker.mrr.domain.model.Song
import io.reactivex.Completable
import io.reactivex.Flowable

interface MusicManager {
    fun setSongs(songs: List<Song>) : Completable
    fun nextSong() : Completable
    fun previousSong() : Completable
    fun play() : Completable
    fun stop() : Completable
    fun pause() : Completable
    fun seekTo(time: Int) : Completable
    fun currentSong() : Flowable<Song>
}