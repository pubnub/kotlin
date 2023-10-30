package com.pubnub.api.endpoints.push

import com.pubnub.api.PubNub
import com.pubnub.internal.endpoints.push.IListPushProvisions

/**
 * @see [PubNub.auditPushChannelProvisions]
 */
class ListPushProvisions internal constructor(listPushProvisions: IListPushProvisions) :
    IListPushProvisions by listPushProvisions
