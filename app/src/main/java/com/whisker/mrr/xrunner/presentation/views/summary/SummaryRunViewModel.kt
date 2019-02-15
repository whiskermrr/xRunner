package com.whisker.mrr.xrunner.presentation.views.summary

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whisker.mrr.domain.common.ChallengeUtils
import com.whisker.mrr.domain.interactor.GetActiveChallengesInteractor
import com.whisker.mrr.domain.interactor.SaveRouteInteractor
import com.whisker.mrr.domain.interactor.SaveSnapshotInteractor
import com.whisker.mrr.domain.interactor.UpdateChallengesInteractor
import com.whisker.mrr.domain.model.RouteEntity
import com.whisker.mrr.xrunner.presentation.mapper.RouteMapper
import com.whisker.mrr.xrunner.presentation.model.Route
import com.whisker.mrr.xrunner.utils.toByteArray
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SummaryRunViewModel
@Inject constructor(
    private val saveRouteInteractor: SaveRouteInteractor,
    private val saveSnapshotInteractor: SaveSnapshotInteractor,
    private val getActiveChallengesInteractor: GetActiveChallengesInteractor,
    private val updateChallengesInteractor: UpdateChallengesInteractor
) : ViewModel() {

    private val disposables = CompositeDisposable()
    private val isRouteSaved =  MutableLiveData<Boolean> ()

    fun saveRoute(route: Route) {
        disposables.add(
            Single.create<RouteEntity> { emitter ->
                emitter.onSuccess(RouteMapper.routeToEntityTransform(route))
            }.flatMapCompletable {routeEntity ->
                saveRouteInteractor.saveRoute(routeEntity)
            }.subscribe {
                updateChallenges(route)
            }
        )
    }

    private fun updateChallenges(route: Route) {
        disposables.add(
            getActiveChallengesInteractor.getChallenges()
                .map { challenges ->
                    val routeEntity = RouteMapper.routeToEntityTransform(route)
                    return@map ChallengeUtils.updateChallengesProgress(routeEntity.routeStats, challenges)
                }.flatMapCompletable { updatedChallenges ->
                    updateChallengesInteractor.updateChallenges(updatedChallenges)
                }.subscribe {
                    isRouteSaved.postValue(true)
                }
        )
    }

    fun saveSnapshot(bitmap: Bitmap, fileName: String) : Completable {
        return saveSnapshotInteractor.saveSnapshot(bitmap.toByteArray(Bitmap.CompressFormat.JPEG), fileName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getIsRouteSaved() = isRouteSaved
}