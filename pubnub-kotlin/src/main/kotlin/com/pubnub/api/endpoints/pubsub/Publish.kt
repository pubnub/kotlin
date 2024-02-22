package com.pubnub.api.endpoints.pubsub

import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.pubsub.IPublish

/**
 * @see [PubNubImpl.publish]
 */
class Publish internal constructor(publish: IPublish) : IPublish by publish
