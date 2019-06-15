package com.whisker.mrr.webapi.response

import com.google.gson.annotations.SerializedName
import com.whisker.mrr.webapi.dto.RouteDto

data class GetRoutesResponse(
    @SerializedName("Success") val success: Boolean? = null,
    @SerializedName("Error") val error: ResponseError? = null,
    @SerializedName("Data") val data: GetRoutesData? = null
)

data class GetRoutesData(
    @SerializedName("Routes") val routes: List<RouteDto>? = null,
    @SerializedName("CurrentDownloadTimestamp") val currentDownloadTimestamp: String? = null
)