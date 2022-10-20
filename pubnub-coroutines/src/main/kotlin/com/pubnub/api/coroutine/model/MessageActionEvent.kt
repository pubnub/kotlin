package com.pubnub.api.coroutine.model

data class MessageActionEvent(
    val type: String,
    val value: String,
    val messageTimetoken: Long,
    val actionTimetoken: Long?,
    val uuid: String?
)
