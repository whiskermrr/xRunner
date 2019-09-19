package com.whisker.mrr.xrunner.presentation.views.song

import com.whisker.mrr.domain.model.Song

sealed class BrowseSongsViewState {
    class Error(val msg: String?) : BrowseSongsViewState()
    class Songs(val songs: List<Song>) : BrowseSongsViewState()
}