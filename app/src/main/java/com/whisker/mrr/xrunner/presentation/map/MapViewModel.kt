package com.whisker.mrr.xrunner.presentation.map

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whisker.mrr.xrunner.domain.LocationRepository
import com.whisker.mrr.xrunner.domain.model.RoutePoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MapViewModel
@Inject constructor(private val locationRepository: LocationRepository) : ViewModel() {

    private val lastRoutePoint = MutableLiveData<RoutePoint>()
    private val disposables: CompositeDisposable = CompositeDisposable()

    fun startTracking() {
        disposables.add(
            locationRepository.startTracking()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    lastRoutePoint.postValue(it)
                }, {
                    it.printStackTrace()
                })
        )
    }

    fun getLastRoutePoint() = lastRoutePoint

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}