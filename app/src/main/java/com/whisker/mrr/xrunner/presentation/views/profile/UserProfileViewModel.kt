package com.whisker.mrr.xrunner.presentation.views.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whisker.mrr.xrunner.domain.interactor.GetUserStatsInteractor
import com.whisker.mrr.xrunner.domain.model.UserStatsEntity
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class UserProfileViewModel @Inject constructor(private val getUserStatsInteractor: GetUserStatsInteractor) : ViewModel() {

    private val disposables = CompositeDisposable()
    private val userStats =  MutableLiveData<UserStatsEntity> ()

    init {
        disposables.add(
            getUserStatsInteractor.getUserStats()
                .subscribe( { stats ->
                        userStats.postValue(stats)
                    }, { error ->
                        error.printStackTrace()
                    }
                )
        )
    }

    fun userStats() = userStats
}