package com.pubnub.examples.blackberry;

import com.pubnub.api.Pubnub;

public class ToggleResumeOnReconnect extends PubnubCommand {

    public ToggleResumeOnReconnect(Pubnub pubnub) {
        super(pubnub, "Toggle Resume on Reconnect");
    }

    protected void initScreen() {

    }
    public void handler() {
        _pubnub.setResumeOnReconnect((_pubnub.isResumeOnReconnect())?false:true);
    }

}
