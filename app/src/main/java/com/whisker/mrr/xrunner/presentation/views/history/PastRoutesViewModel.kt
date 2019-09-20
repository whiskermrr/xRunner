package com.whisker.mrr.xrunner.presentation.views.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whisker.mrr.domain.interactor.GetRouteListInteractor
import com.whisker.mrr.domain.interactor.RemoveRouteInteractor
import com.whisker.mrr.xrunner.presentation.mapper.RouteMapper
import com.whisker.mrr.xrunner.presentation.model.RouteModel
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

    fun removeRoute(route: RouteModel, position: Int) {
        disposables.add(
            removeRouteInteractor.removeRoute(route.routeId)
                .subscribe({
                    routeRemoved.postValue(RemoveRouteViewState.RouteRemoved(route, position))
                }, { error ->
                    routeRemoved.postValue(RemoveRouteViewState.Error(error.message))
                })
        )
    }

    fun getRouteList() = routeList
    fun getRouteRemoved() = routeRemoved
}