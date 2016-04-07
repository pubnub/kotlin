package com.pubnub.api.endpoints.presence;

import com.pubnub.api.core.models.Envelope;
import com.pubnub.api.core.models.WhereNowData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

import java.util.Map;

public interface PresenceService {

    @GET("v2/presence/sub-key/{subKey}/channel/{channel}/leave")
    Call<Envelope> leave(@Path("subKey") String subKey,
                                @Path("channel") String channel,
                                @QueryMap Map<String, Object> options);

    @GET("v2/presence/sub-key/{subKey}/channel/{channel}/heartbeat")
    Call<Envelope> heartbeat(@Path("subKey") String subKey,
                                    @Path("channel") String channel,
                                    @QueryMap Map<String, Object> options);

    @GET("v2/presence/sub-key/{subKey}/uuid/{uuid}")
    Call<Envelope<WhereNowData>> whereNow(@Path("subKey") String subKey,
                                          @Path("uuid") String uuid);

    @GET("v2/presence/sub_key/{subKey}")
    Call<Envelope<Object>> globalHereNow(@Path("subKey") String subKey,
                                              @QueryMap Map<String, String> options);

    @GET("v2/presence/sub_key/{subKey}/channel/{channel}")
    Call<Envelope<Object>> hereNow(@Path("subKey") String subKey,
                                        @Path("channel") String channel,
                                        @QueryMap Map<String, String> options);

    @GET("v2/presence/sub-key/{subKey}/channel/{channel}/uuid/{uuid}")
    Call<Envelope<Object>> getState(@Path("subKey") String subKey,
                                    @Path("channel") String channel,
                                    @Path("uuid") String uuid,
                                    @QueryMap Map<String, Object> options);

    @GET("v2/presence/sub-key/{subKey}/channel/{channel}/uuid/{uuid}/data")
    Call<Envelope<Object>> setState(@Path("subKey") String subKey,
                                    @Path("channel") String channel,
                                    @Path("uuid") String uuid,
                                    @QueryMap Map<String, Object> options);

}
