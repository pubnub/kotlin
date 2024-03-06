package com.pubnub.api.models.server

import com.google.gson.annotations.SerializedName

class Cursor(
    @SerializedName("t")
    internal val timeToken: Long,

    @SerializedName("r")
    internal val region: Int
)
