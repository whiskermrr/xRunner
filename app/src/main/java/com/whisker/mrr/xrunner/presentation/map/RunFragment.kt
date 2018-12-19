package com.whisker.mrr.xrunner.presentation.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.whisker.mrr.xrunner.R
import com.whisker.mrr.xrunner.presentation.model.Route
import com.whisker.mrr.xrunner.presentation.model.RouteStats
import com.whisker.mrr.xrunner.presentation.BaseMapFragment
import com.whisker.mrr.xrunner.presentation.summary.SummaryRunFragment
import com.whisker.mrr.xrunner.utils.xRunnerConstants
import kotlinx.android.synthetic.main.fragment_run.*

class RunFragment : BaseMapFragment() {

    private lateinit var viewModel: MapViewModel
    private var isTracking: Boolean = false
    private var isMapShown: Boolean = false

    private val lastLocationObserver = Observer<LatLng> { lastLocation ->
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLocation, 18f))
        viewModel.getLastKnownLocation().removeObservers(this)
    }

    private val routeStatsObserver = Observer<RouteStats> { stats ->
        tvDistance.text = getString(R.string.distance_format, stats.kilometers, stats.meters)
        tvPace.text = getString(R.string.pace_format, stats.paceMin, stats.paceSec)
    }

    private val routeObserver = Observer<List<LatLng>> { points ->
        if(isMapShown) {
            myRun.points = points
            if(points.isNotEmpty()) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(points.last(), 18f))
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

    private val onRunFinishedObserver = Observer<Route> {
        val bundle = Bundle()
        bundle.putParcelable(xRunnerConstants.EXTRA_FINAL_ROUTE_KEY, it)
        val fragment = SummaryRunFragment()
        fragment.arguments = bundle
        mainActivity.switchContent(fragment)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_run, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MapViewModel::class.java)

        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        mapView.getMapAsync(this)

        viewModel.getRouteStats().observe(this, routeStatsObserver)
        viewModel.getIsTracking().observe(this, isTrackingObserver)
        viewModel.getTime().observe(this, runTimeObserver)
        viewModel.getFinalRoute().observe(this, onRunFinishedObserver)

        bStartRun.setOnClickListener { onStartClick() }

        bPauseRun.setOnClickListener { onPauseClick() }

        bResumeRun.setOnClickListener { onResumeClick() }

        bStopRun.setOnClickListener { onStopClick() }

        bLocation.setOnClickListener { showMap() }

        bDismiss.setOnClickListener { hideMap() }
    }

    override fun onMapCreated() {
        viewModel.onMapShown()
        viewModel.getLastKnownLocation().observe(this, lastLocationObserver)
    }

    private fun showMap() {
        isMapShown = true
        viewModel.getRoutePoints().observe(this, routeObserver)
        mapView.visibility = View.VISIBLE
        bDismiss.visibility = View.VISIBLE
    }

    private fun hideMap() {
        isMapShown = false
        viewModel.getRoutePoints().removeObservers(this)
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