package com.pubnub.api.services;

import com.google.gson.JsonObject;
import com.pubnub.api.models.server.Envelope;
import com.pubnub.api.models.server.access_manager.AccessManagerGrantPayload;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

import java.util.Map;

public interface AccessManagerService {

    @GET("/v2/auth/grant/sub-key/{subKey}")
    Call<Envelope<AccessManagerGrantPayload>> grant(@Path("subKey") String subKey,
                                                    @QueryMap Map<String, String> options);

    @POST("/v3/pam/{subKey}/grant")
    Call<JsonObject> grantToken(@Path("subKey") String subKey,
                                @Body Object body,
                                @QueryMap Map<String, String> options);
}
