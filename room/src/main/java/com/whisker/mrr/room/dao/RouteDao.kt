package com.whisker.mrr.room.dao

import androidx.room.*
import com.whisker.mrr.room.model.RouteEntity
import io.reactivex.Flowable
import io.reactivex.Single

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

    @Query("UPDATE Route SET isDeleted = 1 WHERE routeID = :routeID")
    abstract fun markRouteAsDeleted(routeID: Long)

    @Query("SELECT MIN(routeID) - 1 FROM Route WHERE routeID < 0")
    abstract fun getNextLocalID() : Long?

    @Query("SELECT * FROM Route WHERE routeID < 0 OR isDeleted = 1")
    abstract fun getRoutesSavedLocallyAndDeleted() : Single<List<RouteEntity>>

    @Query("DELETE FROM Route WHERE routeID < 0")
    abstract fun removeRoutesSavedLocally()
}