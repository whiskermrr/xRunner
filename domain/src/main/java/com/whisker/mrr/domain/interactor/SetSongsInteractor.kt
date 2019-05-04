package com.whisker.mrr.domain.interactor

import com.whisker.mrr.domain.manager.MusicManager
import com.whisker.mrr.domain.model.Song
import com.whisker.mrr.domain.usecase.CompletableUseCase
import io.reactivex.Completable
import io.reactivex.CompletableTransformer

class SetSongsInteractor(
    transformer: CompletableTransformer,
    private val musicManager: MusicManager
    ) : CompletableUseCase(transformer) {

    companion object {
        const val KEY_SONGS = "key_songs"
    }

    fun setSongs(songs: List<Song>) : Completable {
        val data = HashMap<String, List<Song>>()
        data[KEY_SONGS] = songs
        return completable(data)
    }

    override fun createCompletable(data: Map<String, Any>?): Completable {
        val param = data?.get(KEY_SONGS)

        param?.let { songs ->
            return if(songs is List<*>) {
                musicManager.setSongs(songs.filterIsInstance<Song>())
            } else {
                Completable.error(ClassCastException("Cannot cost parameter @songs to List<Song>"))
            }
        } ?: return Completable.error(IllegalArgumentException("Parameter @songs must be provided."))
    }
}