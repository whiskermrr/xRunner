package com.example.data.model

import androidx.room.*
import com.google.gson.annotations.SerializedName
import com.whisker.mrr.domain.model.Coords
import com.whisker.mrr.domain.model.RouteStats

@Entity(tableName = "Route")
data class RouteEntity(

    @SerializedName("RouteID")
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COL_ROUTE_ID)
    var routeId: Long? = null,

    @SerializedName("Name")
    @ColumnInfo(name = COL_NAME)
    var name: String? = null,

    @SerializedName("Waypoints")
    @ColumnInfo(name = COL_WAYPOINTS)
    var waypoints: List<Coords>? = null,

    @SerializedName("Date")
    @ColumnInfo(name = COL_DATE)
    var date: Long? = null,

    @Embedded
    var routeStats: RouteStats? = null
) {
    companion object {
        const val COL_ROUTE_ID = "routeID"
        const val COL_NAME = "name"
        const val COL_DATE = "date"
        const val COL_WAYPOINTS = "waypoints"
    }
}