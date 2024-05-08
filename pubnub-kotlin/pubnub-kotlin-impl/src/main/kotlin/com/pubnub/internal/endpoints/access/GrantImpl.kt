package com.pubnub.internal.endpoints.access

import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.endpoints.remoteaction.MappingRemoteAction
import com.pubnub.api.models.consumer.access_manager.PNAccessManagerGrantResult
import com.pubnub.api.models.consumer.access_manager.PNAccessManagerKeyData
import com.pubnub.internal.DelegatingEndpoint
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.grant]
 */
class GrantImpl internal constructor(grant: GrantEndpoint) :
    DelegatingEndpoint<PNAccessManagerGrantResult, com.pubnub.internal.models.consumer.access_manager.PNAccessManagerGrantResult>(
        grant,
    ),
    GrantInterface by grant,
    com.pubnub.api.endpoints.access.Grant {
        override fun convertAction(
            remoteAction: ExtendedRemoteAction<com.pubnub.internal.models.consumer.access_manager.PNAccessManagerGrantResult>,
        ): ExtendedRemoteAction<PNAccessManagerGrantResult> {
            return MappingRemoteAction(remoteAction, ::from)
        }
    }

fun from(result: com.pubnub.internal.models.consumer.access_manager.PNAccessManagerGrantResult): PNAccessManagerGrantResult {
    with(result) {
        return PNAccessManagerGrantResult(
            level,
            ttl,
            subscribeKey,
            channels.toApi(),
            channelGroups.toApi(),
        )
    }
}

private fun Map<String, Map<String, com.pubnub.internal.models.consumer.access_manager.PNAccessManagerKeyData>?>.toApi(): Map<String, Map<String, PNAccessManagerKeyData>?> {
    return mapValues {
        it.value?.mapValues { from(it.value) }
    }
}

fun from(value: com.pubnub.internal.models.consumer.access_manager.PNAccessManagerKeyData): PNAccessManagerKeyData {
    return PNAccessManagerKeyData().apply {
        this.readEnabled = value.readEnabled
        this.manageEnabled = value.manageEnabled
        this.writeEnabled = value.writeEnabled
        this.deleteEnabled = value.deleteEnabled
        this.getEnabled = value.getEnabled
        this.updateEnabled = value.updateEnabled
        this.joinEnabled = value.joinEnabled
    }
}
