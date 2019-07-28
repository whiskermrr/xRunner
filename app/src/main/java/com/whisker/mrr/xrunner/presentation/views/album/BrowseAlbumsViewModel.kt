package com.whisker.mrr.xrunner.presentation.views.album

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whisker.mrr.domain.interactor.GetAlbumsInteractor
import com.whisker.mrr.domain.interactor.GetSongsInteractor
import com.whisker.mrr.domain.interactor.SetSongsInteractor
import com.whisker.mrr.domain.model.Album
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class BrowseAlbumsViewModel @Inject constructor(
        private val getAlbumsInteractor: GetAlbumsInteractor,
        private val setSongsInteractor: SetSongsInteractor,
        private val getSongsInteractor: GetSongsInteractor
) : ViewModel() {

    private val albumList = MutableLiveData<List<Album>>()
    private val isSongListSet = MutableLiveData<Boolean>()
    private val disposables = CompositeDisposable()

    init {
        fetchAlbums()
    }

    private fun fetchAlbums() {
        disposables.add(
            getAlbumsInteractor.getAlbums()
                .subscribe({ albums ->
                    albumList.postValue(albums)
                }, {
                    it.printStackTrace()
                })
        )
    }

    fun setSongs(albumID: Long) {
        disposables.add(
            getSongsInteractor.getSongs(albumID)
                .flatMapCompletable { setSongsInteractor.setSongs(it, true) }
                .subscribe({
                    isSongListSet.postValue(true)
                }, Throwable::printStackTrace)
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    fun getAlbumList() = albumList
    fun getIsSongListSet() = isSongListSet
}