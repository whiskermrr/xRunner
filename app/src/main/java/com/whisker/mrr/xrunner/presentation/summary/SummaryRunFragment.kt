package com.whisker.mrr.xrunner.presentation.summary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.whisker.mrr.xrunner.R
import com.whisker.mrr.xrunner.domain.model.Route
import com.whisker.mrr.xrunner.presentation.BaseMapFragment
import com.whisker.mrr.xrunner.utils.xRunnerConstants
import kotlinx.android.synthetic.main.fragment_summary_run.*

class SummaryRunFragment : BaseMapFragment(), OnMapReadyCallback {

    private lateinit var viewModel: SummaryRunViewModel
    private lateinit var finalRoute: Route
    override val isMyLocationEnabled: Boolean
        get() = false

    private val routeSavedObserver = Observer<Boolean> {
        Toast.makeText(mainActivity, "Route Saved", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(arguments != null) {
            finalRoute = arguments?.getParcelable(xRunnerConstants.EXTRA_FINAL_ROUTE_KEY)!!
        }

        polylineOptions.addAll(finalRoute.waypoints)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_summary_run, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = mainActivity.run {
            ViewModelProviders.of(this, viewModelFactory).get(SummaryRunViewModel::class.java)
        }

        liteMapView.onCreate(savedInstanceState)
        liteMapView.onResume()
        liteMapView.getMapAsync(this)

        viewModel.getIsRouteSaved().observe(this, routeSavedObserver)
        viewModel.saveRoute(finalRoute)
    }

    override fun onMapCreated() {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myRun.points.last(), 18f))
    }
}