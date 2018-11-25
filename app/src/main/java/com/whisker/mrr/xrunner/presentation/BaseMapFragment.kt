package com.whisker.mrr.xrunner.presentation

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions

abstract class BaseMapFragment : BaseFragment(), OnMapReadyCallback {

    protected lateinit var polylineOptions: PolylineOptions
    protected lateinit var mMap: GoogleMap
    protected lateinit var myRun: Polyline
    protected open val isMyLocationEnabled = true

    abstract fun onMapCreated()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        polylineOptions = PolylineOptions()
            .clickable(false)
            .color(Color.BLUE)
            .width(20f)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(p0: GoogleMap?) {
        mMap = p0!!
        mMap.isMyLocationEnabled = isMyLocationEnabled
        myRun = mMap.addPolyline(polylineOptions)
        onMapCreated()
    }
}