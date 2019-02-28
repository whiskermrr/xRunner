package com.whisker.mrr.xrunner.presentation.views.map

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.whisker.mrr.domain.common.DomainConstants.EEE_MMM_d_yyyy
import com.whisker.mrr.domain.common.formatDate
import com.whisker.mrr.domain.interactor.*
import com.whisker.mrr.xrunner.presentation.mapper.LatLngMapper
import com.whisker.mrr.xrunner.presentation.model.RouteModel
import com.whisker.mrr.xrunner.presentation.model.RouteStatsModel
import com.whisker.mrr.xrunner.utils.LocationUtils
import com.whisker.mrr.xrunner.utils.RunnerTimer
import io.reactivex.disposables.CompositeDisposable
import java.util.*
import javax.inject.Inject

class RunViewModel
@Inject constructor(private val startTrackingInteractor: StartTrackingInteractor,
                    private val pauseTrackingInteractor: PauseTrackingInteractor,
                    private val resumeTrackingInteractor: ResumeTrackingInteractor,
                    private val stopTrackingInteractor: StopTrackingInteractor,
                    private val getLastKnownLocationInteractor: GetLastKnownLocationInteractor) : ViewModel() {

    private val lastKnownLocation = MutableLiveData<LatLng>()
    private val routeLive = MutableLiveData<RouteModel>()
    private val isTracking = MutableLiveData<Boolean>()
    private val finalRoute = MutableLiveData<RouteModel>()

    private val disposables = CompositeDisposable()
    private val runnerTimer = RunnerTimer()
    private val route: RouteModel = RouteModel()

    fun onMapShown() {
        disposables.add(
            getLastKnownLocationInteractor.maybe()
                .map {
                    LatLngMapper.coordsToLatLngTransform(it)
                }
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
        routeLive.postValue(route)

        disposables.add(
            startTrackingInteractor.flowable()
                .map {
                    LatLngMapper.coordsToLatLngTransform(it)
                }
                .map {
                    route.routeStats = calculateStats(it)
                    route.waypoints.add(it)
                }
                .subscribe({
                    routeLive.postValue(route)
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
        if(route.routeStats.wgs84distance > 0) {
            calculateFinalStats()
            route.name = Date(route.date).formatDate(EEE_MMM_d_yyyy)
            finalRoute.postValue(route)
        }
    }

    private fun calculateStats(latLng: LatLng) : RouteStatsModel {
        return LocationUtils.calculateRouteStats(
                    routeStats = route.routeStats,
                    firstCoords = if(!route.waypoints.isEmpty()) {
                        route.waypoints.last()
                    } else {
                        latLng
                    },
                    secondCoords = latLng,
                    time = runnerTimer.getElapsedTime()
                )
    }

    private fun calculateFinalStats() {
        LocationUtils.calculateRouteAverageSpeedAndPeace(route.routeStats, runnerTimer.getElapsedTime())
        LocationUtils.calculateRouteTime(route.routeStats, runnerTimer.getElapsedTime())
        routeLive.postValue(route)
    }

    fun getLastKnownLocation() = lastKnownLocation
    fun getIsTracking() = isTracking
    fun getTime() = runnerTimer.getTime()
    fun getFinalRoute() = finalRoute
    fun getRoute() = routeLive

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}