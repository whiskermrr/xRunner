package com.whisker.mrr.domain.common.mapper

import com.whisker.mrr.domain.common.getFirstDayOfTheMonthInMillis
import com.whisker.mrr.domain.common.sumByFloat
import com.whisker.mrr.domain.common.sumByLong
import com.whisker.mrr.domain.model.Route
import com.whisker.mrr.domain.model.RouteHolder
import java.util.*

object RouteHolderMapper {

    fun transformToRouteHolderList(routes: List<Route>) : List<RouteHolder> {
        val holders = mutableListOf<RouteHolder>()
        val months = routes.map { it.date }.distinctBy { Date(it).getFirstDayOfTheMonthInMillis() }
        for(month in months) {
            val holder = RouteHolder()
            val matchingRoutes = routes.filter { Date(it.date).getFirstDayOfTheMonthInMillis() == month }
            holder.month = month
            holder.totalDistance = matchingRoutes.sumByFloat { it.routeStats.wgs84distance }
            holder.totalTime = matchingRoutes.sumByLong { it.routeStats.routeTime }
            holder.routes = matchingRoutes
            holders.add(holder)
        }

        return holders
    }
}