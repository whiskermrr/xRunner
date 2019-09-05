package com.whisker.mrr.webapi.source

import com.whisker.mrr.webapi.request.GetDataRequest
import com.whisker.mrr.domain.manager.PreferencesManager
import com.whisker.mrr.domain.model.Route
import com.whisker.mrr.data.source.RemoteRouteSource
import com.whisker.mrr.domain.common.exception.NoConnectivityException
import com.whisker.mrr.webapi.mapper.RouteDtoMapper
import io.reactivex.Completable
import io.reactivex.Single

class RemoteRouteDataSource(
    private val xRunnerHttpService: com.whisker.mrr.webapi.XRunnerHttpService,
    private val preferencesManager: PreferencesManager
) : RemoteRouteSource {

    companion object {
        const val KEY_ROUTES_TIMESTAMP = "key_routes_timestamp"
    }

    override fun saveRoute(route: Route): Single<Long> {
        return Single.error(NoConnectivityException())
    }

    override fun saveRoutes(routes: List<Route>): Single<List<Long>> {
        return Single.error(NoConnectivityException())
    }

    override fun getRoutes(): Single<List<Route>> {
        return Single.error(NoConnectivityException())
        /*return preferencesManager.getStringValue(KEY_ROUTES_TIMESTAMP)
            .flatMap { lastTimestamp ->
                xRunnerHttpService.getRoutes(GetDataRequest(lastTimestamp).getQueryMap())
                    .doOnSuccess { response ->
                        preferencesManager.saveValue(KEY_ROUTES_TIMESTAMP, response.data?.currentDownloadTimestamp ?: "0")
                    }
                    .map { response ->
                        response.data?.routes?.let { routes ->
                            return@map RouteDtoMapper.transformListFromDtoList(routes)
                        } ?: return@map listOf<Route>()
                    }
                    .onErrorReturn { listOf() }
            }*/
    }

    override fun removeRouteById(routeId: Long): Completable {
        return Completable.error(NoConnectivityException())
    }
}