package com.whisker.mrr.xrunner.presentation.views.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whisker.mrr.domain.interactor.GetRouteListInteractor
import com.whisker.mrr.domain.interactor.RemoveRouteInteractor
import com.whisker.mrr.xrunner.presentation.mapper.RouteMapper
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class PastRoutesViewModel
@Inject constructor(private val getRouteListInteractor: GetRouteListInteractor, private val removeRouteInteractor: RemoveRouteInteractor)
: ViewModel() {

    private val routeList = MutableLiveData<GetRoutesViewState>()
    private val routeRemoved = MutableLiveData<RemoveRouteViewState>()
    private val disposables = CompositeDisposable()
    private lateinit var routesDisposable: Disposable

    init {
        getPastRoutesList()
    }

    fun getPastRoutesList() {
        if(::routesDisposable.isInitialized && !routesDisposable.isDisposed) {
            routesDisposable.dispose()
        }

        routesDisposable =
            getRouteListInteractor.getRoutes()
            .map { holders -> RouteMapper.listOfEntityHoldersToListOfRouteHolders(holders) }
            .subscribe({ routes ->
                routeList.postValue(GetRoutesViewState.Routes(routes))
            }, { error ->
                routeList.postValue(GetRoutesViewState.Error(error.message))
            })
    }

    fun removeRoute(routeId: Long) {
        disposables.add(
            removeRouteInteractor.removeRoute(routeId)
                .subscribe({
                    routeRemoved.postValue(RemoveRouteViewState.RouteRemoved(routeId))
                }, { error ->
                    routeRemoved.postValue(RemoveRouteViewState.Error(error.message))
                })
        )
    }

    fun getRouteList() = routeList
    fun getRouteRemoved() = routeRemoved
}