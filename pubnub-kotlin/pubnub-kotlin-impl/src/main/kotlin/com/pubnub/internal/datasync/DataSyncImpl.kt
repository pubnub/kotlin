package com.pubnub.internal.datasync

import com.pubnub.api.datasync.DataSync
import com.pubnub.api.datasync.EntityApi
import com.pubnub.api.datasync.UserApi
import com.pubnub.internal.PubNubImpl

class DataSyncImpl(pubnub: PubNubImpl) : DataSync {
    override val entity: EntityApi = EntityApiImpl(pubnub)
    override val user: UserApi = UserApiImpl(pubnub)
}
