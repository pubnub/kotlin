package com.pubnub.internal.models.server

import com.google.gson.annotations.SerializedName

class SubscribeMetaData(
    @SerializedName("t")
    internal val timetoken: Long,
    @SerializedName("r")
    internal val region: String,
)
