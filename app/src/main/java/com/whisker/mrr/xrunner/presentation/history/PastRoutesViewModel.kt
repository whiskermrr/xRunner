package com.whisker.mrr.xrunner.presentation.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whisker.mrr.xrunner.domain.model.Route
import com.whisker.mrr.xrunner.domain.repository.RouteRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PastRoutesViewModel
@Inject constructor(private val routeRepository: RouteRepository)
: ViewModel() {

    private val routeList = MutableLiveData<Map<Long, List<Route>>>()
    private val disposables = CompositeDisposable()

    init {
        getPastRoutesList()
    }

    private fun getPastRoutesList() {
        disposables.add(
            routeRepository.getRouteList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { routes ->
                    routeList.postValue(routes)
                }
        )
    }

    fun getRouteList() = routeList
}