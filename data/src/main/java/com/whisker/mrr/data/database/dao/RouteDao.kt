package com.whisker.mrr.data.database.dao

import androidx.room.*
import com.whisker.mrr.data.database.model.RouteEntity
import io.reactivex.Flowable

@Dao
abstract class RouteDao : BaseDao<RouteEntity> {

    @Query("SELECT * FROM Route WHERE isDeleted = 0")
    abstract fun gerRoutes() : Flowable<List<RouteEntity>>

    @Query("DELETE FROM Route WHERE routeID = :routeID")
    abstract fun deleteRouteById(routeID: Long)

    @Query("DELETE FROM Route")
    abstract fun clearTable()

    @Query("UPDATE Route SET routeID = :newID WHERE routeID = :oldID")
    abstract fun updateRouteID(oldID: Long, newID: Long)

    @Query("DELETE FROM Route WHERE isDeleted = 1")
    abstract fun deleteIsDeleted()

    @Query("SELECT MIN(routeID) - 1 FROM Route WHERE routeID < 0")
    abstract fun getNextLocalID() : Long?
}