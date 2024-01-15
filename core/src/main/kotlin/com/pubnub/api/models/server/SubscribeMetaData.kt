package com.pubnub.api.models.server

import com.google.gson.annotations.SerializedName

class SubscribeMetaData(
    @SerializedName("t")
    internal val timetoken: Long,

    @SerializedName("r")
    internal val region: String
)
