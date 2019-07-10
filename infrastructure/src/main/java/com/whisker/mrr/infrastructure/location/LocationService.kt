package com.whisker.mrr.infrastructure.location

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.*
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.whisker.mrr.domain.common.bus.RxBus

class LocationService : Service(), LocationListener {
    companion object {
        const val HANDLER_THREAD_NAME: String = "LocationThread"
        const val CHANNEL_ID: String = "channel_01"
        const val NOTIFICATION_TITLE: String = "Readable title"
        const val REQUIRED_ACCURACY: Int = 50
    }

    private lateinit var looper: Looper
    private lateinit var locationManager: LocationManager
    private val binder = LocationBinder()

    override fun onBind(p0: Intent?): IBinder? {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                NOTIFICATION_TITLE,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(channel)

            val notification = NotificationCompat.Builder(this,
                CHANNEL_ID
            )
            .setContentTitle("")
            .setContentText("")
            .build()

            startForeground(1, notification)
        }

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val thread = HandlerThread(HANDLER_THREAD_NAME)
        thread.start()
        looper = thread.looper
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 2f, this, looper)
        }
    }

    @SuppressLint("MissingPermission")
    fun stopLocationUpdates() {
        locationManager.removeUpdates(this)
    }

    override fun onLocationChanged(newLocation: Location?) {
        if(newLocation != null && newLocation.accuracy < REQUIRED_ACCURACY) {
            RxBus.publish(LocationEvent(newLocation))
        }
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {}

    override fun onProviderEnabled(p0: String?) {}

    override fun onProviderDisabled(p0: String?) {}

    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdates()
        looper.quit()
    }

    inner class LocationBinder : Binder() {
        fun getService() : LocationService = this@LocationService
    }
}