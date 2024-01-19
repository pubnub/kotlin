package com.pubnub.api.integration.pam

import com.pubnub.internal.PubNub

class PamServerIntegrationTest : AccessManagerIntegrationTest() {

    override fun getPamLevel() = LEVEL_APP

    override fun performOnServer() = true

    override val pubnubToTest: PubNub = server
}
