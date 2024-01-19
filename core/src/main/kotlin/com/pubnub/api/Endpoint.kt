package com.pubnub.api

import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction

interface Endpoint<OUTPUT> : ExtendedRemoteAction<OUTPUT> {
    val queryParam: MutableMap<String, String>
}
