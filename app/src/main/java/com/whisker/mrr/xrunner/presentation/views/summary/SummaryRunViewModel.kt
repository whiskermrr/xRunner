package com.whisker.mrr.xrunner.presentation.views.summary

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whisker.mrr.domain.interactor.*
import com.whisker.mrr.domain.model.Challenge
import com.whisker.mrr.domain.model.Route
import com.whisker.mrr.xrunner.presentation.mapper.RouteMapper
import com.whisker.mrr.xrunner.presentation.model.RouteModel
import com.whisker.mrr.xrunner.utils.toByteArray
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class SummaryRunViewModel
@Inject constructor(
    private val saveRouteInteractor: SaveRouteInteractor,
    private val saveSnapshotInteractor: SaveSnapshotInteractor,
    private val updateChallengesInteractor: UpdateChallengesInteractor,
    private val updateUserStatsInteractor: UpdateUserStatsInteractor
) : ViewModel() {

    private val disposables = CompositeDisposable()
    private val isRouteSaved =  MutableLiveData<Boolean>()
    private val isSnapshotSaved = MutableLiveData<Boolean>()
    private val isChallengesUpdated = MutableLiveData<Boolean>()
    private val isUserStatsUpdated = MutableLiveData<Boolean>()

    fun saveRoute(route: RouteModel) {
        val routeEntity = RouteMapper.routeToEntityTransform(route)
        disposables.add(
            saveRouteInteractor.saveRoute(routeEntity)
                .subscribe({
                    isRouteSaved.postValue(true)
                    updateChallenges(routeEntity)
                }, Throwable::printStackTrace)
        )
    }

    private fun updateChallenges(route: Route) {
        disposables.add(
            updateChallengesInteractor.updateChallenges(route.routeStats)
                .subscribe({ updatedChallenges ->
                    isChallengesUpdated.postValue(true)
                    updateUserStats(route, updatedChallenges)
                }, Throwable::printStackTrace)
        )
    }

    private fun updateUserStats(route: Route, challenges: List<Challenge>) {
        disposables.add(
            updateUserStatsInteractor.updateUserStats(route.routeStats, challenges)
                .subscribe({ isUserStatsUpdated.postValue(true) }, Throwable::printStackTrace)
        )
    }

    fun saveSnapshot(bitmap: Bitmap, fileName: String) {
        disposables.add(
            saveSnapshotInteractor.saveSnapshot(bitmap.toByteArray(Bitmap.CompressFormat.JPEG), fileName)
                .subscribe({
                    isSnapshotSaved.postValue(true)
                }, Throwable::printStackTrace)
        )
    }

    fun getIsRouteSaved() = isRouteSaved
    fun getIsSnapshotSaved() = isSnapshotSaved
    fun getIsChallengesUpdated() = isChallengesUpdated
    fun getIsUserStatsUpdated() = isUserStatsUpdated
}