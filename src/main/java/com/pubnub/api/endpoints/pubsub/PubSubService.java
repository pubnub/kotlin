package com.pubnub.api.endpoints.pubsub;

import com.pubnub.api.models.server.SubscribeEnvelope;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Headers;
import retrofit2.http.Body;
import java.util.List;
import java.util.Map;

public interface PubSubService {

    @GET("v2/subscribe/{subKey}/{channel}/0")
    Call<SubscribeEnvelope> subscribe(@Path("subKey") String subKey,
                                      @Path("channel") String channel,
                                      @QueryMap(encoded = true) Map<String, String> options);

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
