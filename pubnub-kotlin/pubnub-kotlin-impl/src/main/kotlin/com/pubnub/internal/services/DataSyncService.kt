package com.pubnub.internal.services

import com.pubnub.api.models.consumer.datasync.channel.PNDataSyncChannel
import com.pubnub.api.models.consumer.datasync.entity.PNDataSyncEntity
import com.pubnub.api.models.consumer.datasync.membership.PNDataSyncMembership
import com.pubnub.api.models.consumer.datasync.relationship.PNDataSyncRelationship
import com.pubnub.api.models.consumer.datasync.user.PNDataSyncUser
import com.pubnub.internal.models.server.datasync.CreateChannelRequest
import com.pubnub.internal.models.server.datasync.CreateEntityRequest
import com.pubnub.internal.models.server.datasync.CreateMembershipRequest
import com.pubnub.internal.models.server.datasync.CreateRelationshipRequest
import com.pubnub.internal.models.server.datasync.CreateUserRequest
import com.pubnub.internal.models.server.datasync.EntitiesEnvelope
import com.pubnub.internal.models.server.datasync.JsonPatchOperation
import com.pubnub.internal.models.server.datasync.SetEntityRequest
import com.pubnub.internal.models.server.datasync.SetMembershipRequest
import com.pubnub.internal.models.server.datasync.SetRelationshipRequest
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
    ): Call<EntityEnvelope<PNDataSyncEntity>>

    @Headers("Content-Type: application/vnd.pubnub.objects.entity+json;version=1")
    @POST("v1/datasync/subkeys/{subKey}/entities")
    fun createEntity(
        @Path("subKey") subKey: String,
        @Body body: CreateEntityRequest,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf(),
    ): Call<EntityEnvelope<PNDataSyncEntity>>

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
    ): Call<EntitiesEnvelope<PNDataSyncEntity>>

    @Headers("Content-Type: application/json-patch+json")
    @PATCH("v1/datasync/subkeys/{subKey}/entities/{entityId}")
    fun updateEntity(
        @Path("subKey") subKey: String,
        @Path("entityId") entityId: String,
        @Body body: List<JsonPatchOperation>,
        @Header("If-Match") ifMatch: String?,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf(),
    ): Call<EntityEnvelope<PNDataSyncEntity>>

    @Headers("Content-Type: application/vnd.pubnub.objects.entity+json;version=1")
    @PUT("v1/datasync/subkeys/{subKey}/entities/{entityId}")
    fun setEntity(
        @Path("subKey") subKey: String,
        @Path("entityId") entityId: String,
        @Body body: SetEntityRequest,
        @Header("If-Match") ifMatch: String?,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf(),
    ): Call<EntityEnvelope<PNDataSyncEntity>>

    @GET("v1/datasync/subkeys/{subKey}/users/{userId}")
    fun getUser(
        @Path("subKey") subKey: String,
        @Path("userId") userId: String,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf(),
    ): Call<EntityEnvelope<PNDataSyncUser>>

    @Headers("Content-Type: application/vnd.pubnub.objects.user+json;version=1")
    @POST("v1/datasync/subkeys/{subKey}/users")
    fun createUser(
        @Path("subKey") subKey: String,
        @Body body: CreateUserRequest,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf(),
    ): Call<EntityEnvelope<PNDataSyncUser>>

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
    ): Call<EntitiesEnvelope<PNDataSyncUser>>

    @Headers("Content-Type: application/json-patch+json")
    @PATCH("v1/datasync/subkeys/{subKey}/users/{userId}")
    fun updateUser(
        @Path("subKey") subKey: String,
        @Path("userId") userId: String,
        @Body body: List<JsonPatchOperation>,
        @Header("If-Match") ifMatch: String?,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf(),
    ): Call<EntityEnvelope<PNDataSyncUser>>

    @Headers("Content-Type: application/vnd.pubnub.objects.user+json;version=1")
    @PUT("v1/datasync/subkeys/{subKey}/users/{userId}")
    fun setUser(
        @Path("subKey") subKey: String,
        @Path("userId") userId: String,
        @Body body: SetEntityRequest,
        @Header("If-Match") ifMatch: String?,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf(),
    ): Call<EntityEnvelope<PNDataSyncUser>>

    @GET("v1/datasync/subkeys/{subKey}/channels/{channelId}")
    fun getChannel(
        @Path("subKey") subKey: String,
        @Path("channelId") channelId: String,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf(),
    ): Call<EntityEnvelope<PNDataSyncChannel>>

    @Headers("Content-Type: application/vnd.pubnub.objects.channel+json;version=1")
    @POST("v1/datasync/subkeys/{subKey}/channels")
    fun createChannel(
        @Path("subKey") subKey: String,
        @Body body: CreateChannelRequest,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf(),
    ): Call<EntityEnvelope<PNDataSyncChannel>>

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
    ): Call<EntitiesEnvelope<PNDataSyncChannel>>

    @Headers("Content-Type: application/json-patch+json")
    @PATCH("v1/datasync/subkeys/{subKey}/channels/{channelId}")
    fun updateChannel(
        @Path("subKey") subKey: String,
        @Path("channelId") channelId: String,
        @Body body: List<JsonPatchOperation>,
        @Header("If-Match") ifMatch: String?,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf(),
    ): Call<EntityEnvelope<PNDataSyncChannel>>

    @Headers("Content-Type: application/vnd.pubnub.objects.channel+json;version=1")
    @PUT("v1/datasync/subkeys/{subKey}/channels/{channelId}")
    fun setChannel(
        @Path("subKey") subKey: String,
        @Path("channelId") channelId: String,
        @Body body: SetEntityRequest,
        @Header("If-Match") ifMatch: String?,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf(),
    ): Call<EntityEnvelope<PNDataSyncChannel>>

    @GET("v1/datasync/subkeys/{subKey}/memberships/{membershipId}")
    fun getMembership(
        @Path("subKey") subKey: String,
        @Path("membershipId") membershipId: String,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf(),
    ): Call<EntityEnvelope<PNDataSyncMembership>>

    @Headers("Content-Type: application/vnd.pubnub.objects.membership+json;version=1")
    @POST("v1/datasync/subkeys/{subKey}/memberships")
    fun createMembership(
        @Path("subKey") subKey: String,
        @Body body: CreateMembershipRequest,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf(),
    ): Call<EntityEnvelope<PNDataSyncMembership>>

    @DELETE("v1/datasync/subkeys/{subKey}/memberships/{membershipId}")
    fun deleteMembership(
        @Path("subKey") subKey: String,
        @Path("membershipId") membershipId: String,
        @Header("If-Match") ifMatch: String?,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf(),
    ): Call<Void>

    @GET("v1/datasync/subkeys/{subKey}/memberships")
    fun getMemberships(
        @Path("subKey") subKey: String,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf(),
    ): Call<EntitiesEnvelope<PNDataSyncMembership>>

    @Headers("Content-Type: application/json-patch+json")
    @PATCH("v1/datasync/subkeys/{subKey}/memberships/{membershipId}")
    fun updateMembership(
        @Path("subKey") subKey: String,
        @Path("membershipId") membershipId: String,
        @Body body: List<JsonPatchOperation>,
        @Header("If-Match") ifMatch: String?,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf(),
    ): Call<EntityEnvelope<PNDataSyncMembership>>

    @Headers("Content-Type: application/vnd.pubnub.objects.membership+json;version=1")
    @PUT("v1/datasync/subkeys/{subKey}/memberships/{membershipId}")
    fun setMembership(
        @Path("subKey") subKey: String,
        @Path("membershipId") membershipId: String,
        @Body body: SetMembershipRequest,
        @Header("If-Match") ifMatch: String?,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf(),
    ): Call<EntityEnvelope<PNDataSyncMembership>>

    @GET("v1/datasync/subkeys/{subKey}/relationships/{relationshipId}")
    fun getRelationship(
        @Path("subKey") subKey: String,
        @Path("relationshipId") relationshipId: String,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf(),
    ): Call<EntityEnvelope<PNDataSyncRelationship>>

    @Headers("Content-Type: application/vnd.pubnub.objects.relationship+json;version=1")
    @POST("v1/datasync/subkeys/{subKey}/relationships")
    fun createRelationship(
        @Path("subKey") subKey: String,
        @Body body: CreateRelationshipRequest,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf(),
    ): Call<EntityEnvelope<PNDataSyncRelationship>>

    @DELETE("v1/datasync/subkeys/{subKey}/relationships/{relationshipId}")
    fun deleteRelationship(
        @Path("subKey") subKey: String,
        @Path("relationshipId") relationshipId: String,
        @Header("If-Match") ifMatch: String?,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf(),
    ): Call<Void>

    @GET("v1/datasync/subkeys/{subKey}/relationships")
    fun getRelationships(
        @Path("subKey") subKey: String,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf(),
    ): Call<EntitiesEnvelope<PNDataSyncRelationship>>

    @Headers("Content-Type: application/json-patch+json")
    @PATCH("v1/datasync/subkeys/{subKey}/relationships/{relationshipId}")
    fun updateRelationship(
        @Path("subKey") subKey: String,
        @Path("relationshipId") relationshipId: String,
        @Body body: List<JsonPatchOperation>,
        @Header("If-Match") ifMatch: String?,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf(),
    ): Call<EntityEnvelope<PNDataSyncRelationship>>

    @Headers("Content-Type: application/vnd.pubnub.objects.relationship+json;version=1")
    @PUT("v1/datasync/subkeys/{subKey}/relationships/{relationshipId}")
    fun setRelationship(
        @Path("subKey") subKey: String,
        @Path("relationshipId") relationshipId: String,
        @Body body: SetRelationshipRequest,
        @Header("If-Match") ifMatch: String?,
        @QueryMap(encoded = true) options: Map<String, String> = mapOf(),
    ): Call<EntityEnvelope<PNDataSyncRelationship>>
}
