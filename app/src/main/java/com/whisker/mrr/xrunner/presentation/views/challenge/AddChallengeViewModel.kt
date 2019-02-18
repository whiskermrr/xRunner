package com.whisker.mrr.xrunner.presentation.views.challenge

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whisker.mrr.domain.interactor.SaveChallengeInteractor
import com.whisker.mrr.domain.model.Challenge
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class AddChallengeViewModel
@Inject constructor(private val saveChallengeInteractor: SaveChallengeInteractor)
: ViewModel() {

    private val isChallengeSaved =  MutableLiveData<Boolean> ()
    private val disposables = CompositeDisposable()

    fun saveChallenge(challenge: Challenge) {
        disposables.add(
            saveChallengeInteractor.saveChallenge(challenge)
                .subscribe({
                    isChallengeSaved.postValue(true)
                }, { error ->
                    isChallengeSaved.postValue(false)
                    error.printStackTrace()
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    fun isChallengeSaved() = isChallengeSaved
}