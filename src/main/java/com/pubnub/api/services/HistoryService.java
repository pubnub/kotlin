package com.pubnub.api.services;

import com.google.gson.JsonElement;
import com.pubnub.api.models.server.DeleteMessagesEnvelope;
import com.pubnub.api.models.server.FetchMessagesEnvelope;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

import java.util.Map;

public interface HistoryService {

    @GET("v2/history/sub-key/{subKey}/channel/{channel}")
    Call<JsonElement> fetchHistory(@Path("subKey") String subKey,
                                   @Path("channel") String channel,
                                   @QueryMap Map<String, String> options);

    @DELETE("v3/history/sub-key/{subKey}/channel/{channels}")
    Call<DeleteMessagesEnvelope> deleteMessages(@Path("subKey") String subKey,
                                                @Path("channels") String channels,
                                                @QueryMap Map<String, String> options);

    @GET("v3/history/sub-key/{subKey}/channel/{channels}")
    Call<FetchMessagesEnvelope> fetchMessages(@Path("subKey") String subKey,
                                              @Path("channels") String channels,
                                              @QueryMap Map<String, String> options);

    @GET("v3/history/sub-key/{subKey}/message-counts/{channels}")
    Call<JsonElement> fetchCount(@Path("subKey") String subKey,
                                 @Path("channels") String channels,
                                 @QueryMap Map<String, String> options);

}
