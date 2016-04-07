package com.pubnub.api.endpoints.access;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

import java.util.Map;

/**
 * Created by Max on 4/3/16.
 */
public interface AccessManagerService {

    @GET("/v1/auth/grant/sub-key/{subKey}")
    Call<Object> grant(@Path("subKey") String subKey, @QueryMap Map<String, Object> options);

    @GET("/v1/auth/audit/sub-key/{subKey}")
    Call<Object> audit(@Path("subKey") String subKey, @QueryMap Map<String, Object> options);

}
