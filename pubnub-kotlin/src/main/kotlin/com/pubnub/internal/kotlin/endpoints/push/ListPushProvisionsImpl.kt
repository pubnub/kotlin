package com.pubnub.internal.kotlin.endpoints.push

import com.pubnub.api.endpoints.push.ListPushProvisions
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.push.IListPushProvisions

/**
 * @see [PubNubImpl.auditPushChannelProvisions]
 */
class ListPushProvisionsImpl internal constructor(listPushProvisions: IListPushProvisions) :
    IListPushProvisions by listPushProvisions, ListPushProvisions
