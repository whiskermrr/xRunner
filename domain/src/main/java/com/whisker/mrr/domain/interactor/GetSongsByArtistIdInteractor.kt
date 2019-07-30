package com.whisker.mrr.domain.interactor

import com.whisker.mrr.domain.model.Song
import com.whisker.mrr.domain.repository.MusicRepository
import com.whisker.mrr.domain.usecase.SingleUseCase
import io.reactivex.Single
import io.reactivex.SingleTransformer
import java.lang.IllegalArgumentException

class GetSongsByArtistIdInteractor(
    transformer: SingleTransformer<List<Song>, List<Song>>,
    private val musicRepository: MusicRepository
) : SingleUseCase<List<Song>>(transformer) {

    companion object {
        const val PARAM_ARTIST_ID = "param_artist_id"
    }

    fun getSongsByArtistID(artistID: Long) : Single<List<Song>> {
        val data = HashMap<String, Any>()
        data[PARAM_ARTIST_ID] = artistID
        return single(data)
    }

    override fun createSingle(data: Map<String, Any>?): Single<List<Song>> {
        val artistID = data?.getValue(PARAM_ARTIST_ID)
        artistID?.let {  id ->
            return musicRepository.getSongsByArtistID(id as Long)
        }
        return Single.error(IllegalArgumentException("Argument @artistID (Long) must be provided."))
    }
}