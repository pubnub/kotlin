package com.pubnub.api

import com.pubnub.api.endpoints.History
import com.pubnub.api.endpoints.Publish
import com.pubnub.api.endpoints.Time
import com.pubnub.api.managers.BasePathManager
import com.pubnub.api.managers.MapperManager
import com.pubnub.api.managers.PublishSequenceManager
import com.pubnub.api.managers.RetrofitManager
import java.util.*

class PubNub(val config: PNConfiguration) {

    companion object Constants {
        private const val TIMESTAMP_DIVIDER = 1000
        private const val SDK_VERSION = "4.29.1"
    }

    internal val mapper = MapperManager()
    private val basePathManager = BasePathManager(this)
    internal val retrofitManager = RetrofitManager(this)
    internal val publishSequenceManager = PublishSequenceManager()

    val version = SDK_VERSION
    val instanceId = UUID.randomUUID().toString()

    fun baseUrl() = basePathManager.basePath()
    fun requestId() = UUID.randomUUID().toString()
    fun timestamp() = Date().time / TIMESTAMP_DIVIDER

    inline fun time(function: Time.Params.() -> Unit): Time {
        val time = Time(this)
        time.params.function()
        return time
    }

    inline fun publish(function: Publish.Params.() -> Unit): Publish {
        val publish = Publish(this)
        publish.params.function()
        return publish
    }

    inline fun history(function: History.Params.() -> Unit): History {
        val history = History(this)
        history.params.function()
        return history
    }

}