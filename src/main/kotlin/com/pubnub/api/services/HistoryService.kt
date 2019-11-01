package com.pubnub.api.services

import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface HistoryService {

    @GET("v2/history/sub-key/{subKey}/channel/{channel}")
    fun fetchHistory(
        @Path("subKey") subKey: String,
        @Path("channel") channel: String,
        @QueryMap options: Map<String, String>
    ): Call<JsonElement>
}