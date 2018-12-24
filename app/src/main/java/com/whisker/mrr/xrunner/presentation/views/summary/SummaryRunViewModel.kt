package com.whisker.mrr.xrunner.presentation.views.summary

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whisker.mrr.xrunner.domain.interactor.SaveRouteInteractor
import com.whisker.mrr.xrunner.domain.interactor.SaveSnapshotInteractor
import com.whisker.mrr.xrunner.presentation.mapper.RouteMapper
import com.whisker.mrr.xrunner.presentation.model.Route
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SummaryRunViewModel
@Inject constructor(private val saveRouteInteractor: SaveRouteInteractor,
                    private val saveSnapshotInteractor: SaveSnapshotInteractor) : ViewModel() {

    private val disposables = CompositeDisposable()
    private val isRouteSaved =  MutableLiveData<Boolean> ()

    fun saveRoute(route: Route) {
        disposables.add(
            saveRouteInteractor.saveRoute(RouteMapper.routeToEntityTransform(route))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    isRouteSaved.postValue(true)
                }
        )
    }

    fun saveSnapshot(bitmap: Bitmap, fileName: String) : Completable {
        return saveSnapshotInteractor.saveSnapshot(bitmap, fileName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getIsRouteSaved() = isRouteSaved
}