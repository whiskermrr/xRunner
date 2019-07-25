package com.whisker.mrr.domain.repository

import com.whisker.mrr.domain.model.Album
import com.whisker.mrr.domain.model.Artist
import com.whisker.mrr.domain.model.Song
import io.reactivex.Single

interface MusicRepository {
    fun getSongs(albumID: Long? = null) : Single<List<Song>>
    fun getAlbums(artistID: String? = null) : Single<List<Album>>
    fun getArtists() : Single<List<Artist>>
    fun getLastPlaylist() : Single<List<Song>>
}