package com.whisker.mrr.xrunner.presentation.views.artist

import androidx.lifecycle.MutableLiveData
import com.whisker.mrr.domain.interactor.GetArtistsInteractor
import com.whisker.mrr.domain.interactor.GetSongsByArtistIdInteractor
import com.whisker.mrr.domain.interactor.SetSongsInteractor
import com.whisker.mrr.domain.model.Artist
import com.whisker.mrr.xrunner.presentation.views.music.BaseBrowseMusicViewModel
import javax.inject.Inject

class BrowseArtistsViewModel @Inject constructor(
    private val getArtistsInteractor: GetArtistsInteractor,
    private val getSongsByArtistIdInteractor: GetSongsByArtistIdInteractor,
    private val setSongsInteractor: SetSongsInteractor
) : BaseBrowseMusicViewModel() {

    private val artistList = MutableLiveData<List<Artist>>()

    init {
        fetchArtists()
    }

    private fun fetchArtists() {
        disposables.add(
            getArtistsInteractor.getArtists()
                .subscribe({ artists ->
                    artistList.postValue(artists)
                }, Throwable::printStackTrace)
        )
    }

    fun setSongs(artistID: Long) {
        disposables.add(
            getSongsByArtistIdInteractor.getSongsByArtistID(artistID)
                .flatMapCompletable { setSongsInteractor.setSongs(it, true) }
                .subscribe({
                    isSongListSet.postValue(true)
                }, Throwable::printStackTrace)
        )
    }

    fun getArtistList() = artistList
}