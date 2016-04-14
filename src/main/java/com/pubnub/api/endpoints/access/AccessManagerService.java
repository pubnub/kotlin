package com.pubnub.api.endpoints.access;

import com.pubnub.api.core.models.Envelope;
import com.pubnub.api.core.models.consumer_facing.PNAccessManagerAuditData;
import com.pubnub.api.core.models.consumer_facing.PNAccessManagerGrantData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

import java.util.Map;

public interface AccessManagerService {

    @GET("/v1/auth/grant/sub-key/{subKey}")
    Call<Envelope<PNAccessManagerGrantData>> grant(@Path("subKey") String subKey, @QueryMap Map<String, String> options);

    @GET("/v1/auth/audit/sub-key/{subKey}")
    Call<Envelope<PNAccessManagerAuditData>> audit(@Path("subKey") String subKey, @QueryMap Map<String, String> options);

}
