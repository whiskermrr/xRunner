package com.whisker.mrr.xrunner.data.datasource

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.whisker.mrr.xrunner.domain.bus.RxBus
import com.whisker.mrr.xrunner.domain.bus.event.LocationEvent
import com.whisker.mrr.xrunner.domain.model.Route
import com.whisker.mrr.xrunner.domain.model.RoutePoint
import com.whisker.mrr.xrunner.infrastructure.LocationService
import durdinapps.rxfirebase2.DataSnapshotMapper
import durdinapps.rxfirebase2.RxFirebaseDatabase
import io.reactivex.Flowable
import io.reactivex.functions.Consumer
import javax.inject.Inject

class LocationDataSource
@Inject constructor(
    private val context: Context,
    private val firebaseDatabase: FirebaseDatabase
) {

    companion object {
        var routeIndex: Int = 0
    }

    lateinit var mReference: DatabaseReference

    fun startTracking(userId: String, routeName: String) : Flowable<RoutePoint> {
        startLocationService()
        firebaseDatabase.reference.child("Users").child(userId).child(routeName).setValue(Route(routeName))
        mReference = firebaseDatabase.reference.child("Users").child(userId).child(routeName).child("waypoints")

        subscribeToLocationEvents()

        return RxFirebaseDatabase.observeChildEvent(mReference, RoutePoint::class.java)
            .map {
                it.value
            }
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
                val routePoint = RoutePoint(event.location.latitude, event.location.longitude)
                mReference.child(routeIndex.toString()).setValue(routePoint)
                routeIndex++
            }
        })
    }

    fun stopTracking() {
        RxBus.unsubscribe(this)
        routeIndex = 0
    }
}