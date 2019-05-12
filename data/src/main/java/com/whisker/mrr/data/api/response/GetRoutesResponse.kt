package com.whisker.mrr.data.api.response

import com.whisker.mrr.data.database.model.RouteEntity
import com.google.gson.annotations.SerializedName

data class GetRoutesResponse(
    @SerializedName("Success") val success: Boolean? = null,
    @SerializedName("Error") val error: ResponseError? = null,
    @SerializedName("Data") val data: GetRoutesData? = null
)

data class GetRoutesData(
    @SerializedName("Routes") val routes: List<RouteEntity>? = null,
    @SerializedName("CurrentDownloadTimestamp") val currentDownloadTimestamp: String? = null
)