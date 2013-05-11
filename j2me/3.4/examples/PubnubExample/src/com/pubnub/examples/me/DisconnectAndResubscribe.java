package com.pubnub.examples.me;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Form;

import com.pubnub.api.Pubnub;

public class DisconnectAndResubscribe extends PubnubCommand {

    public DisconnectAndResubscribe(Pubnub pubnub, Display display, Form menu) {
        super(pubnub, display, menu, "Disconnect & Resub");
    }

    public void handler() {
        _pubnub.disconnectAndResubscribe();
    }
    protected void initForm() {

    }

}
