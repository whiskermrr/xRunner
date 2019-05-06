package com.whisker.mrr.domain.interactor

import com.whisker.mrr.domain.manager.MusicManager
import com.whisker.mrr.domain.model.Song
import com.whisker.mrr.domain.usecase.FlowableUseCase
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer

class GetCurrentSongInteractor(
    transformer: FlowableTransformer<Song, Song>,
    private val musicManager: MusicManager
) : FlowableUseCase<Song>(transformer) {

    fun getCurrentSong() : Flowable<Song> {
        return flowable()
    }

    override fun createFlowable(data: Map<String, Any>?): Flowable<Song> {
        return musicManager.currentSong()
    }
}