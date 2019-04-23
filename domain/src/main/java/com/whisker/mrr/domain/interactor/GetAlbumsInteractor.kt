package com.whisker.mrr.domain.interactor

import com.whisker.mrr.domain.model.Album
import com.whisker.mrr.domain.repository.MusicRepository
import com.whisker.mrr.domain.usecase.SingleUseCase
import io.reactivex.Single
import io.reactivex.SingleTransformer

class GetAlbumsInteractor(
    transformer: SingleTransformer<List<Album>, List<Album>>,
    private val musicRepository: MusicRepository
) : SingleUseCase<List<Album>>(transformer) {

    fun getAlbums() : Single<List<Album>> {
        return single()
    }

    override fun createSingle(data: Map<String, Any>?): Single<List<Album>> {
        return musicRepository.getAlbums()
    }
}