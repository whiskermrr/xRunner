package com.whisker.mrr.xrunner.presentation.views.history

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whisker.mrr.domain.interactor.GetRouteListInteractor
import com.whisker.mrr.domain.interactor.RemoveRouteInteractor
import com.whisker.mrr.xrunner.presentation.mapper.RouteMapper
import com.whisker.mrr.xrunner.presentation.model.RouteHolder
import com.whisker.mrr.xrunner.utils.TAG
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PastRoutesViewModel
@Inject constructor(private val getRouteListInteractor: GetRouteListInteractor, private val removeRouteInteractor: RemoveRouteInteractor)
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

    fun removeRoute(routeId: String, date: Long) {
        disposables.add(
            removeRouteInteractor.removeRoute(routeId, date)
                .subscribe {
                    Log.e(TAG(), "route removed")
                }
        )
    }

    fun getRouteList() = routeList
}