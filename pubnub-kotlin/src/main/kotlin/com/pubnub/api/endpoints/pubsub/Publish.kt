package com.pubnub.api.endpoints.pubsub

import com.pubnub.api.PubNub
import com.pubnub.internal.endpoints.pubsub.IPublish

/**
 * @see [PubNub.publish]
 */
class Publish internal constructor(publish: IPublish) : IPublish by publish
