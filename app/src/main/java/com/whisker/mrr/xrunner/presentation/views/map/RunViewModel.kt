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
                    private val getLastKnownLocationInteractor: GetLastKnownLocationInteractor,
                    private val getSongsInteractor: GetSongsInteractor,
                    private val setSongsInteractor: SetSongsInteractor,
                    private val playMusicInteractor: PlayMusicInteractor,
                    private val nextSongInteractor: NextSongInteractor,
                    private val previousSongInteractor: PreviousSongInteractor,
                    private val pauseMusicInteractor: PauseMusicInteractor,
                    private val stopMusicInteractor: StopMusicInteractor) : ViewModel() {

    private val lastKnownLocation = MutableLiveData<LatLng>()
    private val routeLive = MutableLiveData<RouteModel>()
    private val isTracking = MutableLiveData<Boolean>()
    private val finalRoute = MutableLiveData<RouteModel>()
    private val currentSong = MutableLiveData<String>()
    private val isMusicPlaying = MutableLiveData<Boolean>()

    private val disposables = CompositeDisposable()
    private val runnerTimer = RunnerTimer()
    private var route: RouteModel = RouteModel()

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
        route = RouteModel()
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

    fun getMusic() {
        disposables.add(
            getSongsInteractor.getSongs()
                .flatMapCompletable {
                    if(it.isNotEmpty()) {
                        currentSong.postValue(it[0].displayName)
                    }
                    setSongsInteractor.setSongs(it)
                }
                .subscribe({
                    isMusicPlaying.postValue(false)
                }, Throwable::printStackTrace)
        )
    }

    fun startPlayingMusic() {
        disposables.add(
            playMusicInteractor.playMusic()
                .subscribe({
                    isMusicPlaying.postValue(true)
                }, Throwable::printStackTrace)
        )
    }

    fun pausePlayingMusic() {
        disposables.add(
            pauseMusicInteractor.pauseMusic()
                .subscribe({
                    isMusicPlaying.postValue(false)
                }, Throwable::printStackTrace)
        )
    }

    fun nextSong() {
        disposables.add(
            nextSongInteractor.nextSong()
                .subscribe({
                    currentSong.postValue(it.displayName)
                }, Throwable::printStackTrace)
        )
    }

    fun previousSong() {
        disposables.add(
            previousSongInteractor.previousSong()
                .subscribe({
                    currentSong.postValue(it.displayName)
                }, Throwable::printStackTrace)
        )
    }

    fun stopMusic() {
        disposables.add(
            stopMusicInteractor.stopMusic()
                .subscribe({
                    isMusicPlaying.postValue(false)
                }, Throwable::printStackTrace)
        )
    }

    fun getLastKnownLocation() = lastKnownLocation
    fun getIsTracking() = isTracking
    fun getTime() = runnerTimer.getTime()
    fun getFinalRoute() = finalRoute
    fun getRoute() = routeLive
    fun getCurrentSong() = currentSong
    fun getIsMusicPlaying() = isMusicPlaying

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}