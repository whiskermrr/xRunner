package com.whisker.mrr.xrunner.presentation.mapper

import com.google.android.gms.maps.model.LatLng
import com.whisker.mrr.domain.model.Coords

class LatLngMapper {

    companion object {
        fun latLngToCoordsTransform(points: List<LatLng>) : List<Coords> {
            val returnList = mutableListOf<Coords>()
            for(point in points) {
                returnList.add(Coords(point.latitude, point.longitude))
            }
            return returnList
        }

        fun coordsToLatLngTransform(points: List<Coords>) : MutableList<LatLng> {
            val returnList = mutableListOf<LatLng>()
            for(point in points) {
                returnList.add(LatLng(point.latitude, point.longitude))
            }
            return returnList
        }

        fun coordsToLatLngTransform(point: Coords) : LatLng {
            return LatLng(point.latitude, point.longitude)
        }
    }
}