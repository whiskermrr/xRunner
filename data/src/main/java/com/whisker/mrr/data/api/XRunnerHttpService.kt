package com.whisker.mrr.data.api

import com.whisker.mrr.data.api.response.GetRoutesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface XRunnerHttpService {

    @GET("Routes/GetRoutes")
    fun getRoutes(@QueryMap query: Map<String, String>) : Single<GetRoutesResponse>
}