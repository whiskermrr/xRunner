package com.whisker.mrr.xrunner.presentation.views.map

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

    private val lastKnownLocation = MutableLiveData<LatLng>()
    private val routeLive = MutableLiveData<Route>()
    private val isTracking = MutableLiveData<Boolean>()
    private val finalRoute = MutableLiveData<Route>()

    private val disposables = CompositeDisposable()
    private val runnerTimer = RunnerTimer()
    private val route: Route = Route()

    fun onMapShown() {
        disposables.add(
            getLastKnownLocationInteractor.single()
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
            route.name = DateUtils.formatDate(route.date, DateUtils.EEE_MMM_d_yyyy)
            finalRoute.postValue(route)
        }
    }

    private fun calculateStats(latLng: LatLng) : RouteStats {
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