package com.whisker.mrr.xrunner.presentation.map

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.whisker.mrr.xrunner.R
import com.whisker.mrr.xrunner.presentation.BaseFragment
import kotlinx.android.synthetic.main.fragment_run.*
import java.util.*

class RunFragment : BaseFragment() {

    private lateinit var viewModel: MapViewModel
    private var isTracking: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_run, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MapViewModel::class.java)

        viewModel.getRouteStats().observe(this, Observer {stats ->
            tvDistance.text = String.format(Locale.getDefault(), "%d.%d", stats.kilometers, stats.meters)
            tvPace.text = String.format(Locale.getDefault(), "%d'%02d''", stats.pace.toInt(), (stats.pace % 1 * 100).toInt())
        })

        bStartRun.setOnClickListener {
            isTracking = if(!isTracking) {
                viewModel.startTracking()
                tvTime.base = SystemClock.elapsedRealtime()
                tvTime.start()
                true
            } else {
                viewModel.stopTracking()
                tvTime.stop()
                false
            }
        }
    }
}