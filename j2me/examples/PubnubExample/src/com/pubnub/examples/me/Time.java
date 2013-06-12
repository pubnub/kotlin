package com.pubnub.examples.me;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Form;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;

public class Time extends PubnubCommand {

    public Time(Pubnub pubnub, Display display, Form menu) {
        super(pubnub, display, menu, "Time");
    }

    protected void initForm() {
    }
    public void handler() {
        _pubnub.time( new Callback() {
            public void successCallback(String channel, Object message) {
                notifyUser(message.toString());
            }

            public void errorCallback(String channel, PubnubError error) {
                notifyUser(error.toString());
            }
        });
    }
}
