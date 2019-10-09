package com.pubnub.api.services;

import com.google.gson.JsonObject;
import com.pubnub.api.models.consumer.message_actions.PNMessageAction;
import com.pubnub.api.models.server.objects_api.EntityEnvelope;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

import java.util.Map;

public interface MessageActionService {

    @POST("v1/message-actions/{subKey}/channel/{channel}/message/{messageTimetoken}")
    @Headers("Content-Type: application/json; charset=UTF-8")
    Call<EntityEnvelope<PNMessageAction>> addMessageAction(@Path("subKey") String subKey,
                                                           @Path("channel") String channel,
                                                           @Path("messageTimetoken") String messageTimetoken,
                                                           @Body Object body,
                                                           @QueryMap(encoded = true) Map<String, String> options);

    @GET("v1/message-actions/{subKey}/channel/{channel}")
    Call<JsonObject> getMessageActions(@Path("subKey") String subKey,
                                       @Path("channel") String channel,
                                       @QueryMap(encoded = true) Map<String, String> options);

    @DELETE("v1/message-actions/{subKey}/channel/{channel}/message/{messageTimetoken}/action/{actionTimetoken}")
    Call<Object> deleteMessageAction(@Path("subKey") String subKey,
                                     @Path("channel") String channel,
                                     @Path("messageTimetoken") String messageTimetoken,
                                     @Path("actionTimetoken") String actionTimetoken,
                                     @QueryMap(encoded = true) Map<String, String> options);

}
