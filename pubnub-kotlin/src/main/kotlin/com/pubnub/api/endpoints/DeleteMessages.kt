package com.pubnub.api.endpoints

import com.pubnub.api.PubNub
import com.pubnub.internal.endpoints.IDeleteMessages

/**
 * @see [PubNub.deleteMessages]
 */
class DeleteMessages internal constructor(deleteMessages: IDeleteMessages) : IDeleteMessages by deleteMessages
