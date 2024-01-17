package com.pubnub.internal.endpoints.access

import com.google.gson.JsonElement
import com.pubnub.api.PubNubError
import com.pubnub.api.PubNubException
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.PNConfiguration.Companion.isValid
import com.pubnub.internal.PubNub
import com.pubnub.internal.models.consumer.access_manager.PNAccessManagerGrantResult
import com.pubnub.internal.models.consumer.access_manager.PNAccessManagerKeyData
import com.pubnub.internal.models.server.Envelope
import com.pubnub.internal.models.server.access_manager.AccessManagerGrantPayload
import com.pubnub.internal.toCsv
import retrofit2.Call
import retrofit2.Response

/**
 * @see [PubNub.grant]
 */
open class Grant(
    pubnub: PubNub,
    override val read: Boolean = false,
    override val write: Boolean = false,
    override val manage: Boolean = false,
    override val delete: Boolean = false,
    override val ttl: Int = -1,

    override val authKeys: List<String> = emptyList(),
    override val channels: List<String> = emptyList(),
    override val channelGroups: List<String> = emptyList()
) : com.pubnub.internal.Endpoint<Envelope<AccessManagerGrantPayload>, PNAccessManagerGrantResult>(pubnub), IGrant {

    override fun validateParams() {
        super.validateParams()
        if (!pubnub.configuration.secretKey.isValid()) throw PubNubException(PubNubError.SECRET_KEY_MISSING)
    }

    override fun getAffectedChannels() = channels
    override fun getAffectedChannelGroups() = channelGroups

    override fun doWork(queryParams: HashMap<String, String>): Call<Envelope<AccessManagerGrantPayload>> {

        addQueryParams(queryParams)

        return pubnub.retrofitManager.accessManagerService
            .grant(
                subKey = pubnub.configuration.subscribeKey,
                options = queryParams
            )
    }

    override fun createResponse(input: Response<Envelope<AccessManagerGrantPayload>>): PNAccessManagerGrantResult? {
        val data = input.body()!!.payload!!

        val constructedChannels = mutableMapOf<String, Map<String, PNAccessManagerKeyData>?>()
        val constructedGroups = mutableMapOf<String, Map<String, PNAccessManagerKeyData>?>()

        // we have a case of a singular channel.
        data.channel?.let {
            constructedChannels[it] = data.authKeys!!
        }

        if (channelGroups.size == 1) {
            constructedGroups[pubnub.mapper.elementToString(data.channelGroups)!!] = data.authKeys!!
        } else if (channelGroups.size > 1) {
            val it = pubnub.mapper.getObjectIterator(data.channelGroups!!)
            while (it.hasNext()) {
                val (k, v) = it.next()
                constructedGroups[k] = createKeyMap(v)
            }
        }

        data.channels?.forEach {
            constructedChannels[it.key] = data.channels[it.key]!!.authKeys
        }

        return PNAccessManagerGrantResult(
            level = data.level!!,
            ttl = data.ttl,
            subscribeKey = data.subscribeKey!!,
            channels = constructedChannels,
            channelGroups = constructedGroups
        )
    }

    override fun operationType() = PNOperationType.PNAccessManagerGrant

    override fun isAuthRequired() = false

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.ACCESS_MANAGER

    private fun createKeyMap(input: JsonElement): Map<String, PNAccessManagerKeyData> {
        val result: MutableMap<String, PNAccessManagerKeyData> =
            HashMap()
        val it: Iterator<Map.Entry<String, JsonElement>> =
            pubnub.mapper.getObjectIterator(input, "auths")
        while (it.hasNext()) {
            val keyMap = it.next()
            val pnAccessManagerKeyData = PNAccessManagerKeyData()
            pnAccessManagerKeyData.manageEnabled = (pubnub.mapper.getAsBoolean(keyMap.value, "m"))
            pnAccessManagerKeyData.writeEnabled = (pubnub.mapper.getAsBoolean(keyMap.value, "w"))
            pnAccessManagerKeyData.readEnabled = (pubnub.mapper.getAsBoolean(keyMap.value, "r"))
            pnAccessManagerKeyData.deleteEnabled = (pubnub.mapper.getAsBoolean(keyMap.value, "d"))
            result[keyMap.key] = pnAccessManagerKeyData
        }
        return result
    }

    private fun addQueryParams(queryParams: MutableMap<String, String>) {
        channels.run { if (isNotEmpty()) queryParams["channel"] = toCsv() }
        channelGroups.run { if (isNotEmpty()) queryParams["channel-group"] = toCsv() }
        authKeys.run { if (isNotEmpty()) queryParams["auth"] = toCsv() }

        if (ttl >= -1) {
            queryParams["ttl"] = ttl.toString()
        }

        queryParams["r"] = if (read) "1" else "0"
        queryParams["w"] = if (write) "1" else "0"
        queryParams["m"] = if (manage) "1" else "0"
        queryParams["d"] = if (delete) "1" else "0"
    }
}
