package com.pubnub.internal.services

import com.pubnub.api.models.consumer.datasync.entity.PNEntity
import com.pubnub.internal.models.server.datasync.CreateEntityRequest
import com.pubnub.internal.models.server.objects_api.EntityEnvelope
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.QueryMap

internal interface DataSyncService {
    @GET("v1/datasync/subkeys/{subKey}/entities/{entityId}")
    fun getEntity(
        @Path("subKey") subKey: String,
        @Path("entityId") entityId: String,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf(),
    ): Call<EntityEnvelope<PNEntity>>

    @Headers("Content-Type: application/vnd.pubnub.objects.entity+json;version=1")
    @POST("v1/datasync/subkeys/{subKey}/entities")
    fun createEntity(
        @Path("subKey") subKey: String,
        @Body body: CreateEntityRequest,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf(),
    ): Call<EntityEnvelope<PNEntity>>

    @DELETE("v1/datasync/subkeys/{subKey}/entities/{entityId}")
    fun deleteEntity(
        @Path("subKey") subKey: String,
        @Path("entityId") entityId: String,
        @Header("If-Match") ifMatch: String?,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf(),
    ): Call<Void>
}
