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

public class Subscribe extends PubnubCommand {

    public Subscribe(Pubnub pubnub, Display display, Form menu) {
        super(pubnub, display, menu, "Subscribe");
    }

    protected void initForm() {
        final TextField txtChannel = new TextField("Channel: ", "", 255, TextField.ANY);
        form = new Form("Subscribe");
        form.append(txtChannel);
        form.addCommand(new Command("Subscribe", Command.OK, 2));

        form.setCommandListener(new CommandListener() {
            public void commandAction(Command arg0, Displayable arg1) {
                Hashtable args = new Hashtable();
                args.put("channel", txtChannel.getString());

                try {
                    _pubnub.subscribe(args, new Callback() {
                        public void connectCallback(String channel, Object message) {
                            notifyUser("CONNECT on channel:" + channel);
                        }

                        public void disconnectCallback(String channel, Object message) {
                            notifyUser("DISCONNECT on channel:" + channel);
                        }

                        public void reconnectCallback(String channel, Object message) {
                            notifyUser("RECONNECT on channel:" + channel);
                        }

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
