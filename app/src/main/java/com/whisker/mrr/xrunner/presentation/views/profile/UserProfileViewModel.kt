package com.whisker.mrr.xrunner.presentation.views.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whisker.mrr.domain.interactor.GetUserStatsInteractor
import com.whisker.mrr.xrunner.presentation.mapper.UserStatsMapper
import com.whisker.mrr.xrunner.presentation.model.UserStatsModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class UserProfileViewModel @Inject constructor(private val getUserStatsInteractor: GetUserStatsInteractor) : ViewModel() {

    private val disposables = CompositeDisposable()
    private val userStats =  MutableLiveData<UserStatsModel> ()

    init {
        getUserStats()
    }

    private fun getUserStats() {
        disposables.add(
            getUserStatsInteractor.getUserStats()
                .map {  statsEntity ->
                    UserStatsMapper.transformUserStats(statsEntity)
                }
                .subscribe( { stats ->
                    userStats.postValue(stats)
                }, { error ->
                    error.printStackTrace()
                })
        )
    }

    fun userStats() = userStats
}