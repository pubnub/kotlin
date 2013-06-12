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
import com.pubnub.api.PubnubException;

import org.json.me.JSONException;
import org.json.me.JSONObject;

public class Publish extends PubnubCommand {


    public Publish(Pubnub pubnub, Display display, Form menu) {
        super(pubnub, display,menu, "Publish");
        cmd = new Command("Publish", Command.ITEM, 0);
    }

    protected void initForm() {
        final TextField txtChannel = new TextField("Channel: ", "", 255, TextField.ANY);
        final TextField txtMessage = new TextField("Message: ", "", 255, TextField.ANY);
        form = new Form("Publish");
        form.append(txtChannel);
        form.append(txtMessage);
        form.addCommand(new Command("Publish", Command.OK, 2));

        form.setCommandListener(new CommandListener() {
            public void commandAction(Command arg0, Displayable arg1) {
                try {
                    JSONObject message = new JSONObject();
                    message.put("message", txtMessage.getString());

                    Hashtable args = new Hashtable(2);
                    args.put("channel", txtChannel.getString()); // Channel Name
                    args.put("message", message); // JSON Message
                    _pubnub.publish(args, new Callback() {
                        public void successCallback(String channel, Object message) {
                            notifyUser(message.toString());
                        }

                        public void errorCallback(String channel, PubnubError error) {
                            notifyUser(channel + " : " + error.toString());
                        }
                    });
                    display.setCurrent(menu);

                } catch (JSONException ex) {

                }

            }});

    }

}
