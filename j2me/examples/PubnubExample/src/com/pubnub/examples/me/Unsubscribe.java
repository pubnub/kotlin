package com.pubnub.examples.me;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

import com.pubnub.api.Pubnub;

public class Unsubscribe extends PubnubCommand {

    public Unsubscribe(Pubnub pubnub, Display display, Form menu) {
        super(pubnub, display, menu, "Unsubscribe");
    }

    protected void initForm() {
        final TextField txtChannel = new TextField("Channel: ", "", 255, TextField.ANY);
        form = new Form("Unsubscribe");
        form.append(txtChannel);
        form.addCommand(new Command("Unsubscribe", Command.OK, 2));

        form.setCommandListener(new CommandListener() {
            public void commandAction(Command arg0, Displayable arg1) {
                _pubnub.unsubscribe(txtChannel.getString());
                display.setCurrent(menu);

            }
        });


    }

}
