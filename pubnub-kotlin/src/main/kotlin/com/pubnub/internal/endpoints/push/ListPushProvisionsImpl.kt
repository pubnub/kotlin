package com.pubnub.internal.endpoints.push

import com.pubnub.api.endpoints.push.ListPushProvisions
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.auditPushChannelProvisions]
 */
class ListPushProvisionsImpl internal constructor(listPushProvisions: IListPushProvisions) :
    IListPushProvisions by listPushProvisions, ListPushProvisions
