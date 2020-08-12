package com.pubnub.api.integration.pam

class PamServerIntegrationTest : AccessManagerIntegrationTest() {

    override fun getPamLevel() = LEVEL_APP

    override fun performOnServer() = true
}
