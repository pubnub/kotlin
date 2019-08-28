package com.pubnub.api.services;

import com.google.gson.JsonElement;
import com.pubnub.api.models.consumer.objects_api.space.PNSpace;
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

public interface SpaceService {

    @GET("v1/objects/{subKey}/spaces")
    Call<EntityArrayEnvelope<PNSpace>> getSpaces(@Path("subKey") String subKey,
                                                 @QueryMap(encoded = true) Map<String, String> options);

    @GET("v1/objects/{subKey}/spaces/{spaceId}")
    Call<EntityEnvelope<PNSpace>> getSpace(@Path("subKey") String subKey,
                                           @Path("spaceId") String spaceId,
                                           @QueryMap(encoded = true) Map<String, String> options);

    @POST("v1/objects/{subKey}/spaces")
    @Headers("Content-Type: application/json; charset=UTF-8")
    Call<EntityEnvelope<PNSpace>> createSpace(@Path("subKey") String subKey,
                                              @Body Object body,
                                              @QueryMap(encoded = true) Map<String, String> options);

    @PATCH("v1/objects/{subKey}/spaces/{spaceId}")
    @Headers("Content-Type: application/json; charset=UTF-8")
    Call<EntityEnvelope<PNSpace>> updateSpace(@Path("subKey") String subKey,
                                              @Path("spaceId") String spaceId,
                                              @Body Object body,
                                              @QueryMap(encoded = true) Map<String, String> options);

    @DELETE("v1/objects/{subKey}/spaces/{spaceId}")
    Call<EntityEnvelope<JsonElement>> deleteSpace(@Path("subKey") String subKey,
                                                  @Path("spaceId") String spaceId,
                                                  @QueryMap(encoded = true) Map<String, String> options);

}
