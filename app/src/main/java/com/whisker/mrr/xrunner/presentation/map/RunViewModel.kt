package com.whisker.mrr.xrunner.presentation.map

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.whisker.mrr.xrunner.domain.interactor.*
import com.whisker.mrr.xrunner.presentation.model.Route
import com.whisker.mrr.xrunner.presentation.model.RouteStats
import com.whisker.mrr.xrunner.utils.DateUtils
import com.whisker.mrr.xrunner.utils.LocationUtils
import com.whisker.mrr.xrunner.utils.RunnerTimer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RunViewModel
@Inject constructor(private val startTrackingInteractor: StartTrackingInteractor,
                    private val pauseTrackingInteractor: PauseTrackingInteractor,
                    private val resumeTrackingInteractor: ResumeTrackingInteractor,
                    private val stopTrackingInteractor: StopTrackingInteractor,
                    private val getLastKnownLocationInteractor: GetLastKnownLocationInteractor) : ViewModel() {

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
            getLastKnownLocationInteractor.single()
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
            startTrackingInteractor.flowable()
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
        pauseTrackingInteractor.execute()
    }

    fun resumeTracking() {
        runnerTimer.resume()
        resumeTrackingInteractor.execute()
    }

    fun stopTracking() {
        runnerTimer.stop()
        isTracking.postValue(false)
        stopTrackingInteractor.execute()
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