package com.pubnub.internal.services

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.QueryMap

internal interface PublishService {
    @GET("publish/{pubKey}/{subKey}/0/{channel}/0/{message}")
    fun publish(
        @Path("pubKey") pubKey: String,
        @Path("subKey") subKey: String,
        @Path("channel") channel: String,
        @Path(value = "message", encoded = false) message: String,
        @QueryMap(encoded = false) options: Map<String, String>,
    ): Call<List<Any>>

    @POST("publish/{pubKey}/{subKey}/0/{channel}/0")
    @Headers("Content-Type: application/json; charset=UTF-8")
    fun publishWithPost(
        @Path("pubKey") pubKey: String,
        @Path("subKey") subKey: String,
        @Path("channel") channel: String,
        @Body body: Any,
        @QueryMap(encoded = false) options: Map<String, String>,
    ): Call<List<Any>>

    /**
     * Publish V2 endpoint for large messages (> 32KB, up to 2MB).
     * Uses V2 signature (includes body in signature calculation).
     */
    @POST("v2/publish/{pubKey}/{subKey}/0/{channel}/0")
    @Headers("Content-Type: application/json; charset=UTF-8")
    fun publishWithPostV2(
        @Path("pubKey") pubKey: String,
        @Path("subKey") subKey: String,
        @Path("channel") channel: String,
        @Body body: Any,
        @QueryMap(encoded = false) options: Map<String, String>,
    ): Call<List<Any>>
}
