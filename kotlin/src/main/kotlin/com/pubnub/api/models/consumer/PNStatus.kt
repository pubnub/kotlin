package com.pubnub.api.models.consumer

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNubException
import com.pubnub.api.endpoints.remoteaction.ExtendedRemoteAction
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.core.PNErrorData
import com.pubnub.core.Status
import okhttp3.Request

/**
 * Metadata related to executed PubNub API operations.
 *
 * @property category The [PNStatusCategory] of an executed operation.
 * @property error Is `true` if the operation didn't succeed. Always check for it in [async][Endpoint.async] blocks.
 * @property operation The concrete API operation type that's been executed.
 * @property exception Error information if the request didn't succeed.
 * @property statusCode HTTP status code.
 * @property tlsEnabled Whether the API operation was executed over HTTPS.
 * @property origin Origin of the HTTP request. See more at [PNConfiguration.origin].
 * @property uuid The UUID which requested the API operation to be executed. See more at [PNConfiguration.uuid].
 * @property authKey The authentication key attached to the request, if needed. See more at [PNConfiguration.authKey].
 * @property affectedChannels List of channels affected by the this API operation.
 * @property affectedChannelGroups List of channel groups affected by the this API operation.
 */
data class PNStatus(
    override var category: PNStatusCategory,
    override var error: Boolean,
    override val operation: PNOperationType,

    override val exception: PubNubException? = null,

    override var statusCode: Int? = null,
    override var tlsEnabled: Boolean? = null,
    override var origin: String? = null,
    override var uuid: String? = null,
    override var authKey: String? = null,

    override var affectedChannels: List<String> = emptyList(),
    override var affectedChannelGroups: List<String> = emptyList(),
    override val errorData: PNErrorData? = exception?.let { PNErrorData(it.message, it) }

) : Status {
    internal var executedEndpoint: ExtendedRemoteAction<*>? = null

    var clientRequest: Request? = null

    /**
     * Execute the API operation again.
     */
    fun retry() {
        executedEndpoint?.retry()
    }
}
