package com.pubnub.internal.endpoints.pubsub

import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.publish]
 */
class PublishImpl internal constructor(publish: Publish) :
    com.pubnub.api.endpoints.pubsub.Publish,
    IPublish by publish
