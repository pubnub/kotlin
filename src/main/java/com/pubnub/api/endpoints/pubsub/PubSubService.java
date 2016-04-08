package com.pubnub.api.endpoints.pubsub;

import com.pubnub.api.core.models.server_responses.SubscribeEnvelope;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.Map;

public interface PubSubService {

    @GET("v2/subscribe/{subKey}/{channel}/0")
    Call<SubscribeEnvelope> subscribe(@Path("subKey") String subKey,
                                      @Path("channel") String channel,
                                      @QueryMap Map<String, Object> options);

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
