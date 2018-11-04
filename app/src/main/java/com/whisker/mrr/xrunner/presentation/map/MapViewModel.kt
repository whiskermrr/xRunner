package com.whisker.mrr.xrunner.presentation.map

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.whisker.mrr.xrunner.domain.LocationRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MapViewModel
@Inject constructor(private val locationRepository: LocationRepository) : ViewModel() {

    private val routePoints = MutableLiveData<List<LatLng>>()
    private val lastKnownLocation = MutableLiveData<LatLng>()
    private val disposables: CompositeDisposable = CompositeDisposable()

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

    fun getRoutePoints() = routePoints
    fun getLastKnownLocation() = lastKnownLocation

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}