package com.pubnub.api.models.consumer.pubsub.objects

import com.pubnub.api.models.consumer.pubsub.BasePubSubResult

open class ObjectResult<T>(
    internal open val result: BasePubSubResult,
    internal open val event: String,
    internal open val data: T
) : BasePubSubResult(result)