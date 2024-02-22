package com.pubnub.api.endpoints

import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.IDeleteMessages

/**
 * @see [PubNubImpl.deleteMessages]
 */
class DeleteMessages internal constructor(deleteMessages: IDeleteMessages) : IDeleteMessages by deleteMessages
