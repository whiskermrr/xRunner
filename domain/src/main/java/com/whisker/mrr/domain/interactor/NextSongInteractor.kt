package com.whisker.mrr.domain.interactor

import com.whisker.mrr.domain.manager.MusicManager
import com.whisker.mrr.domain.model.Song
import com.whisker.mrr.domain.usecase.SingleUseCase
import io.reactivex.Single
import io.reactivex.SingleTransformer

class NextSongInteractor(
    transformer: SingleTransformer<Song, Song>,
    private val musicManager: MusicManager
) : SingleUseCase<Song>(transformer) {

    fun nextSong() : Single<Song> {
        return single()
    }

    override fun createSingle(data: Map<String, Any>?): Single<Song> {
        return musicManager.nextSong()
    }
}