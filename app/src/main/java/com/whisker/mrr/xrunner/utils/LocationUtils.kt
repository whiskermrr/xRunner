package com.whisker.mrr.xrunner.utils

import android.location.Location
import com.google.android.gms.maps.model.LatLng

class LocationUtils {

    companion object {
        fun calculateDistance(firstCoords: LatLng, secondCoords: LatLng) : Float {
            val firstLocation = Location("A")
            val secondLocation = Location("B")

            firstLocation.latitude = firstCoords.latitude
            firstLocation.longitude = firstCoords.longitude

            secondLocation.latitude = secondCoords.latitude
            secondLocation.longitude = secondCoords.longitude

            return firstLocation.distanceTo(secondLocation)
        }
    }
}