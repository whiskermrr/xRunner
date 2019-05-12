package com.whisker.mrr.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "RouteStats")
data class RouteStatsEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COL_ID)
    var id: Long? = null,

    @ColumnInfo(name = COL_ROUTE_ID)
    var routeID: Long? = null,

    @SerializedName("RouteTime")
    @ColumnInfo(name = COL_ROUTE_TIME)
    var routeTime: Long? = null,

    @SerializedName("AverageSpeed")
    @ColumnInfo(name = COL_AVERAGE_SPEED)
    var averageSpeed: Float? = null,

    @SerializedName("PaceMin")
    @ColumnInfo(name = COL_PACE_MIN)
    var paceMin: Int? = null,

    @SerializedName("PaceSec")
    @ColumnInfo(name = COL_PACE_SEC)
    var paceSec: Int? = null,

    @SerializedName("Distance")
    @ColumnInfo(name = COL_DISTANCE)
    var wgs84distance: Float? = null
) {
    companion object {
        const val COL_ID = "id"
        const val COL_ROUTE_ID = "routeID"
        const val COL_ROUTE_TIME = "routeTime"
        const val COL_AVERAGE_SPEED = "averageSpeed"
        const val COL_PACE_MIN = "paceMin"
        const val COL_PACE_SEC = "paceSec"
        const val COL_DISTANCE = "wgs84distance"
    }
}