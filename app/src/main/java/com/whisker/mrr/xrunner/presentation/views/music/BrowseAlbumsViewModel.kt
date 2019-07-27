package com.whisker.mrr.xrunner.presentation.views.music

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whisker.mrr.domain.interactor.GetAlbumsInteractor
import com.whisker.mrr.domain.model.Album
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class BrowseAlbumsViewModel @Inject constructor(
        private val getAlbumsInteractor: GetAlbumsInteractor
) : ViewModel() {

    private val albumList = MutableLiveData<List<Album>>()
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

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    fun getAlbumList() = albumList
}