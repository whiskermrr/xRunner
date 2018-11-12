package com.whisker.mrr.xrunner.presentation.map

import android.os.SystemClock
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.whisker.mrr.xrunner.domain.model.Route
import com.whisker.mrr.xrunner.domain.model.RouteStats
import com.whisker.mrr.xrunner.domain.repository.LocationRepository
import com.whisker.mrr.xrunner.utils.LocationUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.timerTask

class MapViewModel
@Inject constructor(private val locationRepository: LocationRepository) : ViewModel() {

    private val routePoints = MutableLiveData<List<LatLng>>()
    private val lastKnownLocation = MutableLiveData<LatLng>()
    private val routeStats = MutableLiveData<RouteStats>()
    private val isTracking = MutableLiveData<Boolean>()
    private val runTime = MutableLiveData<String>()

    private val disposables: CompositeDisposable = CompositeDisposable()
    private lateinit var timer: Timer
    private var startTime: Long = 0L
    private var pauseTime: Long = 0L
    private var elapsedTime: Long  = 0L

    fun onMapShown() {
        disposables.add(
            locationRepository.getLastKnownLocation()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    lastKnownLocation.postValue(it)
                }, {
                    it.printStackTrace()
                })
        )
    }

    private fun startTimer() {
        timer = Timer()
        timer.scheduleAtFixedRate(timerTask {
            elapsedTime = SystemClock.elapsedRealtime() - startTime
            val seconds = elapsedTime / 1000 % 60
            val minutes = elapsedTime / (1000 * 60) % 60
            val hours = elapsedTime / (1000 * 60 * 60) % 24

            val stringTime = if(hours == 0L) {
                String.format("%02d:%02d", minutes, seconds)
            } else {
                String.format("%02d:%02d:%02d", hours, minutes, seconds)
            }
            runTime.postValue(stringTime)
        }, 1000, 1000)
    }

    fun startTracking() {
        startTime = SystemClock.elapsedRealtime()
        isTracking.postValue(true)
        routePoints.value = listOf()
        routeStats.value = RouteStats()
        startTimer()

        disposables.add(
            locationRepository.startTracking()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val points = routePoints.value?.toMutableList() ?: arrayListOf()
                    routeStats.postValue(LocationUtils.calculateRouteStats(
                        routeStats = routeStats.value ?: RouteStats(),
                        firstCoords = if(!points.isEmpty()) {
                            points.last()
                        } else {
                            it
                        },
                        secondCoords = it,
                        time = SystemClock.elapsedRealtime() - startTime
                    ))
                    points.add(it)
                    routePoints.postValue(points)
                }, {
                    it.printStackTrace()
                })
        )
    }

    fun pauseTracking() {
        timer.cancel()
        pauseTime = SystemClock.elapsedRealtime()
        locationRepository.pauseTracking()
    }

    fun resumeTracking() {
        startTime = startTime + SystemClock.elapsedRealtime() - pauseTime
        startTimer()
        locationRepository.resumeTracking()
    }

    fun stopTracking() {
        isTracking.postValue(false)
        if(routePoints.value != null && routeStats.value != null) {
            if(routeStats.value!!.wgs84distance == 0f) return
                calculateFinalStats()
                saveStats()
        }
    }

    private fun saveStats() {
        disposables.add(
            locationRepository.stopTracking(Route(startTime.toString(), routePoints.value!!, routeStats.value!!))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                }, {
                    it.printStackTrace()
                })
        )
    }

    private fun calculateFinalStats() {
        val stats = routeStats.value!!
        LocationUtils.calculateRouteAverageSpeedAndPeace(stats, elapsedTime)
        LocationUtils.calculateRouteTime(stats, elapsedTime)
        routeStats.postValue(stats)
    }

    fun getRoutePoints() = routePoints
    fun getLastKnownLocation() = lastKnownLocation
    fun getRouteStats() = routeStats
    fun getIsTracking() = isTracking
    fun getTime() = runTime

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}