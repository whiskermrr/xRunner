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
        const val KEY_CURRENT_POSITION = "key_current_position"
        const val KEY_IS_START_PLAYING = "key_is_start_playing"
    }

    fun setSongs(songs: List<Song>, isStartPlaying: Boolean, currentPosition: Int? = null) : Completable {
        val data = HashMap<String, Any>()
        data[KEY_SONGS] = songs
        data[KEY_IS_START_PLAYING] = isStartPlaying
        if(currentPosition != null) {
            data[KEY_CURRENT_POSITION] = currentPosition
        }
        return completable(data)
    }

    override fun createCompletable(data: Map<String, Any>?): Completable {
        val dataSongs = data?.get(KEY_SONGS)
        val currentPosition = data?.get(KEY_CURRENT_POSITION)
        val isStartPlaying = data?.get(KEY_IS_START_PLAYING) as Boolean

        dataSongs?.let { songs ->
            return if(songs is List<*>) {
                if(currentPosition != null) {
                    musicManager.setSongs(songs.filterIsInstance<Song>(), isStartPlaying, currentPosition as Int)
                } else {
                    musicManager.setSongs(songs.filterIsInstance<Song>(), isStartPlaying)
                }
            } else {
                Completable.error(ClassCastException("Cannot cost parameter @songs to List<Song>"))
            }
        } ?: return Completable.error(IllegalArgumentException("Parameter @songs must be provided."))
    }
}