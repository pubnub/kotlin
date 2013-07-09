package com.pubnub.examples.me;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;

public class HereNow extends PubnubCommand {

    public HereNow(Pubnub pubnub, Display display, Form menu) {
        super(pubnub, display, menu,"Here Now");
    }

    protected void initForm() {
        final TextField txtChannel = new TextField("Channel: ", "", 255, TextField.ANY);
        form = new Form("Here Now");
        form.append(txtChannel);
        form.addCommand(new Command("Here Now", Command.OK, 2));

        form.setCommandListener(new CommandListener() {
            public void commandAction(Command arg0, Displayable arg1) {
                _pubnub.hereNow(txtChannel.getString(), new Callback() {
                    public void successCallback(String channel, Object message) {
                        notifyUser(message.toString());
                    }

                    public void errorCallback(String channel, PubnubError error) {
                        notifyUser(channel + " : " + error.toString());
                    }
                });
                display.setCurrent(menu);

            }
        });

    }

}
