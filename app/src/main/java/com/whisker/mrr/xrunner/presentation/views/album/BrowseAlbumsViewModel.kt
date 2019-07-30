package com.whisker.mrr.xrunner.presentation.views.album

import androidx.lifecycle.MutableLiveData
import com.whisker.mrr.domain.interactor.GetAlbumsInteractor
import com.whisker.mrr.domain.interactor.GetSongsInteractor
import com.whisker.mrr.domain.interactor.SetSongsInteractor
import com.whisker.mrr.domain.model.Album
import com.whisker.mrr.xrunner.presentation.views.music.BaseBrowseMusicViewModel
import javax.inject.Inject

class BrowseAlbumsViewModel @Inject constructor(
        private val getAlbumsInteractor: GetAlbumsInteractor,
        private val setSongsInteractor: SetSongsInteractor,
        private val getSongsInteractor: GetSongsInteractor
) : BaseBrowseMusicViewModel() {

    private val albumList = MutableLiveData<List<Album>>()

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

    fun getAlbumList() = albumList
}