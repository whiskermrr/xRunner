package com.whisker.mrr.xrunner.presentation.views.challenge

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whisker.mrr.domain.interactor.GetChallengesInteractor
import com.whisker.mrr.xrunner.presentation.mapper.ChallengeMapper
import com.whisker.mrr.xrunner.presentation.model.ChallengeHolder
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ChallengeViewModel
@Inject constructor(private val getChallengesInteractor: GetChallengesInteractor)
: ViewModel() {

    private val challengeList = MutableLiveData<ChallengeHolder>()
    private val disposables = CompositeDisposable()

    init {
        getChallenges()
    }

    private fun getChallenges() {
        disposables.add(
            getChallengesInteractor.getChallenges()
                .map {
                    ChallengeMapper.transformList(it)
                }
                .subscribe({ challenges ->
                    val challengeHolder = ChallengeHolder(
                        challenges.filter { !it.isFinished },
                        challenges.filter { it.isFinished }
                    )
                    challengeList.postValue(challengeHolder)
                }, { error ->
                    error.printStackTrace()
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    fun getChallengeList() = challengeList
}