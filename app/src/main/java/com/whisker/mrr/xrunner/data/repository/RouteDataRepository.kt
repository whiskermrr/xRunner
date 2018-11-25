package com.whisker.mrr.xrunner.data.repository

import com.whisker.mrr.xrunner.data.datasource.RouteDatabaseSource
import com.whisker.mrr.xrunner.data.datasource.UserDataSource
import com.whisker.mrr.xrunner.domain.model.Route
import com.whisker.mrr.xrunner.domain.repository.RouteRepository
import io.reactivex.Completable
import javax.inject.Inject

class RouteDataRepository
@Inject constructor(
    private val userDataSource: UserDataSource,
    private val routeDatabaseSource: RouteDatabaseSource
)
: RouteRepository {

    override fun saveRoute(route: Route): Completable {
        return userDataSource.getUserId()
            .flatMapCompletable {
                routeDatabaseSource.saveRoute(route, it)
            }
    }
}