package com.whisker.mrr.xrunner.presentation.views.challenge

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whisker.mrr.domain.interactor.GetChallengesInteractor
import com.whisker.mrr.domain.interactor.SaveChallengeInteractor
import com.whisker.mrr.xrunner.presentation.mapper.ChallengeMapper
import com.whisker.mrr.xrunner.presentation.model.ChallengeModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ChallengeViewModel
@Inject constructor(private val getChallengesInteractor: GetChallengesInteractor, saveChallengeInteractor: SaveChallengeInteractor)
: ViewModel() {

    private val challengeList = MutableLiveData<List<ChallengeModel>>()
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
                    challengeList.postValue(challenges)
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