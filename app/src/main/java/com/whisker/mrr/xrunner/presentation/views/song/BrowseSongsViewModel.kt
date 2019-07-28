package com.whisker.mrr.xrunner.presentation.views.song

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whisker.mrr.domain.interactor.GetSongsInteractor
import com.whisker.mrr.domain.interactor.SetSongsInteractor
import com.whisker.mrr.domain.model.Song
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class BrowseSongsViewModel @Inject constructor(
    private val getSongsInteractor: GetSongsInteractor,
    private val setSongsInteractor: SetSongsInteractor
) : ViewModel() {

    private val songList = MutableLiveData<List<Song>>()
    private val isSongListSet = MutableLiveData<Boolean>()
    private val disposables = CompositeDisposable()

    init {
        fetchSongs()
    }

    private fun fetchSongs() {
        disposables.add(
            getSongsInteractor.getSongs()
                .subscribe({ songs ->
                    songList.postValue(songs)
                }, Throwable::printStackTrace)
        )
    }

    fun setSongs(songs: List<Song>, currentPosition: Int) {
        disposables.add(
            setSongsInteractor.setSongs(songs, true, currentPosition)
                .subscribe({
                    isSongListSet.postValue(true)
                }, Throwable::printStackTrace)
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    fun getSongList() = songList
    fun getIsSongListSet() = isSongListSet
}