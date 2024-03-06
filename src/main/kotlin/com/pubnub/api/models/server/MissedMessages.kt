package com.pubnub.api.models.server

import com.google.gson.annotations.SerializedName

class MissedMessages(
    @SerializedName("c")
    internal val channel: String,

    @SerializedName("s")
    internal val startingCursor: Cursor,

    @SerializedName("e")
    internal val endingCursor: Cursor
)
