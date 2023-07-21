package com.pubnub.api

import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.enums.PNStatusCategory
import com.pubnub.api.models.consumer.PNErrorData
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.core.CoreException
import com.pubnub.core.PNStatusFactory
import com.pubnub.core.Status

class KotlinPNStatusFactory : PNStatusFactory {
    override fun createPNStatus(
        category: PNStatusCategory,
        error: Boolean,
        operation: PNOperationType,
        affectedChannels: List<String>,
        affectedChannelGroups: List<String>,
        exception: CoreException?,
        statusCode: Int?,
        tlsEnabled: Boolean?,
        origin: String?,
        uuid: String?,
        authKey: String?,
        errorData: PNErrorData?
    ): Status = PNStatus(
        category = category,
        error = error,
        operation = operation,
        affectedChannels = affectedChannels,
        affectedChannelGroups = affectedChannelGroups,
        exception = exception?.let { PubNubException(it) },
        statusCode = statusCode,
        tlsEnabled = tlsEnabled,
        origin = origin,
        uuid = uuid,
        authKey = authKey
    )
}
