package com.pubnub.examples.me;

import java.util.Hashtable;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;

public class Presence extends PubnubCommand {

    public Presence(Pubnub pubnub, Display display, Form menu) {
        super(pubnub, display, menu, "Presence");
    }

    protected void initForm() {
        final TextField txtChannel = new TextField("Channel: ", "", 255, TextField.ANY);
        form = new Form("Subscribe");
        form.append(txtChannel);
        form.addCommand(new Command("Subscribe", Command.OK, 2));

        form.setCommandListener(new CommandListener() {
            public void commandAction(Command arg0, Displayable arg1) {

                try {
                    _pubnub.presence(txtChannel.getString(), new Callback() {
                        public void successCallback(String channel, Object message) {
                            notifyUser("Channel " + channel + " : " + message.toString());
                        }
                        public void errorCallback(String channel, PubnubError error) {
                            notifyUser("Channel " + channel + " : " + error.toString());
                        }
                    });
                    display.setCurrent(menu);

                } catch (Exception e) {

                }

            }});

    }

}
