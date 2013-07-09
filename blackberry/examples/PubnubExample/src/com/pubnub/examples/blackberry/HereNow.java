package com.pubnub.examples.blackberry;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.container.MainScreen;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;

public class HereNow extends PubnubCommand {

    public HereNow(Pubnub pubnub) {
        super(pubnub, "Here Now");
    }

    protected void initScreen() {
        final BasicEditField txtChannel = new BasicEditField("Channel : ", "", 256, BasicEditField.FILTER_DEFAULT);
        screen = new MainScreen();
        screen.add(txtChannel);

        ButtonField btn = new ButtonField();
        btn.setLabel("Get Here Now Data");
        screen.add(btn);

        btn.setChangeListener(new FieldChangeListener() {

            public void fieldChanged(Field field, int context) {
                _pubnub.hereNow(txtChannel.toString(), new Callback() {
                    public void successCallback(String channel, Object message) {
                        notifyUser(message.toString());
                    }

                    public void errorCallback(String channel, PubnubError error) {
                        notifyUser(channel + " : " + error.toString());
                    }
                });
                close();
            }
        });
    }

}
