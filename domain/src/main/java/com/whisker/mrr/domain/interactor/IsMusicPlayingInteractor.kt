package com.whisker.mrr.domain.interactor

import com.whisker.mrr.domain.manager.MusicManager
import com.whisker.mrr.domain.usecase.FlowableUseCase
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer

class IsMusicPlayingInteractor(
    transformer: FlowableTransformer<Boolean, Boolean>,
    private val musicManager: MusicManager
) : FlowableUseCase<Boolean>(transformer) {

    fun isMusicPlaying() : Flowable<Boolean> {
        return flowable()
    }

    override fun createFlowable(data: Map<String, Any>?): Flowable<Boolean> {
        return musicManager.isMusicPlaying()
    }
}