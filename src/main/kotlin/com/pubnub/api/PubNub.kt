package com.pubnub.api

import com.pubnub.api.builder.SubscribeBuilder
import com.pubnub.api.builder.UnsubscribeBuilder
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.endpoints.History
import com.pubnub.api.endpoints.Publish
import com.pubnub.api.endpoints.Time
import com.pubnub.api.endpoints.presence.GetState
import com.pubnub.api.endpoints.presence.HereNow
import com.pubnub.api.endpoints.presence.SetState
import com.pubnub.api.endpoints.presence.WhereNow
import com.pubnub.api.managers.BasePathManager
import com.pubnub.api.managers.MapperManager
import com.pubnub.api.managers.PublishSequenceManager
import com.pubnub.api.managers.RetrofitManager
import com.pubnub.api.managers.SubscriptionManager
import com.pubnub.api.managers.TelemetryManager
import java.util.Date
import java.util.UUID

class PubNub(val configuration: PNConfiguration) {

    private companion object Constants {
        private const val TIMESTAMP_DIVIDER = 1000
        private const val SDK_VERSION = "0.0.1-canary"
    }

    private val basePathManager = BasePathManager(this)

    internal val mapper = MapperManager()
    internal val retrofitManager = RetrofitManager(this)
    internal val publishSequenceManager = PublishSequenceManager()
    internal val telemetryManager = TelemetryManager()
    internal val subscriptionManager = SubscriptionManager(this)

    val version = SDK_VERSION
    val instanceId = UUID.randomUUID().toString()

    fun baseUrl() = basePathManager.basePath()
    fun requestId() = UUID.randomUUID().toString()
    fun timestamp() = Date().time / TIMESTAMP_DIVIDER

    fun publish() = Publish(this)
    fun subscribe() = SubscribeBuilder(subscriptionManager)
    fun unsubscribe() = UnsubscribeBuilder(subscriptionManager)
    fun history() = History(this)
    fun hereNow() = HereNow(this)
    fun whereNow() = WhereNow(this)
    fun setState() = SetState(this)
    fun getState() = GetState(this)
    fun time() = Time(this)

    fun addListener(listener: SubscribeCallback) {
        subscriptionManager.addListener(listener)
    }

    fun destroy() {
        subscriptionManager.destroy()
        retrofitManager.destroy()
    }

}