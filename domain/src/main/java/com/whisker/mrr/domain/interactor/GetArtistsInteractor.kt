package com.whisker.mrr.domain.interactor

import com.whisker.mrr.domain.model.Artist
import com.whisker.mrr.domain.repository.MusicRepository
import com.whisker.mrr.domain.usecase.SingleUseCase
import io.reactivex.Single
import io.reactivex.SingleTransformer

class GetArtistsInteractor(
    transformer: SingleTransformer<List<Artist>, List<Artist>>,
    private val musicRepository: MusicRepository
) : SingleUseCase<List<Artist>>(transformer) {

    fun getArtists() : Single<List<Artist>> {
        return single()
    }

    override fun createSingle(data: Map<String, Any>?): Single<List<Artist>> {
        return musicRepository.getArtists()
    }
}