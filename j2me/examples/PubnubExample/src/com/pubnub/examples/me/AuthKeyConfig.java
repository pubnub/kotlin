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

public class AuthKeyConfig extends PubnubCommand {

    public AuthKeyConfig(Pubnub pubnub, Display display, Form menu) {
        super(pubnub, display, menu, "Set Auth Keys");
    }

    protected void initForm() {
        final TextField txtAuthKey = new TextField("Auth Key : ", "", 255, TextField.ANY);

        form = new Form("History");
        form.append(txtAuthKey);
        form.addCommand(new Command("Set Auth Key", Command.OK, 2));

        form.setCommandListener(new CommandListener() {
            public void commandAction(Command arg0, Displayable arg1) {
                _pubnub.setAuthKey(txtAuthKey.getString());
                display.setCurrent(menu);
            }
        });

    }

}
