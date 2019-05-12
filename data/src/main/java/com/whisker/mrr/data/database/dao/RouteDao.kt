package com.whisker.mrr.data.database.dao

import androidx.room.*
import com.whisker.mrr.data.database.model.RouteEntity
import io.reactivex.Flowable

@Dao
abstract class RouteDao : BaseDao<RouteEntity> {

    @Query("SELECT * FROM Route")
    abstract fun gerRoutes() : Flowable<List<RouteEntity>>

    @Query("DELETE FROM Route WHERE routeID = :routeID")
    abstract fun deleteRouteById(routeID: Long)

    @Query("DELETE FROM Route")
    abstract fun clearTable()
}