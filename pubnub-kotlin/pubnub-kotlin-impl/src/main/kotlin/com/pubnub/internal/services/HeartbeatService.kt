package com.pubnub.internal.services

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

internal interface HeartbeatService {
    @GET("v2/presence/sub-key/{subKey}/channel/{channel}/leave")
    fun leave(
        @Path("subKey") subKey: String,
        @Path("channel") channel: String,
        @QueryMap options: Map<String, String>,
    ): Call<Void>

    @GET("v2/presence/sub-key/{subKey}/channel/{channel}/heartbeat")
    fun heartbeat(
        @Path("subKey") subKey: String,
        @Path("channel") channel: String,
        @QueryMap options: Map<String, String>,
    ): Call<Void>
}
