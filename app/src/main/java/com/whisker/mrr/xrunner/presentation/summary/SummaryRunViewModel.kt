package com.whisker.mrr.xrunner.presentation.summary

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whisker.mrr.xrunner.domain.model.Route
import com.whisker.mrr.xrunner.domain.repository.RouteRepository
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SummaryRunViewModel
@Inject constructor(private val routeRepository: RouteRepository) : ViewModel() {

    private val disposables = CompositeDisposable()
    private val isRouteSaved =  MutableLiveData<Boolean> ();

    fun saveRoute(route: Route) {
        disposables.add(
            routeRepository.saveRoute(route)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    isRouteSaved.postValue(true)
                }
        )
    }

    fun saveSnapshot(bitmap: Bitmap) : Completable {
        return Completable.complete()
    }

    fun getIsRouteSaved() = isRouteSaved
}