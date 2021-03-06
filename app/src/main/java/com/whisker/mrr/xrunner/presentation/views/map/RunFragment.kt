package com.whisker.mrr.xrunner.presentation.views.map

import android.Manifest
import android.content.pm.PackageManager
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
import com.whisker.mrr.xrunner.presentation.model.TrackingState
import com.whisker.mrr.xrunner.presentation.views.base.BaseMapFragment
import com.whisker.mrr.xrunner.presentation.views.music.MusicPlayerFragment
import com.whisker.mrr.xrunner.presentation.views.summary.SummaryRunFragment
import com.whisker.mrr.xrunner.utils.PermissionsUtils
import com.whisker.mrr.xrunner.utils.XRunnerConstants
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_run.*

class RunFragment : BaseMapFragment() {

    private lateinit var viewModel: RunViewModel
    private lateinit var trackingState: TrackingState
    private var isMapShown: Boolean = false

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

    private val trackingStateObserver = Observer<TrackingState> { state ->
        trackingState = state
        if((state == TrackingState.RESUME || state == TrackingState.START) && mainActivity.isBottomNavEnabled) {
            mainActivity.disableBottomNavigation()
        } else if((state == TrackingState.STOP || state == TrackingState.PAUSE) && !mainActivity.isBottomNavEnabled) {
            mainActivity.enableBottomNavigation()
        }
        setViewAccordingToTrackingState()
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_run, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity.toolbar.title = getString(R.string.title_run)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RunViewModel::class.java)

        setViewAccordingToTrackingState()

        if(PermissionsUtils.checkPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            initMap(savedInstanceState)
        }

        viewModel.getRoute().observe(this, routeObserver)
        viewModel.getTrackingState().observe(this, trackingStateObserver)
        viewModel.getTime().observe(this, runTimeObserver)
        viewModel.getFinalRoute().observe(this, onRunFinishedObserver)

        bStartRun.setOnClickListener {
            if(PermissionsUtils.checkPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                viewModel.startTracking()
            }
        }

        bPauseRun.setOnClickListener { viewModel.pauseTracking() }

        bResumeRun.setOnClickListener { viewModel.resumeTracking() }

        bStopRun.setOnClickListener { viewModel.stopTracking() }

        bDismiss.setOnClickListener { hideMap() }

        childFragmentManager.beginTransaction()
            .add(R.id.musicPlayerContainer, MusicPlayerFragment())
            .commit()
    }

    private fun initMap(savedInstanceState: Bundle? = null) {
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        mapView.getMapAsync(this)
    }

    override fun onMapCreated() {
        viewModel.getLastKnownLocation().observe(this, lastLocationObserver)
        viewModel.onMapShown()
        bLocation.setOnClickListener { showMap() }
    }

    private fun showMap() {
        isMapShown = true
        musicPlayerContainer.visibility = View.GONE
        mapView.visibility = View.VISIBLE
        bDismiss.visibility = View.VISIBLE
    }

    private fun hideMap() {
        isMapShown = false
        musicPlayerContainer.visibility = View.VISIBLE
        mapView.visibility = View.GONE
        bDismiss.visibility = View.GONE
    }

    private fun setViewAccordingToTrackingState() {
        if(::trackingState.isInitialized) {
            when(trackingState) {
                TrackingState.START -> onStartTracking()
                TrackingState.PAUSE -> onPauseTracking()
                TrackingState.STOP -> onStopTracking()
                TrackingState.RESUME -> onResumeTracking()
            }
        }
    }

    private fun onStartTracking() {
        bStartRun.visibility = View.GONE
        bPauseRun.visibility = View.VISIBLE
    }

    private fun onPauseTracking() {
        bPauseRun.visibility = View.GONE
        bStopRun.visibility = View.VISIBLE
        bResumeRun.visibility = View.VISIBLE
    }

    private fun onResumeTracking() {
        bStopRun.visibility = View.GONE
        bResumeRun.visibility = View.GONE
        bPauseRun.visibility = View.VISIBLE
    }

    private fun onStopTracking() {
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when(requestCode) {
            PermissionsUtils.REQUEST_CODE -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initMap(null)
                } else {
                    val rationalMessage = getString(R.string.permission_location_info)
                    PermissionsUtils.onRequestPermissionDenied(this, permissions, rationalMessage)
                }
            }
        }
    }
}