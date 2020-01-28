package com.pubnub.api.services;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

import java.util.List;
import java.util.Map;

public interface PushService {

    @GET("v1/push/sub-key/{subKey}/devices/{pushToken}")
    Call<List<Object>> modifyChannelsForDevice(@Path("subKey") String subKey,
                                               @Path("pushToken") String pushToken,
                                               @QueryMap Map<String, String> options);

    @GET("v1/push/sub-key/{subKey}/devices/{pushToken}/remove")
    Call<List<Object>> removeAllChannelsForDevice(@Path("subKey") String subKey,
                                                  @Path("pushToken") String pushToken,
                                                  @QueryMap Map<String, String> options);

    @GET("v1/push/sub-key/{subKey}/devices/{pushToken}")
    Call<List<String>> listChannelsForDevice(@Path("subKey") String subKey,
                                             @Path("pushToken") String pushToken,
                                             @QueryMap Map<String, String> options);

    // V2 (APNS2)

    @GET("v2/push/sub-key/{subKey}/devices-apns2/{deviceApns2}")
    Call<List<Object>> modifyChannelsForDeviceApns2(@Path("subKey") String subKey,
                                                    @Path("deviceApns2") String deviceApns2,
                                                    @QueryMap Map<String, String> options);

    @GET("v2/push/sub-key/{subKey}/devices-apns2/{deviceApns2}")
    Call<List<String>> listChannelsForDeviceApns2(@Path("subKey") String subKey,
                                                  @Path("deviceApns2") String deviceApns2,
                                                  @QueryMap Map<String, String> options);

    @GET("v2/push/sub-key/{subKey}/devices-apns2/{deviceApns2}/remove")
    Call<List<Object>> removeAllChannelsForDeviceApns2(@Path("subKey") String subKey,
                                                       @Path("deviceApns2") String deviceApns2,
                                                       @QueryMap Map<String, String> options);


}
