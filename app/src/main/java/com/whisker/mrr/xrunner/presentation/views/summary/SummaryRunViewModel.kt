package com.whisker.mrr.xrunner.presentation.views.summary

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whisker.mrr.domain.common.ChallengeUtils
import com.whisker.mrr.domain.common.UserStatsUtils
import com.whisker.mrr.domain.interactor.*
import com.whisker.mrr.domain.model.Challenge
import com.whisker.mrr.domain.model.RouteEntity
import com.whisker.mrr.xrunner.presentation.mapper.RouteMapper
import com.whisker.mrr.xrunner.presentation.model.Route
import com.whisker.mrr.xrunner.utils.toByteArray
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SummaryRunViewModel
@Inject constructor(
    private val saveRouteInteractor: SaveRouteInteractor,
    private val saveSnapshotInteractor: SaveSnapshotInteractor,
    private val getActiveChallengesInteractor: GetActiveChallengesInteractor,
    private val updateChallengesInteractor: UpdateChallengesInteractor,
    private val getUserStatsInteractor: GetUserStatsInteractor,
    private val updateUserStatsInteractor: UpdateUserStatsInteractor
) : ViewModel() {

    private val disposables = CompositeDisposable()
    private val isRouteSaved =  MutableLiveData<Boolean> ()

    fun saveRoute(route: Route) {
        val routeEntity = RouteMapper.routeToEntityTransform(route)
        disposables.add(
            saveRouteInteractor.saveRoute(routeEntity)
                .subscribe {
                    updateChallenges(routeEntity)
                }
        )
    }

    private fun updateChallenges(route: RouteEntity) {
        disposables.add(
            getActiveChallengesInteractor.getChallenges()
                .map { challenges ->
                    return@map ChallengeUtils.updateChallengesProgress(route.routeStats, challenges)
                }.flatMapCompletable { updatedChallenges ->
                    Completable.concatArray(
                        updateChallengesInteractor.updateChallenges(updatedChallenges),
                        updateUserStats(route, updatedChallenges)
                    )
                }.subscribe {
                    isRouteSaved.postValue(true)
                }
        )
    }

    private fun updateUserStats(route: RouteEntity, challenges: List<Challenge>) : Completable {
        return getUserStatsInteractor.getUserStats()
                .flatMapCompletable { userStats ->
                    UserStatsUtils.updateUserStats(userStats, route.routeStats)
                    for(challenge in challenges.filter { it.isFinished }) {
                        userStats.experience += challenge.experience
                    }
                    updateUserStatsInteractor.updateUserStats(userStats)
                }
    }

    fun saveSnapshot(bitmap: Bitmap, fileName: String) : Completable {
        return saveSnapshotInteractor.saveSnapshot(bitmap.toByteArray(Bitmap.CompressFormat.JPEG), fileName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getIsRouteSaved() = isRouteSaved
}