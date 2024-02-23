package com.pubnub.internal.kotlin.endpoints.pubsub

import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.pubsub.IPublish
import com.pubnub.internal.endpoints.pubsub.Publish

/**
 * @see [PubNubImpl.publish]
 */
class PublishImpl internal constructor(publish: Publish) : com.pubnub.api.endpoints.pubsub.Publish,
    IPublish by publish