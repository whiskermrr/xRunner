package com.whisker.mrr.xrunner.presentation.map

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.whisker.mrr.xrunner.domain.model.RouteStats
import com.whisker.mrr.xrunner.domain.repository.LocationRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MapViewModel
@Inject constructor(private val locationRepository: LocationRepository) : ViewModel() {

    private val routePoints = MutableLiveData<List<LatLng>>()
    private val lastKnownLocation = MutableLiveData<LatLng>()
    private val routeStats = MutableLiveData<RouteStats>()

    private val disposables: CompositeDisposable = CompositeDisposable()
    private var startTime: Long = 0
    private var endTime: Long = 0

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
        startTime = System.currentTimeMillis()
        disposables.add(
            locationRepository.startTracking()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val points = routePoints.value?.toMutableList() ?: arrayListOf()
                    points.add(it)
                    routePoints.postValue(points)
                }, {
                    it.printStackTrace()
                })
        )
    }

    fun stopTracking() {
        endTime = System.currentTimeMillis()
    }

    fun getRoutePoints() = routePoints
    fun getLastKnownLocation() = lastKnownLocation

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}