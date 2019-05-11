package com.example.data.dao

import androidx.room.*
import com.example.data.model.RouteEntity
import io.reactivex.Flowable

@Dao
interface RouteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Transaction
    fun insert(route: RouteEntity) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Transaction
    fun insertAll(routes: List<RouteEntity>) : List<Long>

    @Query("SELECT * FROM Route")
    fun gerRoutes() : Flowable<List<RouteEntity>>

    @Query("DELETE FROM Route WHERE routeID = :routeID")
    fun deleteRouteById(routeID: Long)

    @Query("DELETE FROM Route")
    fun clearTable()
}