package com.whisker.mrr.domain.interactor

import com.whisker.mrr.domain.manager.MusicManager
import com.whisker.mrr.domain.usecase.CompletableUseCase
import com.whisker.mrr.domain.usecase.UseCase
import io.reactivex.Completable
import io.reactivex.CompletableTransformer

class StopMusicInteractor(
    private val musicManager: MusicManager
) : UseCase() {


    override fun execute(data: Map<String, Any>?) {
        musicManager.stop()
    }
}