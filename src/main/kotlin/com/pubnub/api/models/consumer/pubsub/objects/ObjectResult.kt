package com.pubnub.api.models.consumer.pubsub.objects

import com.pubnub.api.models.consumer.pubsub.BasePubSubResult

open class ObjectResult<T>(
    open val result: BasePubSubResult,
    open val event: String,
    open val data: T
) : BasePubSubResult(result)