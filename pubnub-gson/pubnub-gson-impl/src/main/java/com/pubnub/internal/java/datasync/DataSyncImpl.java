package com.pubnub.internal.java.datasync;

import com.pubnub.api.PubNub;
import com.pubnub.api.java.datasync.DataSync;
import com.pubnub.api.java.datasync.EntityApi;
import com.pubnub.api.java.datasync.UserApi;

public class DataSyncImpl implements DataSync {
    private final PubNub pubnubInstance;

    public DataSyncImpl(PubNub pubnubInstance) {
        this.pubnubInstance = pubnubInstance;
    }

    @Override
    public EntityApi entity() {
        return new EntityApiImpl(pubnubInstance);
    }

    @Override
    public UserApi user() {
        return new UserApiImpl(pubnubInstance);
    }
}
