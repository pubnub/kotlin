package com.pubnub.examples.blackberry;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.container.MainScreen;

import com.pubnub.api.Pubnub;

public class AuthKeyConfig extends PubnubCommand {

    public AuthKeyConfig(Pubnub pubnub) {
        super(pubnub, "Auth Key Config");
    }

    protected void initScreen() {
        final BasicEditField txtAuthKey = new BasicEditField("Auth Key : ", "", 256, BasicEditField.FILTER_DEFAULT);
        screen = new MainScreen();
        screen.add(txtAuthKey);

        ButtonField btn = new ButtonField();
        btn.setLabel("Set Auth Key");
        screen.add(btn);

        btn.setChangeListener(new FieldChangeListener() {

            public void fieldChanged(Field field, int context) {

                try {
                    _pubnub.setAuthKey(txtAuthKey.getText());
                    close();
                } catch (Exception e) {

                }
            }});
    }
}


