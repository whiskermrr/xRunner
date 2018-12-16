package com.whisker.mrr.xrunner.presentation.map

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.whisker.mrr.xrunner.domain.model.Route
import com.whisker.mrr.xrunner.domain.model.RouteStats
import com.whisker.mrr.xrunner.domain.repository.LocationRepository
import com.whisker.mrr.xrunner.utils.DateUtils
import com.whisker.mrr.xrunner.utils.LocationUtils
import com.whisker.mrr.xrunner.utils.RunnerTimer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MapViewModel
@Inject constructor(private val locationRepository: LocationRepository) : ViewModel() {

    private val routePoints = MutableLiveData<List<LatLng>>()
    private val lastKnownLocation = MutableLiveData<LatLng>()
    private val routeStats = MutableLiveData<RouteStats>()
    private val isTracking = MutableLiveData<Boolean>()
    private val finalRoute = MutableLiveData<Route>()

    private val disposables = CompositeDisposable()
    private val runnerTimer = RunnerTimer()
    private val route: Route = Route()

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
    
    fun startTracking() {
        route.date = System.currentTimeMillis()
        runnerTimer.startTimer()
        isTracking.postValue(true)
        routePoints.value = listOf()
        routeStats.value = RouteStats()

        disposables.add(
            locationRepository.startTracking()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val points = routePoints.value?.toMutableList() ?: arrayListOf()
                    routeStats.postValue(calculateStats(points, it))
                    points.add(it)
                    routePoints.postValue(points)
                }, {
                    it.printStackTrace()
                })
        )
    }

    fun pauseTracking() {
        runnerTimer.pause()
        locationRepository.pauseTracking()
    }

    fun resumeTracking() {
        runnerTimer.resume()
        locationRepository.resumeTracking()
    }

    fun stopTracking() {
        runnerTimer.stop()
        isTracking.postValue(false)
        locationRepository.stopTracking()
        if(routePoints.value != null && routeStats.value != null) {
            if(routeStats.value!!.wgs84distance == 0f) {
                return
            }
            calculateFinalStats()
            route.name = DateUtils.formatDate(route.date, DateUtils.EEE_MMM_d_yyyy)
            route.routeStats = routeStats.value!!
            route.waypoints = routePoints.value!!
            finalRoute.postValue(route)
        }
    }

    private fun calculateStats(points: List<LatLng>, latLng: LatLng) : RouteStats {
        return LocationUtils.calculateRouteStats(
                    routeStats = routeStats.value ?: RouteStats(),
                    firstCoords = if(!points.isEmpty()) {
                        points.last()
                    } else {
                        latLng
                    },
                    secondCoords = latLng,
                    time = runnerTimer.getElapsedTime()
                )
    }

    private fun calculateFinalStats() {
        val stats = routeStats.value!!
        LocationUtils.calculateRouteAverageSpeedAndPeace(stats, runnerTimer.getElapsedTime())
        LocationUtils.calculateRouteTime(stats, runnerTimer.getElapsedTime())
        routeStats.postValue(stats)
    }

    fun getRoutePoints() = routePoints
    fun getLastKnownLocation() = lastKnownLocation
    fun getRouteStats() = routeStats
    fun getIsTracking() = isTracking
    fun getTime() = runnerTimer.getTime()
    fun getFinalRoute() = finalRoute

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}