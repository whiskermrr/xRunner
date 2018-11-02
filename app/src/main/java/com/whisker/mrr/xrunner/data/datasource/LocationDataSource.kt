package com.whisker.mrr.xrunner.data.datasource

import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import com.whisker.mrr.xrunner.domain.bus.RxBus
import com.whisker.mrr.xrunner.domain.bus.event.LocationEvent
import com.whisker.mrr.xrunner.infrastructure.LocationService
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class LocationDataSource
@Inject constructor(
    private val context: Context
) {

    companion object {
        var routeIndex: Int = 0
    }

    private lateinit var locationSubject: PublishSubject<Location>

    fun startTracking() : Flowable<Location> {
        startLocationService()
        locationSubject = PublishSubject.create()
        subscribeToLocationEvents()
        return locationSubject.toFlowable(BackpressureStrategy.LATEST)
    }

    private fun startLocationService() {
        val intent = Intent(LocationService.ACTION_START_TRACKING)
        intent.setPackage(context.packageName)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }

    private fun subscribeToLocationEvents() {
        RxBus.subscribe(LocationEvent::class.java.name, this, Consumer { event ->
            if(event is LocationEvent) {
                locationSubject.onNext(event.location)
                routeIndex++
            }
        })
    }

    fun stopTracking() {
        RxBus.unsubscribe(this)
        routeIndex = 0
    }
}