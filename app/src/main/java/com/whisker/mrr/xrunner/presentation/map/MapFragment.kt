package com.whisker.mrr.xrunner.presentation.map

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.whisker.mrr.xrunner.R
import com.whisker.mrr.xrunner.presentation.BaseFragment
import kotlinx.android.synthetic.main.fragment_map.*

class MapFragment : BaseFragment() {

    private lateinit var viewModel: MapViewModel
    private lateinit var polylineOptions: PolylineOptions
    private lateinit var mMap: GoogleMap
    private lateinit var myRun: Polyline

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
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MapViewModel::class.java)

        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        mapView.getMapAsync {map ->
            mMap = map
        }

        viewModel.getLastRoutePoint().observe(this, Observer {
            val points = myRun.points
            points.add(it)
            myRun.points = points
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 18f))
        })

        bStart.setOnClickListener {
            startRunning()
        }
    }

    private fun startRunning() {
        myRun = mMap.addPolyline(polylineOptions)
        viewModel.startTracking()
    }
}