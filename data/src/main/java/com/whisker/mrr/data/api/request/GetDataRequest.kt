package com.whisker.mrr.data.api.request

import com.google.gson.annotations.SerializedName

open class GetDataRequest(
    @SerializedName("lastDownloadTimestamp")
    private val lastDownload: String
) {
    fun getQueryMap() : Map<String, String> {
        val queryMap = HashMap<String, String>()
        queryMap["lastDownloadTimestamp"] = if(lastDownload.isNotEmpty()) lastDownload else "0"
        return queryMap
    }
}