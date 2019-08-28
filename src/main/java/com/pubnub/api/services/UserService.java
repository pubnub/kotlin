package com.pubnub.api.services;

import com.google.gson.JsonElement;
import com.pubnub.api.models.consumer.objects_api.user.PNUser;
import com.pubnub.api.models.server.objects_api.EntityArrayEnvelope;
import com.pubnub.api.models.server.objects_api.EntityEnvelope;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

import java.util.Map;

public interface UserService {

    @GET("v1/objects/{subKey}/users")
    Call<EntityArrayEnvelope<PNUser>> getUsers(@Path("subKey") String subKey,
                                               @QueryMap(encoded = true) Map<String, String> options);

    @GET("v1/objects/{subKey}/users/{userId}")
    Call<EntityEnvelope<PNUser>> getUser(@Path("subKey") String subKey,
                                         @Path("userId") String userId,
                                         @QueryMap(encoded = true) Map<String, String> options);

    @POST("v1/objects/{subKey}/users")
    @Headers("Content-Type: application/json; charset=UTF-8")
    Call<EntityEnvelope<PNUser>> createUser(@Path("subKey") String subKey,
                                            @Body Object body,
                                            @QueryMap(encoded = true) Map<String, String> options);

    @PATCH("v1/objects/{subKey}/users/{userId}")
    @Headers("Content-Type: application/json; charset=UTF-8")
    Call<EntityEnvelope<PNUser>> updateUser(@Path("subKey") String subKey,
                                            @Path("userId") String userId,
                                            @Body Object body,
                                            @QueryMap(encoded = true) Map<String, String> options);

    @DELETE("v1/objects/{subKey}/users/{userId}")
    Call<EntityEnvelope<JsonElement>> deleteUser(@Path("subKey") String subKey,
                                                 @Path("userId") String userId,
                                                 @QueryMap(encoded = true) Map<String, String> options);

}
