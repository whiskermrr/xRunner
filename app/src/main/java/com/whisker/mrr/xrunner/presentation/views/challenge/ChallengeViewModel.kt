package com.whisker.mrr.xrunner.presentation.views.challenge

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whisker.mrr.domain.interactor.GetChallengesInteractor
import com.whisker.mrr.domain.interactor.SaveChallengeInteractor
import com.whisker.mrr.domain.model.Challenge
import com.whisker.mrr.xrunner.presentation.mapper.ChallengeMapper
import com.whisker.mrr.xrunner.presentation.model.ChallengeModel
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ChallengeViewModel
@Inject constructor(private val getChallengesInteractor: GetChallengesInteractor, saveChallengeInteractor: SaveChallengeInteractor)
: ViewModel() {

    private val challengeList = MutableLiveData<List<ChallengeModel>>()
    private val disposables = CompositeDisposable()

    init {
        getChallenges()
/*        val challenge = saveChallengeInteractor.saveChallenge(Challenge(title = "Best Challenge", distance = 10_000f))
        val challenge2 = saveChallengeInteractor.saveChallenge(Challenge(title = "Best Challenge2", distance = 10_000f, time = 60_000L))
        val challenge3 = saveChallengeInteractor.saveChallenge(Challenge(title = "Best Challenge3", distance = 10_000f, time = 60_000L))
        val challenge4 = saveChallengeInteractor.saveChallenge(Challenge(title = "Best Challenge4", time = 60_000L, speed = 5f))
        val challenge5 = saveChallengeInteractor.saveChallenge(Challenge(title = "Best Challenge5", distance = 10_000f, speed = 5f, time = 60_000L))
        val challenge6 = saveChallengeInteractor.saveChallenge(Challenge(title = "Best Challenge6", speed = 5f, time = 60_000L))

        disposables.add(
            Completable.concatArray(challenge, challenge2, challenge3, challenge4, challenge5, challenge6)
                .subscribe { Log.e("Mrr", "saved!") }
        )*/
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