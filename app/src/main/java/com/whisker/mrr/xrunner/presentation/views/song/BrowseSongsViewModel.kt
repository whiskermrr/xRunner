package com.whisker.mrr.xrunner.presentation.views.song

import androidx.lifecycle.MutableLiveData
import com.whisker.mrr.domain.interactor.GetSongsInteractor
import com.whisker.mrr.domain.interactor.SetSongsInteractor
import com.whisker.mrr.domain.model.Song
import com.whisker.mrr.xrunner.presentation.views.music.BaseBrowseMusicViewModel
import javax.inject.Inject

class BrowseSongsViewModel @Inject constructor(
    private val getSongsInteractor: GetSongsInteractor,
    private val setSongsInteractor: SetSongsInteractor
) : BaseBrowseMusicViewModel() {

    private val songList = MutableLiveData<BrowseSongsViewState>()

    init {
        fetchSongs()
    }

    private fun fetchSongs() {
        disposables.add(
            getSongsInteractor.getSongs()
                .subscribe({ songs ->
                    songList.postValue(BrowseSongsViewState.Songs(songs))
                }, { error ->
                    songList.postValue(BrowseSongsViewState.Error(error.message))
                })
        )
    }

    fun setSongs(songs: List<Song>, currentPosition: Int) {
        disposables.add(
            setSongsInteractor.setSongs(songs, true, currentPosition)
                .subscribe({
                    isSongListSet.postValue(true)
                }, { error ->
                    songList.postValue(BrowseSongsViewState.Error(error.message))
                })
        )
    }

    fun getSongList() = songList
}