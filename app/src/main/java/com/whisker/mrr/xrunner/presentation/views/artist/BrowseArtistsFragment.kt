package com.whisker.mrr.xrunner.presentation.views.artist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.whisker.mrr.xrunner.R
import com.whisker.mrr.xrunner.presentation.views.BaseFragment

class BrowseArtistsFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_browse_artists, container, false)
    }
}