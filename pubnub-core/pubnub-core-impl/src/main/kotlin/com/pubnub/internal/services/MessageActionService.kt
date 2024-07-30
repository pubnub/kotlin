package com.pubnub.internal.services

import com.pubnub.api.models.consumer.message_actions.PNGetMessageActionsResult
import com.pubnub.api.models.consumer.message_actions.PNSavedMessageAction
import com.pubnub.internal.models.server.objects_api.EntityEnvelope
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.QueryMap

internal interface
MessageActionService {
    @POST("v1/message-actions/{subKey}/channel/{channel}/message/{messageTimetoken}")
    @Headers("Content-Type: application/json; charset=UTF-8")
    fun addMessageAction(
        @Path("subKey") subKey: String,
        @Path("channel") channel: String,
        @Path("messageTimetoken") messageTimetoken: String,
        @Body body: Any,
        @QueryMap options: Map<String, String>,
    ): Call<EntityEnvelope<PNSavedMessageAction>>

    @GET("v1/message-actions/{subKey}/channel/{channel}")
    fun getMessageActions(
        @Path("subKey") subKey: String,
        @Path("channel") channel: String,
        @QueryMap options: Map<String, String>,
    ): Call<PNGetMessageActionsResult>

    @DELETE("v1/message-actions/{subKey}/channel/{channel}/message/{messageTimetoken}/action/{actionTimetoken}")
    fun deleteMessageAction(
        @Path("subKey") subKey: String,
        @Path("channel") channel: String,
        @Path("messageTimetoken") messageTimetoken: String,
        @Path("actionTimetoken") actionTimetoken: String,
        @QueryMap options: Map<String, String>,
    ): Call<Void>
}
