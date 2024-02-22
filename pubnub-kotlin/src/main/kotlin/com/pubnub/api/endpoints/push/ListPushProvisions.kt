package com.pubnub.api.endpoints.push

import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.push.IListPushProvisions

/**
 * @see [PubNubImpl.auditPushChannelProvisions]
 */
class ListPushProvisions internal constructor(listPushProvisions: IListPushProvisions) :
    IListPushProvisions by listPushProvisions
