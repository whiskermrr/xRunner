package com.whisker.mrr.xrunner.presentation.views.challenge

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whisker.mrr.domain.interactor.GetChallengesInteractor
import com.whisker.mrr.domain.model.Challenge
import io.reactivex.disposables.CompositeDisposable

class ChallengeViewModel(private val getChallengesInteractor: GetChallengesInteractor)
: ViewModel() {

    private val challengeList = MutableLiveData<List<Challenge>>()
    private val disposables = CompositeDisposable()

    init {
        getChallenges()
    }

    private fun getChallenges() {
        disposables.add(
            getChallengesInteractor.GetChallenges()
                .subscribe({ challenges ->
                    challengeList.postValue(challenges)
                }, { error ->
                    error.printStackTrace()
                })
        )
    }

    fun getChallengeList() = challengeList
}