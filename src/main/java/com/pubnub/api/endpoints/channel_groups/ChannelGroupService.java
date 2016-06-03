package com.pubnub.api.endpoints.channel_groups;

import com.pubnub.api.models.server.Envelope;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

import java.util.Map;


public interface ChannelGroupService {
    @GET("v1/channel-registration/sub-key/{subKey}/channel-group")
    Call<Envelope<Object>> listAllChannelGroup(@Path("subKey") String subKey,
                                               @QueryMap Map<String, String> options);

    @GET("v1/channel-registration/sub-key/{subKey}/channel-group/{group}")
    Call<Envelope<Object>> allChannelsChannelGroup(@Path("subKey") String subKey,
                                                   @Path("group") String group,
                                                   @QueryMap Map<String, String> options);

    @GET("v1/channel-registration/sub-key/{subKey}/channel-group/{group}")
    Call<Envelope> addChannelChannelGroup(@Path("subKey") String subKey,
                                          @Path("group") String group,
                                          @QueryMap Map<String, String> options);

    @GET("v1/channel-registration/sub-key/{subKey}/channel-group/{group}")
    Call<Envelope> removeChannel(@Path("subKey") String subKey,
                                 @Path("group") String group,
                                 @QueryMap Map<String, String> options);

    @GET("v1/channel-registration/sub-key/{subKey}/channel-group/{group}/remove")
    Call<Envelope> deleteChannelGroup(@Path("subKey") String subKey,
                                      @Path("group") String group,
                                      @QueryMap Map<String, String> options);
}

