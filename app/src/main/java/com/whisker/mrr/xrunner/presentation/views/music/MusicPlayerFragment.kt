package com.whisker.mrr.xrunner.presentation.views.music

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.whisker.mrr.xrunner.R
import com.whisker.mrr.xrunner.presentation.views.base.BaseFragment
import com.whisker.mrr.xrunner.utils.PermissionsUtils
import kotlinx.android.synthetic.main.fragment_music_player.*

class MusicPlayerFragment : BaseFragment() {

    companion object {
        private var isMusicSet = false
    }

    private lateinit var viewModel: MusicPlayerViewModel
    private var isMusicPlaying: Boolean = false

    private val currentSongObserver = Observer<String> {
        isMusicSet = true
        tvSongName.text = it
        groupPlayerButtons.visibility = View.VISIBLE
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
        ibMusic.setOnClickListener {
            if(PermissionsUtils.checkPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                mainActivity.switchContent(MusicBrowserFragment())
            }
        }

        if(!isMusicSet) {
            if(PermissionsUtils.isPermissionGranted(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                viewModel.getMusic()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when(requestCode) {
            PermissionsUtils.REQUEST_CODE -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mainActivity.switchContent(MusicBrowserFragment())
                } else {
                    val rationalMessage = getString(R.string.permission_music_info)
                    PermissionsUtils.onRequestPermissionDenied(this, permissions, rationalMessage)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopMusic()
    }
}