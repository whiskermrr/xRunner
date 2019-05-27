package com.whisker.mrr.xrunner.presentation.views.summary

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whisker.mrr.domain.common.ChallengeUtils
import com.whisker.mrr.domain.common.UserStatsUtils
import com.whisker.mrr.domain.interactor.*
import com.whisker.mrr.domain.model.Challenge
import com.whisker.mrr.domain.model.Route
import com.whisker.mrr.xrunner.presentation.mapper.RouteMapper
import com.whisker.mrr.xrunner.presentation.model.RouteModel
import com.whisker.mrr.xrunner.utils.toByteArray
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
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
    private val isRouteSaved =  MutableLiveData<Boolean>()
    private val isSnapshotSaved = MutableLiveData<Boolean>()

    fun saveRoute(route: RouteModel) {
        val routeEntity = RouteMapper.routeToEntityTransform(route)
        disposables.add(
            saveRouteInteractor.saveRoute(routeEntity)
                .andThen(updateChallenges(routeEntity))
                .subscribe({ isRouteSaved.postValue(true) }, Throwable::printStackTrace)
        )
    }

    private fun updateChallenges(route: Route) : Completable {
        return getActiveChallengesInteractor.getChallenges()
            .map { challenges ->
                return@map ChallengeUtils.updateChallengesProgress(route.routeStats, challenges)
            }.flatMapCompletable { updatedChallenges ->
                Completable.concatArray(
                    updateChallengesInteractor.updateChallenges(updatedChallenges),
                    updateUserStats(route, updatedChallenges)
                )
            }
    }

    private fun updateUserStats(route: Route, challenges: List<Challenge>) : Completable {
        return getUserStatsInteractor.getUserStats()
                .flatMapCompletable { userStats ->
                    UserStatsUtils.updateUserStats(userStats, route.routeStats)
                    for(challenge in challenges.filter { it.isFinished }) {
                        userStats.experience += challenge.experience
                    }
                    updateUserStatsInteractor.updateUserStats(userStats)
                }
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
}