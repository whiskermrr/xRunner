package com.whisker.mrr.data.database.model

import androidx.room.*
import com.google.gson.annotations.SerializedName
import com.whisker.mrr.domain.model.Coords
import com.whisker.mrr.domain.model.RouteStats

@Entity(tableName = "Route")
data class RouteEntity(

    @SerializedName("RouteID")
    @PrimaryKey(autoGenerate = false)
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

    @SerializedName("IsDeleted")
    @ColumnInfo(name = COL_IS_DELETED)
    var isDeleted: Boolean = false,

    @Embedded
    var routeStats: RouteStats? = null
) {
    companion object {
        const val COL_ROUTE_ID = "routeID"
        const val COL_NAME = "name"
        const val COL_DATE = "date"
        const val COL_WAYPOINTS = "waypoints"
        const val COL_IS_DELETED = "isDeleted"
    }
}