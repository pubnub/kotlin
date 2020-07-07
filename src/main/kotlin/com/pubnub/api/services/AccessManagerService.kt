package com.pubnub.api.services

import com.pubnub.api.models.server.Envelope
import com.pubnub.api.models.server.access_manager.AccessManagerGrantPayload
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface AccessManagerService {

    @GET("/v2/auth/grant/sub-key/{subKey}")
    fun grant(
        @Path("subKey") subKey: String,
        @QueryMap options: Map<String, String>
    ): Call<Envelope<AccessManagerGrantPayload>>
}
