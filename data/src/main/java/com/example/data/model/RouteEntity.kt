package com.example.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.whisker.mrr.domain.model.Coords
import com.whisker.mrr.domain.model.RouteStats

@Entity(tableName = "Route")
data class RouteEntity(

    @SerializedName("RouteID")
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = COL_ROUTE_ID)
    var routeId: String = "",

    @SerializedName("Name")
    @ColumnInfo(name = COL_NAME)
    var name: String = "",

    @SerializedName("Waypoints")
    @ColumnInfo(name = COL_WAYPOINTS)
    var waypoints: List<Coords> = listOf(),

    @SerializedName("Date")
    @ColumnInfo(name = COL_DATE)
    var date: Long = 0L,

    @Ignore
    @SerializedName("Waypoints")
    var routeStats: RouteStats = RouteStats()
) {
    companion object {
        const val COL_ROUTE_ID = "routeID"
        const val COL_NAME = "name"
        const val COL_DATE = "date"
        const val COL_WAYPOINTS = "waypoints"
    }
}