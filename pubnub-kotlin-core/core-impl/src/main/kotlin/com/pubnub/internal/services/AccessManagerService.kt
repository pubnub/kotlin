package com.pubnub.internal.services

import com.pubnub.internal.models.server.Envelope
import com.pubnub.internal.models.server.access_manager.AccessManagerGrantPayload
import com.pubnub.internal.models.server.access_manager.v3.GrantTokenResponse
import com.pubnub.internal.models.server.access_manager.v3.RevokeTokenResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.QueryMap

internal interface AccessManagerService {

    @GET("/v2/auth/grant/sub-key/{subKey}")
    fun grant(
        @Path("subKey") subKey: String,
        @QueryMap options: Map<String, String>
    ): Call<Envelope<AccessManagerGrantPayload>>

    @POST("/v3/pam/{subKey}/grant")
    fun grantToken(
        @Path("subKey") subKey: String?,
        @Body body: Any?,
        @QueryMap options: Map<String, String>
    ): Call<GrantTokenResponse>

    @DELETE("/v3/pam/{subKey}/grant/{token}")
    fun revokeToken(
        @Path("subKey") subKey: String,
        @Path("token", encoded = true) token: String,
        @QueryMap queryParams: Map<String, String>
    ): Call<RevokeTokenResponse>
}
