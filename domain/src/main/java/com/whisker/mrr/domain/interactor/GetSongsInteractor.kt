package com.whisker.mrr.domain.interactor

import com.whisker.mrr.domain.model.Song
import com.whisker.mrr.domain.repository.MusicRepository
import com.whisker.mrr.domain.usecase.SingleUseCase
import io.reactivex.Single
import io.reactivex.SingleTransformer

class GetSongsInteractor(
    transformer: SingleTransformer<List<Song>, List<Song>>,
    private val musicRepository: MusicRepository
) : SingleUseCase<List<Song>>(transformer) {

    companion object {
        const val PARAM_ALBUM_ID = "param_album_id"
    }

    fun getSongs(albumID: Long? = null) : Single<List<Song>> {
        albumID?.let { id ->
            val data = HashMap<String, Any>()
            data[PARAM_ALBUM_ID] = id
            return single(data)
        } ?: kotlin.run {
            return single()
        }
    }

    override fun createSingle(data: Map<String, Any>?): Single<List<Song>> {
        val albumID = data?.get(PARAM_ALBUM_ID) as Long
        return musicRepository.getSongs(albumID)
    }
}