package com.whisker.mrr.xrunner.domain.mapper

import com.google.android.gms.maps.model.LatLng
import com.whisker.mrr.xrunner.domain.model.Coords

class LatLngMapper {

    companion object {
        fun latLngToCoordsTransform(points: List<LatLng>) : List<Coords> {
            val returnList = mutableListOf<Coords>()
            for(point in points) {
                returnList.add(Coords(point.latitude, point.longitude))
            }
            return returnList
        }

        fun coordsToLatLngTransform(points: List<Coords>) : List<LatLng> {
            val returnList = mutableListOf<LatLng>()
            for(point in points) {
                returnList.add(LatLng(point.latitude, point.longitude))
            }
            return returnList
        }
    }
}