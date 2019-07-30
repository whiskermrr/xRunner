package com.whisker.mrr.xrunner.presentation.views.music

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

open class BaseBrowseMusicViewModel : ViewModel() {

    protected val isSongListSet = MutableLiveData<Boolean>()
    protected val disposables = CompositeDisposable()

    fun getIsSongListSet() = isSongListSet

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}