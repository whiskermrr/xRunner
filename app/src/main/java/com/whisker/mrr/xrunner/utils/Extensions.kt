package com.whisker.mrr.xrunner.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.location.Location
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.pow

fun Any.TAG() = javaClass.simpleName

fun ImageView.loadSnapshot(fileName: String) {
    if(context.fileExists(fileName)) {
        val image = context.getFile(fileName)
        Picasso.get().load(image).into(this)
    } else {
        val imageReference = FirebaseStorage.getInstance().reference.child("snapshots/$fileName")
        imageReference.downloadUrl.addOnSuccessListener {
            Picasso.get().load(it).into(this)
        }
    }
}

fun Bitmap.toByteArray(format: Bitmap.CompressFormat) : ByteArray {
    val byteArray = ByteArrayOutputStream()
    this.compress(format, 100, byteArray)
    return byteArray.toByteArray()
}

fun Context.saveFile(data: ByteArray?, fileName: String) {
    val outputStream: FileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE)
    outputStream.write(data)
    outputStream.flush()
    outputStream.close()
}
fun Context.getFile(fileName: String) : File {
    val filePath = getFileStreamPath(fileName).absolutePath
    return File(filePath)
}

fun Context.fileExists(fileName: String) : Boolean {
    val file = getFileStreamPath(fileName)
    return file.exists()
}

fun Activity.getScreenWidth() : Int {
    val size = Point()
    windowManager.defaultDisplay.getSize(size)
    return size.x
}

fun GoogleMap.calculateZoom(points: List<LatLng>, screenWidth: Int) {
    var minLat = points[0].latitude
    var maxLat = points[0].latitude
    var minLng = points[0].longitude
    var maxLng = points[0].longitude

    for(point in points) {
        if(point.latitude > maxLat) {
            maxLat = point.latitude
        } else if(point.latitude < minLat) {
            minLat = point.latitude
        }

        if(point.longitude > maxLng) {
            maxLng = point.longitude
        } else if(point.longitude < minLng) {
            minLng = point.longitude
        }
    }

    val minLocation = Location("A")
    val maxLocation = Location("B")

    minLocation.latitude = minLat
    minLocation.longitude = minLng

    maxLocation.latitude = maxLat
    maxLocation.longitude = maxLng

    val centerOfRoute = this.calculateMiddleLatLng(minLocation, maxLocation)
    val longestDistance = minLocation.distanceTo(maxLocation)
    val zoom = this.getZoomBasedOnDistance(longestDistance, screenWidth)
    animateCamera(CameraUpdateFactory.newLatLngZoom(centerOfRoute, zoom))
}

fun GoogleMap.calculateMiddleLatLng(firstLocation: Location, secondLocation: Location) : LatLng {
    val averageLat = (firstLocation.latitude + secondLocation.latitude) / 2
    val averageLng =(firstLocation.longitude + secondLocation.longitude) / 2

    return LatLng(averageLat, averageLng)
}

fun GoogleMap.getZoomBasedOnDistance(distance: Float, screenWidth: Int) : Float {
    var metersPerPixel = EQUATOR_LENGTH_IN_METERS / (EQUATOR_LENGTH_IN_PIXELS * 2.0.pow(MAX_ZOOM))
    var currentZoom = MAX_ZOOM
    var visibleDistance: Double = metersPerPixel * screenWidth

    while(visibleDistance < distance) {
        metersPerPixel *= 2
        visibleDistance = metersPerPixel * screenWidth
        currentZoom--
    }

    val ratio = (distance - visibleDistance / 2) / (visibleDistance / 2)
    val zoomRatio = currentZoom - 1.1 - ratio

    return zoomRatio.toFloat()
}

private val GoogleMap.MAX_ZOOM: Int
    get() = 19

private val GoogleMap.EQUATOR_LENGTH_IN_PIXELS: Int
    get() = 256

private val GoogleMap.EQUATOR_LENGTH_IN_METERS: Double
    get() = 40_075_004.0

fun TextView.setTextAndVisibility(newText: String?) {
    if(newText != null) {
        this.text = newText
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

fun TextView.setTextAndVisibility(format: String, newText: String?) {
    if(newText != null) {
        this.text = String.format(Locale.getDefault(), format, newText)
    } else {
        this.visibility = View.GONE
    }
}

fun Float.toDistance() : HashMap<String, Int> {
    val distance: HashMap<String, Int> = hashMapOf()
    val kilometers: Int = (this / 1000).toInt()
    val meters: Int = (this % 1000).toInt() / 10
    distance[Float.KILOMETERS] = kilometers
    distance[Float.METERS] = meters
    return distance
}

val Float.Companion.KILOMETERS: String
    get() = "FLOAT_DISTANCE_KILOMETERS"

val Float.Companion.METERS: String
    get() = "FLOAT_DISTANCE_METERS"
