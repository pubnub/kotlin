package com.pubnub.examples.me;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Form;

import com.pubnub.api.Pubnub;

public class ToggleResumeOnReconnect extends PubnubCommand {

    public ToggleResumeOnReconnect(Pubnub pubnub, Display display, Form menu) {
        super(pubnub, display, menu, "Toggle Resume on Reconnect");
    }

    public void handler() {
        _pubnub.setResumeOnReconnect((_pubnub.isResumeOnReconnect())?false:true);
    }

    protected void initForm() {

    }

}
