package com.whisker.mrr.xrunner.presentation.views.music

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.whisker.mrr.xrunner.R
import com.whisker.mrr.xrunner.presentation.views.BaseFragment
import kotlinx.android.synthetic.main.fragment_music_player.*

class MusicPlayerFragment : BaseFragment() {

    companion object {
        private var isMusicPlaying: Boolean = false
    }

    private lateinit var viewModel: MusicPlayerViewModel

    private val currentSongObserver = Observer<String> {
        tvSongName.text = it
    }

    private val isMusicPlayingObserver = Observer<Boolean> {
        isMusicPlaying = it
        if(isMusicPlaying) {
            ibPlayPauseMusic.setBackgroundResource(R.drawable.ic_stop_music)
        } else {
            ibPlayPauseMusic.setBackgroundResource(R.drawable.ic_play)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_music_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MusicPlayerViewModel::class.java)
        viewModel.getCurrentSong().observe(this, currentSongObserver)
        viewModel.getIsMusicPlaying().observe(this, isMusicPlayingObserver)

        ibPlayPauseMusic.setOnClickListener {
            if(isMusicPlaying) {
                viewModel.pausePlayingMusic()
            } else {
                viewModel.startPlayingMusic()
            }
        }

        ibNextSong.setOnClickListener { viewModel.nextSong() }
        ibPreviousSong.setOnClickListener { viewModel.previousSong() }
        ibMusic.setOnClickListener { mainActivity.switchContent(MusicBrowserFragment()) }

        if(!isMusicPlaying) {
            viewModel.getMusic()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopMusic()
    }
}