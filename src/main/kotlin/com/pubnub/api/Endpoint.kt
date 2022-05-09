package com.pubnub.api

import com.pubnub.api.PNConfiguration.Companion.isValid
import com.pubnub.api.enums.PNOperationType
import retrofit2.Call
import retrofit2.Response

/**
 * Base class for all PubNub API operation implementations.
 *
 * @param Input Server's response.
 * @param Output Parsed and encapsulated response for endusers.
 * @property pubnub The client instance.
 */
abstract class Endpoint<Input, Output> protected constructor(pubnub: PubNub) :
    com.pubnub.api.endpoints.Endpoint<Input, Output>(
        pubnub,
        pubnub.telemetryManager,
        pubnub.retrofitManager,
        pubnub.tokenManager
    ) {
    override fun getAffectedChannels(): List<String> = emptyList()
    override fun getAffectedChannelGroups(): List<String> = emptyList()

    protected override fun validateParams() {
        if (isSubKeyRequired() && !pubnub.configuration.subscribeKey.isValid()) {
            throw PubNubException(PubNubError.SUBSCRIBE_KEY_MISSING)
        }
        if (isPubKeyRequired() && !pubnub.configuration.publishKey.isValid()) {
            throw PubNubException(PubNubError.PUBLISH_KEY_MISSING)
        }
    }

    override fun getOperationType(): PNOperationType = operationType()

    protected open fun isSubKeyRequired() = true
    protected open fun isPubKeyRequired() = false
    override fun isAuthRequired() = true
}
