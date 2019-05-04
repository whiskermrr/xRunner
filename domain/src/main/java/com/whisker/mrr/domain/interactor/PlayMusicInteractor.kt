package com.whisker.mrr.domain.interactor

import com.whisker.mrr.domain.manager.MusicManager
import com.whisker.mrr.domain.usecase.CompletableUseCase
import io.reactivex.Completable
import io.reactivex.CompletableTransformer

class PlayMusicInteractor(
    transformer: CompletableTransformer,
    private val musicManager: MusicManager
) : CompletableUseCase(transformer) {

    fun playMusic() : Completable {
        return completable()
    }

    override fun createCompletable(data: Map<String, Any>?): Completable {
        return musicManager.play()
    }
}