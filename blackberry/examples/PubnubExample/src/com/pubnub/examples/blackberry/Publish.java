package com.pubnub.examples.blackberry;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.container.MainScreen;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;

public class Publish extends PubnubCommand {

    public Publish(Pubnub pubnub) {
        super(pubnub, "Publish");
    }

    protected void initScreen() {
        final BasicEditField txtChannel = new BasicEditField("Channel : ", "", 256, BasicEditField.FILTER_DEFAULT);
        final BasicEditField txtMessage = new BasicEditField("Message : ", "",256, BasicEditField.FILTER_DEFAULT);
        screen = new MainScreen();
        screen.add(txtChannel);
        screen.add(txtMessage);

        ButtonField btn = new ButtonField();
        btn.setLabel("Publish");
        screen.add(btn);

        btn.setChangeListener(new FieldChangeListener() {

            public void fieldChanged(Field field, int context) {

                _pubnub.publish(txtChannel.toString(), txtMessage.toString(), new Callback() {
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
