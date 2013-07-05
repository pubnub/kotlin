package com.pubnub.examples.me;

import java.util.Hashtable;

import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;

public class DetailedHistory extends PubnubCommand {

    public DetailedHistory(Pubnub pubnub, Display display, Form menu) {
        super(pubnub, display, menu, "Detailed History");
    }

    protected void initForm() {
        final TextField txtChannel = new TextField("Channel : ", "", 255, TextField.ANY);
        final TextField txtCount = new TextField("Count : ", "1", 10, TextField.NUMERIC);
        final ChoiceGroup cg = new ChoiceGroup("Reverse?",ChoiceGroup.EXCLUSIVE);
        cg.append("Yes", null);
        cg.append("No", null);

        form = new Form("History");
        form.append(txtChannel);
        form.append(txtCount);
        form.append(cg);
        form.addCommand(new Command("Get History", Command.OK, 2));

        form.setCommandListener(new CommandListener() {
            public void commandAction(Command arg0, Displayable arg1) {


                _pubnub.detailedHistory(txtChannel.getString(),
                                        Integer.parseInt(txtCount.getString()),
                                        cg.isSelected(0)?true:false,
                new Callback() {
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
