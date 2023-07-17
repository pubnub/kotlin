package com.pubnub.core

interface PNStatus {
    val error: Boolean
    val exception: PubNubException?
}