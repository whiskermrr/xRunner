package com.whisker.mrr.infrastructure.source

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.location.Location
import android.location.LocationManager
import android.os.IBinder
import com.whisker.mrr.domain.common.bus.RxBus
import com.whisker.mrr.domain.model.Coords
import com.whisker.mrr.infrastructure.LocationEvent
import com.whisker.mrr.infrastructure.LocationService
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class LocationDataManager
@Inject constructor(
    private val context: Context
) : com.whisker.mrr.domain.manager.LocationManager {

    private lateinit var locationSubject: PublishSubject<Coords>
    private lateinit var locationService: LocationService
    private var isServiceBounded = false

    private val serviceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as LocationService.LocationBinder
            locationService = binder.getService()
            isServiceBounded = true
            locationService.startLocationUpdates()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isServiceBounded = false
        }
    }

    override fun startTracking() : Flowable<Coords> {
        if(isServiceBounded) {
            locationService.startLocationUpdates()
        } else {
            startLocationService()
        }
        locationSubject = PublishSubject.create()
        subscribeToLocationEvents()
        return locationSubject.toFlowable(BackpressureStrategy.LATEST)
    }

    override fun pauseTracking() : Completable {
        return Completable.fromAction {
            if(isServiceBounded) {
                locationService.stopLocationUpdates()
            }
        }
    }

    override fun resumeTracking() : Completable {
        return Completable.fromAction {
            if(isServiceBounded) {
                locationService.startLocationUpdates()
            }
        }
    }

    override fun stopTracking() {
        if(isServiceBounded) {
            stopLocationService()
        }
        RxBus.unsubscribe(this)
        locationSubject.onComplete()
    }

    private fun startLocationService() {
        Intent(context, LocationService::class.java).also { intent ->
            context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    private fun stopLocationService() {
        context.unbindService(serviceConnection)
        isServiceBounded = false
    }

    private fun subscribeToLocationEvents() {
        RxBus.subscribe(LocationEvent::class.java.name, this, Consumer { event ->
            if(event is LocationEvent) {
                locationSubject.onNext(Coords(event.location.latitude, event.location.longitude))
            }
        })
    }

    @SuppressLint("MissingPermission")
    override fun getBestLastKnownLocation() : Maybe<Coords> {
        return Maybe.fromCallable {
            val locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val providers = locationManager.getProviders(true)
            var bestLocation: Location? = null

            for(provider in providers) {
                val location: Location = locationManager.getLastKnownLocation(provider) ?: continue
                if(bestLocation == null || location.accuracy < bestLocation.accuracy) {
                    bestLocation = location
                }
            }
            if(bestLocation != null) {
                Coords(bestLocation.latitude, bestLocation.longitude)
            } else {
                null
            }
        }
    }
}