package com.pubnub.api.services;

import com.pubnub.api.models.server.SubscribeEnvelope;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

import java.util.Map;

public interface SubscribeService {

    @GET("v2/subscribe/{subKey}/{channel}/0")
    Call<SubscribeEnvelope> subscribe(@Path("subKey") String subKey,
                                      @Path("channel") String channel,
                                      @QueryMap(encoded = true) Map<String, String> options);

}
