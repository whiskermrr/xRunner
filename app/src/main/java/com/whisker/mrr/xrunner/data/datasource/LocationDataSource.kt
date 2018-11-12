package com.whisker.mrr.xrunner.data.datasource

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Build
import com.whisker.mrr.xrunner.domain.bus.RxBus
import com.whisker.mrr.xrunner.domain.bus.event.LocationEvent
import com.whisker.mrr.xrunner.infrastructure.LocationService
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class LocationDataSource
@Inject constructor(
    private val context: Context
) {

    private lateinit var locationSubject: PublishSubject<Location>

    fun startTracking() : Flowable<Location> {
        startLocationService(LocationService.ACTION_START_TRACKING)
        locationSubject = PublishSubject.create()
        subscribeToLocationEvents()
        return locationSubject.toFlowable(BackpressureStrategy.LATEST)
    }

    fun pauseTracking() {
        startLocationService(LocationService.ACTION_STOP_TRACKING)
    }

    fun resumeTracking() {
        startLocationService(LocationService.ACTION_START_TRACKING)
    }

    fun stopTracking() {
        stopLocationService()
        RxBus.unsubscribe(this)
        locationSubject.onComplete()
    }

    private fun startLocationService(command: String) {
        val intent = Intent(command)
        intent.setPackage(context.packageName)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }

    private fun stopLocationService() {
        val intent = Intent(context, LocationService::class.java)
        context.stopService(intent)
    }

    private fun subscribeToLocationEvents() {
        RxBus.subscribe(LocationEvent::class.java.name, this, Consumer { event ->
            if(event is LocationEvent) {
                locationSubject.onNext(event.location)
            }
        })
    }

    @SuppressLint("MissingPermission")
    fun getBestLastKnownLocation() : Maybe<Location> {
        val locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers = locationManager.getProviders(true)
        var bestLocation: Location? = null

        for(provider in providers) {
            val location: Location = locationManager.getLastKnownLocation(provider) ?: continue
            if(bestLocation == null || location.accuracy < bestLocation.accuracy) {
                bestLocation = location
            }
        }
        return if(bestLocation != null) {
            Maybe.just(bestLocation)
        } else {
            Maybe.empty()
        }
    }
}