package com.pubnub.entities.models.consumer.membership

import com.pubnub.entities.models.consumer.space.Space
import com.pubnub.entities.models.consumer.user.User

data class Membership(
    val user: User?,
    val space: Space?,
    val custom: Any?,
    val updated: String,
    val eTag: String
)
