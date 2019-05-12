package com.whisker.mrr.data.source

import com.whisker.mrr.data.database.dao.RouteDao
import com.whisker.mrr.data.mapper.RouteEntityMapper
import com.whisker.mrr.domain.model.Route
import com.whisker.mrr.domain.source.LocalRouteSource
import io.reactivex.Completable
import io.reactivex.Flowable

class LocalRouteDataSource(
    private val routeDao: RouteDao
) : LocalRouteSource {

    override fun saveRoute(route: Route): Completable {
        return Completable.fromAction {
            routeDao.insert(RouteEntityMapper.transformToEntity(route))
        }
    }

    override fun saveRoutes(routes: List<Route>): Completable {
        return Completable.fromAction {
            routeDao.insertAll(RouteEntityMapper.transformListToEntitites(routes))
        }
    }

    override fun getRoutes(): Flowable<List<Route>> {
        return routeDao.gerRoutes().map { RouteEntityMapper.transformListFromEntities(it) }
    }

    override fun removeRouteById(routeID: Long): Completable {
        return Completable.fromAction {
            routeDao.deleteRouteById(routeID)
        }
    }
}