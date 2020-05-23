package com.pubnub.api.services

import com.google.gson.JsonObject
import com.pubnub.api.models.server.Envelope
import com.pubnub.api.models.server.access_manager.AccessManagerGrantPayload
import retrofit2.Call
import retrofit2.http.*

interface AccessManagerService {

    @GET("/v2/auth/grant/sub-key/{subKey}")
    fun grant(
        @Path("subKey") subKey: String,
        @QueryMap options: Map<String, String>
    ): Call<Envelope<AccessManagerGrantPayload>>

    @POST("/v3/pam/{subKey}/grant")
    fun grantToken(
        @Path("subKey") subKey: String,
        @Body body: Any,
        @QueryMap options: Map<String, String>
    ): Call<JsonObject>

}