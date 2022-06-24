package com.pubnub.api.services;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

import java.util.List;
import java.util.Map;

public interface SignalService {

    @GET("/signal/{pubKey}/{subKey}/0/{channel}/0/{payload}")
    Call<List<Object>> signal(@Path("pubKey") String pubKey,
                              @Path("subKey") String subKey,
                              @Path("channel") String channel,
                              @Path(value = "payload", encoded = true) String message,
                              @QueryMap(encoded = true) Map<String, String> options);

}
