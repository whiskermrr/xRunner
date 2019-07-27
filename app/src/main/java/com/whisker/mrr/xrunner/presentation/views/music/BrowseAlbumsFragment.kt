package com.whisker.mrr.xrunner.presentation.views.music

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.whisker.mrr.xrunner.R
import com.whisker.mrr.xrunner.presentation.adapters.AlbumsAdapter
import com.whisker.mrr.xrunner.presentation.adapters.PaddingItemDecoration
import com.whisker.mrr.xrunner.presentation.views.BaseFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_browse_albums.*

class BrowseAlbumsFragment : BaseFragment() {

    private lateinit var viewModel: BrowseAlbumsViewModel
    private lateinit var albumsAdapter: AlbumsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_browse_albums, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(BrowseAlbumsViewModel::class.java)

        albumsAdapter = AlbumsAdapter()
        val gridLayoutManager = GridLayoutManager(context, 2)
        rvAlbums.layoutManager = gridLayoutManager
        rvAlbums.adapter = albumsAdapter
        rvAlbums.addItemDecoration(PaddingItemDecoration(resources.getDimensionPixelOffset(R.dimen.music_grid_spacing)))

        viewModel.getAlbumList().observe(this, Observer { albums ->
            albumsAdapter.setAlbums(albums)
        })
    }
}