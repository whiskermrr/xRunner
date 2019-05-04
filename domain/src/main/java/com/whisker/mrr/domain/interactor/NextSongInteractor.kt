package com.whisker.mrr.domain.interactor

import com.whisker.mrr.domain.manager.MusicManager
import com.whisker.mrr.domain.usecase.CompletableUseCase
import io.reactivex.Completable
import io.reactivex.CompletableTransformer

class NextSongInteractor(
    transformer: CompletableTransformer,
    private val musicManager: MusicManager
) : CompletableUseCase(transformer) {

    fun nextSong() : Completable {
        return completable()
    }

    override fun createCompletable(data: Map<String, Any>?): Completable {
        return musicManager.nextSong()
    }
}