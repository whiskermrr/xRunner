package com.whisker.mrr.domain.model

data class Artist(
    var artistName: String? = null,
    var artistKey: String? = null,
    var numberOfAlbums: Int? = null,
    var numberOfTracks: Int? = null,
    var albums: List<Album>? = null
)