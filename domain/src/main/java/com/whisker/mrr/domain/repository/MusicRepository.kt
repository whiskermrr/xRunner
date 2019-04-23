package com.whisker.mrr.domain.repository

import com.whisker.mrr.domain.model.Album
import com.whisker.mrr.domain.model.Song
import io.reactivex.Single

interface MusicRepository {
    fun getSongs(albumID: Long? = null) : Single<List<Song>>
    fun getAlbums() : Single<List<Album>>
    fun getLastPlaylist() : Single<List<Song>>
}