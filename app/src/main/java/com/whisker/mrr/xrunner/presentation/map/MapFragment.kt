package com.whisker.mrr.xrunner.presentation.map

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
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
import java.lang.StringBuilder

class MapFragment : BaseFragment() {

    private lateinit var viewModel: MapViewModel
    private lateinit var polylineOptions: PolylineOptions
    private lateinit var mMap: GoogleMap
    private lateinit var myRun: Polyline
    var isTracking: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.fragment_map, container, false)

        polylineOptions = PolylineOptions()
            .clickable(false)
            .color(Color.BLUE)
            .width(20f)

        return view
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MapViewModel::class.java)

        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        mapView.getMapAsync {map ->
            mMap = map
            mMap.isMyLocationEnabled = true
            myRun = mMap.addPolyline(polylineOptions)
            viewModel.onMapShown()
        }

        viewModel.getLastKnownLocation().observe(this, Observer {
            myRun.points = listOf(it)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 18f))
            viewModel.getLastKnownLocation().removeObservers(this)
        })

        viewModel.getRoutePoints().observe(this, Observer {
            myRun.points = it
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it.last(), 18f))
        })

        viewModel.getRouteStats().observe(this, Observer {
            val stringBuilder = StringBuilder()
            stringBuilder.append(it.kilometers).append("\n")
            stringBuilder.append(it.meters).append("\n").append("\n")
            stringBuilder.append(it.hours).append("\n")
            stringBuilder.append(it.minutes).append("\n")
            stringBuilder.append(it.seconds).append("\n").append("\n")
            stringBuilder.append(it.averageSpeed)

            tvStats.text = stringBuilder.toString()
        })

        bStart.setOnClickListener {
            if(!isTracking) {
                isTracking = true
                viewModel.startTracking()
            } else {
                viewModel.stopTracking()
            }
        }
    }
}