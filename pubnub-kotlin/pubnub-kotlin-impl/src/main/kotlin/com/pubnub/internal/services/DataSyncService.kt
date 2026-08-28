package com.pubnub.internal.services

import com.pubnub.api.models.consumer.datasync.channel.DataSyncChannel
import com.pubnub.api.models.consumer.datasync.entity.DataSyncEntity
import com.pubnub.api.models.consumer.datasync.user.DataSyncUser
import com.pubnub.internal.models.server.datasync.CreateChannelRequest
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
    ): Call<EntityEnvelope<DataSyncEntity>>

    @Headers("Content-Type: application/vnd.pubnub.objects.entity+json;version=1")
    @POST("v1/datasync/subkeys/{subKey}/entities")
    fun createEntity(
        @Path("subKey") subKey: String,
        @Body body: CreateEntityRequest,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf(),
    ): Call<EntityEnvelope<DataSyncEntity>>

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
    ): Call<EntitiesEnvelope<DataSyncEntity>>

    @Headers("Content-Type: application/json-patch+json")
    @PATCH("v1/datasync/subkeys/{subKey}/entities/{entityId}")
    fun updateEntity(
        @Path("subKey") subKey: String,
        @Path("entityId") entityId: String,
        @Body body: List<JsonPatchOperation>,
        @Header("If-Match") ifMatch: String?,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf(),
    ): Call<EntityEnvelope<DataSyncEntity>>

    @Headers("Content-Type: application/vnd.pubnub.objects.entity+json;version=1")
    @PUT("v1/datasync/subkeys/{subKey}/entities/{entityId}")
    fun setEntity(
        @Path("subKey") subKey: String,
        @Path("entityId") entityId: String,
        @Body body: SetEntityRequest,
        @Header("If-Match") ifMatch: String?,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf(),
    ): Call<EntityEnvelope<DataSyncEntity>>

    @GET("v1/datasync/subkeys/{subKey}/users/{userId}")
    fun getUser(
        @Path("subKey") subKey: String,
        @Path("userId") userId: String,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf(),
    ): Call<EntityEnvelope<DataSyncUser>>

    @Headers("Content-Type: application/vnd.pubnub.objects.user+json;version=1")
    @POST("v1/datasync/subkeys/{subKey}/users")
    fun createUser(
        @Path("subKey") subKey: String,
        @Body body: CreateUserRequest,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf(),
    ): Call<EntityEnvelope<DataSyncUser>>

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
    ): Call<EntitiesEnvelope<DataSyncUser>>

    @Headers("Content-Type: application/json-patch+json")
    @PATCH("v1/datasync/subkeys/{subKey}/users/{userId}")
    fun updateUser(
        @Path("subKey") subKey: String,
        @Path("userId") userId: String,
        @Body body: List<JsonPatchOperation>,
        @Header("If-Match") ifMatch: String?,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf(),
    ): Call<EntityEnvelope<DataSyncUser>>

    @Headers("Content-Type: application/vnd.pubnub.objects.user+json;version=1")
    @PUT("v1/datasync/subkeys/{subKey}/users/{userId}")
    fun setUser(
        @Path("subKey") subKey: String,
        @Path("userId") userId: String,
        @Body body: SetEntityRequest,
        @Header("If-Match") ifMatch: String?,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf(),
    ): Call<EntityEnvelope<DataSyncUser>>

    @GET("v1/datasync/subkeys/{subKey}/channels/{channelId}")
    fun getChannel(
        @Path("subKey") subKey: String,
        @Path("channelId") channelId: String,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf(),
    ): Call<EntityEnvelope<DataSyncChannel>>

    @Headers("Content-Type: application/vnd.pubnub.objects.channel+json;version=1")
    @POST("v1/datasync/subkeys/{subKey}/channels")
    fun createChannel(
        @Path("subKey") subKey: String,
        @Body body: CreateChannelRequest,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf(),
    ): Call<EntityEnvelope<DataSyncChannel>>

    @DELETE("v1/datasync/subkeys/{subKey}/channels/{channelId}")
    fun deleteChannel(
        @Path("subKey") subKey: String,
        @Path("channelId") channelId: String,
        @Header("If-Match") ifMatch: String?,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf(),
    ): Call<Void>

    @GET("v1/datasync/subkeys/{subKey}/channels")
    fun getChannels(
        @Path("subKey") subKey: String,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf(),
    ): Call<EntitiesEnvelope<DataSyncChannel>>

    @Headers("Content-Type: application/json-patch+json")
    @PATCH("v1/datasync/subkeys/{subKey}/channels/{channelId}")
    fun updateChannel(
        @Path("subKey") subKey: String,
        @Path("channelId") channelId: String,
        @Body body: List<JsonPatchOperation>,
        @Header("If-Match") ifMatch: String?,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf(),
    ): Call<EntityEnvelope<DataSyncChannel>>

    @Headers("Content-Type: application/vnd.pubnub.objects.channel+json;version=1")
    @PUT("v1/datasync/subkeys/{subKey}/channels/{channelId}")
    fun setChannel(
        @Path("subKey") subKey: String,
        @Path("channelId") channelId: String,
        @Body body: SetEntityRequest,
        @Header("If-Match") ifMatch: String?,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf(),
    ): Call<EntityEnvelope<DataSyncChannel>>
}
