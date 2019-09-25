package com.whisker.mrr.xrunner.presentation.views.album

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.whisker.mrr.xrunner.R
import com.whisker.mrr.xrunner.presentation.adapters.AlbumsAdapter
import com.whisker.mrr.xrunner.presentation.adapters.PaddingItemDecoration
import com.whisker.mrr.xrunner.presentation.views.base.BaseFragment
import com.whisker.mrr.xrunner.utils.TAG
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_browse_music.*

class BrowseAlbumsFragment : BaseFragment() {

    private lateinit var viewModel: BrowseAlbumsViewModel
    private lateinit var albumsAdapter: AlbumsAdapter
    private lateinit var disposables: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disposables = CompositeDisposable()
        albumsAdapter = AlbumsAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_browse_music, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(BrowseAlbumsViewModel::class.java)

        val gridLayoutManager = GridLayoutManager(context, 2)
        rvMusic.layoutManager = gridLayoutManager
        rvMusic.adapter = albumsAdapter
        rvMusic.addItemDecoration(PaddingItemDecoration(resources.getDimensionPixelOffset(R.dimen.music_grid_spacing)))

        viewModel.getAlbumList().observe(viewLifecycleOwner, Observer { viewState ->
            when(viewState) {
                is BrowseAlbumsViewState.Albums -> {
                    val albums = viewState.albums
                    if(albums.isNotEmpty()) {
                        albumsAdapter.setAlbums(albums)
                    } else {
                        tvMusicNoResult.visibility = View.VISIBLE
                    }
                }
                is BrowseAlbumsViewState.Error -> {
                    // TODO: dialog showing error
                    Log.e(TAG(), viewState.msg ?: "Unknown Error")
                }
            }
        })

        viewModel.getIsSongListSet().observe(viewLifecycleOwner, Observer { isSet ->
            if(isSet) mainActivity.onBackPressed()
        })

        disposables.add(
            albumsAdapter.clickEvent()
                .subscribe({ album ->
                    val albumID = album.id
                    viewModel.setSongs(albumID)
                }, Throwable::printStackTrace)
        )
    }
}