package com.pubnub.internal.services

import com.pubnub.api.models.consumer.datasync.entity.PNEntity
import com.pubnub.api.models.consumer.datasync.user.PNUser
import com.pubnub.internal.models.server.datasync.CreateEntityRequest
import com.pubnub.internal.models.server.datasync.CreateUserRequest
import com.pubnub.internal.models.server.datasync.EntitiesEnvelope
import com.pubnub.internal.models.server.datasync.JsonPatchOperation
import com.pubnub.internal.models.server.datasync.SetEntityRequest
import com.pubnub.internal.models.server.objects_api.EntityEnvelope
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @GET("v1/datasync/subkeys/{subKey}/entities")
    fun getEntities(
        @Path("subKey") subKey: String,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf(),
    ): Call<EntitiesEnvelope<PNEntity>>

    @Headers("Content-Type: application/json-patch+json")
    @PATCH("v1/datasync/subkeys/{subKey}/entities/{entityId}")
    fun updateEntity(
        @Path("subKey") subKey: String,
        @Path("entityId") entityId: String,
        @Body body: List<JsonPatchOperation>,
        @Header("If-Match") ifMatch: String?,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf(),
    ): Call<EntityEnvelope<PNEntity>>

    @Headers("Content-Type: application/vnd.pubnub.objects.entity+json;version=1")
    @PUT("v1/datasync/subkeys/{subKey}/entities/{entityId}")
    fun setEntity(
        @Path("subKey") subKey: String,
        @Path("entityId") entityId: String,
        @Body body: SetEntityRequest,
        @Header("If-Match") ifMatch: String?,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf(),
    ): Call<EntityEnvelope<PNEntity>>

    @GET("v1/datasync/subkeys/{subKey}/users/{userId}")
    fun getUser(
        @Path("subKey") subKey: String,
        @Path("userId") userId: String,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf(),
    ): Call<EntityEnvelope<PNUser>>

    @Headers("Content-Type: application/vnd.pubnub.objects.user+json;version=1")
    @POST("v1/datasync/subkeys/{subKey}/users")
    fun createUser(
        @Path("subKey") subKey: String,
        @Body body: CreateUserRequest,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf(),
    ): Call<EntityEnvelope<PNUser>>

    @DELETE("v1/datasync/subkeys/{subKey}/users/{userId}")
    fun deleteUser(
        @Path("subKey") subKey: String,
        @Path("userId") userId: String,
        @Header("If-Match") ifMatch: String?,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf(),
    ): Call<Void>

    @GET("v1/datasync/subkeys/{subKey}/users")
    fun getUsers(
        @Path("subKey") subKey: String,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf(),
    ): Call<EntitiesEnvelope<PNUser>>

    @Headers("Content-Type: application/json-patch+json")
    @PATCH("v1/datasync/subkeys/{subKey}/users/{userId}")
    fun updateUser(
        @Path("subKey") subKey: String,
        @Path("userId") userId: String,
        @Body body: List<JsonPatchOperation>,
        @Header("If-Match") ifMatch: String?,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf(),
    ): Call<EntityEnvelope<PNUser>>

    @Headers("Content-Type: application/vnd.pubnub.objects.user+json;version=1")
    @PUT("v1/datasync/subkeys/{subKey}/users/{userId}")
    fun setUser(
        @Path("subKey") subKey: String,
        @Path("userId") userId: String,
        @Body body: SetEntityRequest,
        @Header("If-Match") ifMatch: String?,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf(),
    ): Call<EntityEnvelope<PNUser>>
}
