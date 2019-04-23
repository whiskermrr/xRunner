package com.whisker.mrr.music

import com.whisker.mrr.domain.manager.MusicManager
import com.whisker.mrr.domain.model.Song
import io.reactivex.Completable
import io.reactivex.Single

class MusicDataManager : MusicManager {

    override fun setSongs(songs: List<Song>) : Completable {
        return Completable.complete()
    }

    override fun currentSong(): Single<Song> {
        return Single.error(Throwable(""))
    }

    override fun nextSong() : Single<Song> {
        return Single.error(Throwable(""))
    }

    override fun previousSong() : Single<Song> {
        return Single.error(Throwable(""))
    }

    override fun start() {
    }

    override fun stop() {
    }

    override fun pause() {
    }

    override fun seekTo(time: Int) {
    }
}