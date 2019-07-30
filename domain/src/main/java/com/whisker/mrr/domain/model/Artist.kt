package com.whisker.mrr.domain.model

data class Artist(
    var id: Long,
    var artistName: String,
    var artistKey: String,
    var numberOfAlbums: Int,
    var numberOfTracks: Int,
    var albums: List<Album> = listOf()
)