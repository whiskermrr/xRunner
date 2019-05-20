package com.whisker.mrr.data.source

import com.whisker.mrr.data.database.dao.RouteDao
import com.whisker.mrr.data.mapper.RouteEntityMapper
import com.whisker.mrr.domain.model.Route
import com.whisker.mrr.domain.source.LocalRouteSource
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class LocalRouteDataSource(
    private val routeDao: RouteDao
) : LocalRouteSource {

    override fun saveRoute(route: Route): Single<Long> {
        return Single.fromCallable {
            val nextID = routeDao.getNextLocalID()
            nextID?.let { route.routeId = it } ?: run { route.routeId = -1 }
            routeDao.insert(RouteEntityMapper.transformToEntity(route))
        }
    }

    override fun saveRoutes(routes: List<Route>): Completable {
        return Completable.fromAction {
            routeDao.insertAll(RouteEntityMapper.transformListToEntities(routes))
            routeDao.deleteIsDeleted()
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

    override fun markRouteAsDeleted(routeID: Long): Completable {
        return Completable.fromAction {
            routeDao.markRouteAsDeleted(routeID)
        }
    }

    override fun updateRouteID(oldID: Long, newID: Long): Completable {
        return Completable.fromAction {
            routeDao.updateRouteID(oldID, newID)
        }
    }
}