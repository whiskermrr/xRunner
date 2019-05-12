package com.whisker.mrr.data.source

import com.whisker.mrr.data.api.XRunnerHttpService
import com.whisker.mrr.data.api.request.GetDataRequest
import com.whisker.mrr.data.mapper.RouteEntityMapper
import com.whisker.mrr.domain.manager.PreferencesManager
import com.whisker.mrr.domain.model.Route
import com.whisker.mrr.domain.source.RemoteRouteSource
import io.reactivex.Completable
import io.reactivex.Single

class RemoteRouteDataSource(
    private val xRunnerHttpService: XRunnerHttpService,
    private val preferencesManager: PreferencesManager
) : RemoteRouteSource {

    companion object {
        const val KEY_ROUTES_TIMESTAMP = "key_routes_timestamp"
    }

    override fun saveRoute(route: Route): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveRoutes(routes: List<Route>): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRoutes(): Single<List<Route>> {
        return preferencesManager.getStringValue(KEY_ROUTES_TIMESTAMP)
            .flatMap { lastTimestamp ->
                xRunnerHttpService.getRoutes(GetDataRequest(lastTimestamp).getQueryMap())
                    .doOnSuccess { response ->
                        preferencesManager.saveValue(KEY_ROUTES_TIMESTAMP, response.data?.currentDownloadTimestamp ?: "0")
                    }
                    .map { response ->
                        response.data?.routes?.let { routes ->
                            return@map RouteEntityMapper.transformListFromEntities(routes)
                        } ?: return@map listOf<Route>()
                    }
                    .onErrorReturn { listOf() }
            }
    }

    override fun removeRouteById(routeId: Long): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}