package com.pubnub.api.services;

import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.Map;

public interface PublishService {


    @GET("publish/{pubKey}/{subKey}/0/{channel}/0/{message}")
    Call<List<Object>> publish(@Path("pubKey") String pubKey,
                               @Path("subKey") String subKey,
                               @Path("channel") String channel,
                               @Path(value = "message", encoded = true) String message,
                               @QueryMap(encoded = true) Map<String, String> options);

    @POST("publish/{pubKey}/{subKey}/0/{channel}/0")
    @Headers("Content-Type: application/json; charset=UTF-8")
    Call<List<Object>> publishWithPost(@Path("pubKey") String pubKey,
                                       @Path("subKey") String subKey,
                                       @Path("channel") String channel,
                                       @Body Object body,
                                       @QueryMap(encoded = true) Map<String, String> options);


}
