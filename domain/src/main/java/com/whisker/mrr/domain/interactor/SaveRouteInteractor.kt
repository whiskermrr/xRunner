package com.whisker.mrr.domain.interactor

import com.whisker.mrr.domain.common.DomainConstants
import com.whisker.mrr.domain.common.DomainConstants.MILLISECONDS_PER_SECOND
import com.whisker.mrr.domain.common.DomainConstants.MINUTES_PER_HOUR
import com.whisker.mrr.domain.model.RouteEntity
import com.whisker.mrr.domain.model.RouteStatsEntity
import com.whisker.mrr.domain.repository.RouteRepository
import com.whisker.mrr.domain.repository.UserRepository
import com.whisker.mrr.domain.source.AuthSource
import com.whisker.mrr.domain.usecase.CompletableUseCase
import io.reactivex.Completable
import io.reactivex.CompletableTransformer
import java.lang.IllegalArgumentException
import kotlin.math.roundToInt

class SaveRouteInteractor(
    transformer: CompletableTransformer,
    private val routeRepository: RouteRepository,
    private val userRepository: UserRepository,
    private val authSource: AuthSource
) : CompletableUseCase(transformer) {

    companion object {
        private const val PARAM_ROUTE = "param_route"
    }

    fun saveRoute(route: RouteEntity) : Completable {
        val data = HashMap<String, RouteEntity>()
        data[PARAM_ROUTE] = route
        return completable(data)
    }

    override fun createCompletable(data: Map<String, Any>?): Completable {
        val routeEntity = data?.get(PARAM_ROUTE)

        routeEntity?.let {
            return authSource.getUserId()
                .flatMapCompletable { userId ->
                    Completable.concatArray(
                        routeRepository.saveRoute(userId, routeEntity as RouteEntity),
                        userRepository.getUserStats(userId)
                            .map {  userStats ->
                                userStats.totalDistance += routeEntity.routeStats.wgs84distance
                                userStats.totalTime += routeEntity.routeStats.routeTime
                                userStats.experience += calculateExp(routeEntity.routeStats)
                                userStats.averagePace = calculateAveragePace(userStats.totalDistance, userStats.totalTime)
                                userStats
                            }.flatMapCompletable { updatedStats ->
                                userRepository.updateUserStats(userId, updatedStats)
                            }
                    )
                }
        } ?: return Completable.error(IllegalArgumentException("Argument @route must be provided."))
    }

    private fun calculateExp(stats: RouteStatsEntity) : Int {
        return ((stats.averageSpeed * stats.wgs84distance) / 10f).roundToInt()
    }

    private fun calculateAveragePace(totalDistanceInMeters: Float, time: Long) : Float {
        val totalTimeInSeconds = (time / MILLISECONDS_PER_SECOND).toInt()
        return MINUTES_PER_HOUR / ((totalDistanceInMeters / totalTimeInSeconds) * 3.6f)
    }
}