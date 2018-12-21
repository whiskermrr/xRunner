package com.whisker.mrr.xrunner.presentation.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whisker.mrr.xrunner.domain.interactor.GetRouteListInteractor
import com.whisker.mrr.xrunner.presentation.mapper.RouteMapper
import com.whisker.mrr.xrunner.presentation.model.RouteHolder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PastRoutesViewModel
@Inject constructor(private val getRouteListInteractor: GetRouteListInteractor)
: ViewModel() {

    private val routeList = MutableLiveData<List<RouteHolder>>()
    private val disposables = CompositeDisposable()

    init {
        getPastRoutesList()
    }

    private fun getPastRoutesList() {
        disposables.add(
            getRouteListInteractor.flowable()
                .map {
                    RouteMapper.listOfEntityHoldersToListOfRouteHolders(it)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { routes ->
                    routeList.postValue(routes)
                }
        )
    }

    fun getRouteList() = routeList
}