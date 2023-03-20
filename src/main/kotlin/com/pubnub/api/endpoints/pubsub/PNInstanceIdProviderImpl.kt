package com.pubnub.api.endpoints.pubsub

class PNInstanceIdProviderImpl(private val pnInstanceId: String) : PNInstanceIdProvider {
    override fun getPNInstanceId(): String {
        return pnInstanceId
    }
}
