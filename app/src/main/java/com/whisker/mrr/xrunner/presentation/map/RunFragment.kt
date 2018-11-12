package com.whisker.mrr.xrunner.presentation.map

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.whisker.mrr.xrunner.R
import com.whisker.mrr.xrunner.domain.model.RouteStats
import com.whisker.mrr.xrunner.presentation.BaseFragment
import kotlinx.android.synthetic.main.fragment_run.*

class RunFragment : BaseFragment() {

    private lateinit var viewModel: MapViewModel
    private var isTracking: Boolean = false
    private var pauseTime: Long = 0L

    private val routeStatsObserver = Observer<RouteStats> { stats ->
        tvDistance.text = getString(R.string.distance_format, stats.kilometers, stats.meters)
        tvPace.text = getString(R.string.pace_format, stats.paceMin, stats.paceSec)
    }

    private val isTrackingObserver = Observer<Boolean> {
        isTracking = it
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_run, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = mainActivity.run {
            ViewModelProviders.of(this, viewModelFactory).get(MapViewModel::class.java)
        }

        viewModel.getRouteStats().observe(this, routeStatsObserver)
        viewModel.getIsTracking().observe(this, isTrackingObserver)

        bStartRun.setOnClickListener { onStartClick() }

        bPauseRun.setOnClickListener { onPauseClick() }

        bResumeRun.setOnClickListener { onResumeClick() }

        bStopRun.setOnClickListener { onStopClick() }

        bLocation.setOnClickListener { mainActivity.addContent(MapFragment()) }
    }

    private fun onStartClick() {
        pauseTime = 0L
        viewModel.startTracking()
        startChronometer()
        bStartRun.visibility = View.GONE
        bPauseRun.visibility = View.VISIBLE
    }

    private fun onPauseClick() {
        pauseChronometer()
        bPauseRun.visibility = View.GONE
        bStopRun.visibility = View.VISIBLE
        bResumeRun.visibility = View.VISIBLE
    }

    private fun onResumeClick() {
        startChronometer()
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

    private fun pauseChronometer() {
            tvTime.stop()
            pauseTime = SystemClock.elapsedRealtime()
        }

    private fun startChronometer() {
        if(pauseTime == 0L) {
            tvTime.base = SystemClock.elapsedRealtime()
        } else {
            tvTime.base = tvTime.base + SystemClock.elapsedRealtime() - pauseTime
        }

        tvTime.start()
    }
}