package com.whisker.mrr.xrunner.presentation.map

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.whisker.mrr.xrunner.R
import com.whisker.mrr.xrunner.presentation.BaseFragment
import kotlinx.android.synthetic.main.fragment_map.*

class MapFragment : BaseFragment(), OnMapReadyCallback {

    private lateinit var viewModel: MapViewModel
    private lateinit var polylineOptions: PolylineOptions
    private lateinit var mMap: GoogleMap
    private lateinit var myRun: Polyline
    private var isTracking: Boolean = false

    private val lastLocationObserver = Observer<LatLng> { lastLocation ->
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLocation, 18f))
        viewModel.getLastKnownLocation().removeObservers(this)
    }

    private val routeObserver = Observer<List<LatLng>> { points ->
        myRun.points = points
        if(points.isNotEmpty()) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(points.last(), 18f))
        }
    }

    private val isTrackingObserver = Observer<Boolean> {
        isTracking = it
        initView()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.fragment_map, container, false)

        polylineOptions = PolylineOptions()
            .clickable(false)
            .color(Color.BLUE)
            .width(20f)

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = mainActivity.run {
            ViewModelProviders.of(this, viewModelFactory).get(MapViewModel::class.java)
        }

        viewModel.getIsTracking().observe(this, isTrackingObserver)

        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        mapView.getMapAsync(this)
        initView()

        bStart.setOnClickListener {
            mainActivity.switchContent(RunFragment())
        }

        bDismiss.setOnClickListener {
            mainActivity.onBackPressed()
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(p0: GoogleMap?) {
        mMap = p0!!
        mMap.isMyLocationEnabled = true
        viewModel.onMapShown()
        myRun = mMap.addPolyline(polylineOptions)

        viewModel.getLastKnownLocation().observe(this, lastLocationObserver)
        viewModel.getRoutePoints().observe(this, routeObserver)
    }

    private fun initView() {
        if(isTracking) {
            bStart.visibility = View.GONE
            bDismiss.visibility = View.VISIBLE
        } else {
            bStart.visibility = View.VISIBLE
            bDismiss.visibility = View.GONE
        }
    }
}