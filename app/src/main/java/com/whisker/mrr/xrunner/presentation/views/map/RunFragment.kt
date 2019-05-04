package com.whisker.mrr.xrunner.presentation.views.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.whisker.mrr.xrunner.R
import com.whisker.mrr.xrunner.presentation.model.RouteModel
import com.whisker.mrr.xrunner.presentation.views.BaseMapFragment
import com.whisker.mrr.xrunner.presentation.views.summary.SummaryRunFragment
import com.whisker.mrr.xrunner.utils.XRunnerConstants
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_run.*

class RunFragment : BaseMapFragment() {

    private lateinit var viewModel: RunViewModel
    private var isTracking: Boolean = false
    private var isMapShown: Boolean = false
    private var isMusicPlaying: Boolean = false

    private val lastLocationObserver = Observer<LatLng> { lastLocation ->
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLocation, 18f))
        viewModel.getLastKnownLocation().removeObservers(this)
    }

    private val routeObserver = Observer<RouteModel> { route ->
        tvDistance.text = getString(R.string.distance_format, route.routeStats.kilometers, route.routeStats.meters)
        tvPace.text = getString(R.string.pace_format, route.routeStats.paceMin, route.routeStats.paceSec)

        if(isMapShown) {
            myRun.points = route.waypoints
            if(myRun.points.isNotEmpty()) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myRun.points.last(), 18f))
            }
        }
    }

    private val isTrackingObserver = Observer<Boolean> {
        isTracking = it
        if(isTracking && mainActivity.isBottomNavEnabled) {
            mainActivity.disableBottomNavigation()
        } else if(!isTracking && !mainActivity.isBottomNavEnabled) {
            mainActivity.enableBottomNavigation()
        }
    }

    private val runTimeObserver = Observer<String> {
        tvTime.text = it
    }

    private val onRunFinishedObserver = Observer<RouteModel> {
        val bundle = Bundle()
        bundle.putParcelable(XRunnerConstants.EXTRA_FINAL_ROUTE_KEY, it)
        val fragment = SummaryRunFragment()
        fragment.arguments = bundle
        mainActivity.switchContent(fragment)
    }

    private val currentSongObserver = Observer<String> {
        tvSongName.text = it
    }

    private val isMusicPlayingObserver = Observer<Boolean> {
        isMusicPlaying = it
        if(isMusicPlaying) {
            ibPlayPauseMusic.background = resources.getDrawable(R.drawable.ic_stop_music)
        } else {
            ibPlayPauseMusic.setBackgroundResource(R.drawable.ic_play)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_run, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity.toolbar.title = getString(R.string.title_run)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RunViewModel::class.java)

        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        mapView.getMapAsync(this)

        viewModel.getRoute().observe(this, routeObserver)
        viewModel.getIsTracking().observe(this, isTrackingObserver)
        viewModel.getTime().observe(this, runTimeObserver)
        viewModel.getFinalRoute().observe(this, onRunFinishedObserver)
        viewModel.getCurrentSong().observe(this, currentSongObserver)
        viewModel.getIsMusicPlaying().observe(this, isMusicPlayingObserver)

        bStartRun.setOnClickListener { onStartClick() }

        bPauseRun.setOnClickListener { onPauseClick() }

        bResumeRun.setOnClickListener { onResumeClick() }

        bStopRun.setOnClickListener { onStopClick() }

        bLocation.setOnClickListener { showMap() }

        bDismiss.setOnClickListener { hideMap() }

        ibPlayPauseMusic.setOnClickListener {
            if(isMusicPlaying) {
                viewModel.pausePlayingMusic()
            } else {
                viewModel.startPlayingMusic()
            }
        }

        ibNextSong.setOnClickListener { viewModel.nextSong() }
        ibPreviousSong.setOnClickListener { viewModel.previousSong() }

        viewModel.getMusic()
    }

    override fun onMapCreated() {
        viewModel.onMapShown()
        viewModel.getLastKnownLocation().observe(this, lastLocationObserver)
    }

    private fun showMap() {
        isMapShown = true
        mapView.visibility = View.VISIBLE
        bDismiss.visibility = View.VISIBLE
    }

    private fun hideMap() {
        isMapShown = false
        mapView.visibility = View.GONE
        bDismiss.visibility = View.GONE
    }

    private fun onStartClick() {
        viewModel.startTracking()
        bStartRun.visibility = View.GONE
        bPauseRun.visibility = View.VISIBLE
    }

    private fun onPauseClick() {
        viewModel.pauseTracking()
        bPauseRun.visibility = View.GONE
        bStopRun.visibility = View.VISIBLE
        bResumeRun.visibility = View.VISIBLE
    }

    private fun onResumeClick() {
        viewModel.resumeTracking()
        bStopRun.visibility = View.GONE
        bResumeRun.visibility = View.GONE
        bPauseRun.visibility = View.VISIBLE
    }

    private fun onStopClick() {
        viewModel.stopTracking()
        bStopRun.visibility = View.GONE
        bResumeRun.visibility = View.GONE
        bStartRun.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if(!mainActivity.isBottomNavEnabled) {
            mainActivity.enableBottomNavigation()
        }
    }
}