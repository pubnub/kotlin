package com.pubnub.examples.blackberry;

import com.pubnub.api.Pubnub;

public class DisconnectAndResubscribe extends PubnubCommand {

    public DisconnectAndResubscribe(Pubnub pubnub) {
        super(pubnub, "Disconnect & Resubscribe");
    }

    protected void initScreen() {

    }
    public void handler() {
        _pubnub.disconnectAndResubscribe();
    }

}
