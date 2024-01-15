package com.pubnub.api.services;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

import java.util.List;
import java.util.Map;

public interface TimeService {
    @GET("/time/0")
    Call<List<Long>> fetchTime(@QueryMap Map<String, String> options);
}
