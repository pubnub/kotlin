package com.pubnub.api.endpoints.pubsub;

import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.Map;

/**
 * Created by Max on 4/5/16.
 */
public interface PubSubService {

    @GET("publish/{pubKey}/{subKey}/0/{channel}/0/{message}")
    Call<List<Object>> publish(@Path("pubKey") String pubKey,
                               @Path("subKey") String subKey,
                               @Path("channel") String channel,
                               @Path("message") String message,
                               @QueryMap Map<String, Object> options);

    @POST("publish/{pubKey}/{subKey}/0/{channel}/0")
    @Headers("Content-Type: application/json; charset=UTF-8")
    Call<List<Object>> publishWithPost(@Path("pubKey") String pubKey,
                                       @Path("subKey") String subKey,
                                       @Path("channel") String channel,
                                       @Body Object body,
                                       @QueryMap Map<String, Object> options);


}
