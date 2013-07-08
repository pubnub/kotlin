package com.pubnub.examples.blackberry;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.container.MainScreen;

import com.pubnub.api.Pubnub;

public class Unsubscribe extends PubnubCommand {

    public Unsubscribe(Pubnub pubnub) {
        super(pubnub, "Unsubscribe");
    }

    protected void initScreen() {
        final BasicEditField txtChannel = new BasicEditField("Channel : ", "", 256, BasicEditField.FILTER_DEFAULT);
        screen = new MainScreen();
        screen.add(txtChannel);

        ButtonField btn = new ButtonField();
        btn.setLabel("Unsubscribe");
        screen.add(btn);

        btn.setChangeListener(new FieldChangeListener() {

            public void fieldChanged(Field field, int context) {
                _pubnub.unsubscribe(txtChannel.toString());
                close();
            }
        });
    }

}
