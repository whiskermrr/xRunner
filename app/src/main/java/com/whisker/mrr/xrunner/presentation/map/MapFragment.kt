package com.whisker.mrr.xrunner.presentation.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.whisker.mrr.xrunner.R
import com.whisker.mrr.xrunner.presentation.BaseMapFragment
import kotlinx.android.synthetic.main.fragment_map.*

class MapFragment : BaseMapFragment(), OnMapReadyCallback {

    private lateinit var viewModel: MapViewModel

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity.hideBottomNavigation()
        viewModel = mainActivity.run {
            ViewModelProviders.of(this, viewModelFactory).get(MapViewModel::class.java)
        }

        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        mapView.getMapAsync(this)

        bDismiss.setOnClickListener {
            mainActivity.onBackPressed()
        }
    }

    override fun onMapCreated() {
        viewModel.onMapShown()
        viewModel.getLastKnownLocation().observe(this, lastLocationObserver)
        viewModel.getRoutePoints().observe(this, routeObserver)
    }

    override fun onDestroy() {
        super.onDestroy()
        mainActivity.showBottomNavigation()
    }
}