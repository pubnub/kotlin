package com.pubnub.api.services

import com.pubnub.api.models.consumer.objects_api.user.PNUser
import com.pubnub.api.models.server.objects_api.EntityArrayEnvelope
import com.pubnub.api.models.server.objects_api.EntityEnvelope
import retrofit2.Call
import retrofit2.http.*

interface UserService {

    @GET("v1/objects/{subKey}/users")
    fun getUsers(
        @Path("subKey") subKey: String,
        @QueryMap(encoded = true) options: Map<String, String>
    ): Call<EntityArrayEnvelope<PNUser>>

    @GET("v1/objects/{subKey}/users/{userId}")
    fun getUser(
        @Path("subKey") subKey: String,
        @Path("userId") userId: String,
        @QueryMap(encoded = true) options: Map<String, String>
    ): Call<EntityEnvelope<PNUser>>

    @POST("v1/objects/{subKey}/users")
    @Headers("Content-Type: application/json; charset=UTF-8")
    fun createUser(
        @Path("subKey") subKey: String,
        @Body body: Any,
        @QueryMap(encoded = true) options: Map<String, String>
    ): Call<EntityEnvelope<PNUser>>

    @PATCH("v1/objects/{subKey}/users/{userId}")
    @Headers("Content-Type: application/json; charset=UTF-8")
    fun updateUser(
        @Path("subKey") subKey: String,
        @Path("userId") userId: String,
        @Body body: Any,
        @QueryMap(encoded = true) options: Map<String, String>
    ): Call<EntityEnvelope<PNUser>>

    @DELETE("v1/objects/{subKey}/users/{userId}")
    fun deleteUser(
        @Path("subKey") subKey: String,
        @Path("userId") userId: String,
        @QueryMap(encoded = true) options: Map<String, String>
    ): Call<Void>

}