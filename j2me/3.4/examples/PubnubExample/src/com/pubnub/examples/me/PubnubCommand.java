package com.pubnub.examples.me;

import java.util.Enumeration;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Form;

import org.json.me.JSONArray;
import org.json.me.JSONObject;

import com.pubnub.api.Pubnub;

public abstract class PubnubCommand {

    protected Display display;
    protected Form menu;
    protected Form form;
    protected Pubnub _pubnub;

    protected Command cmd;


    protected abstract void initForm();

    public PubnubCommand(Pubnub pubnub, Display display, Form menu, String command) {
        this.display = display;
        this.menu = menu;
        this._pubnub = pubnub;
        this.cmd = new Command(command, Command.ITEM, 0);
    }

    public void notifyUser(Object message) {
        try {
            if (message instanceof JSONObject) {
                JSONObject obj = (JSONObject) message;
                Alert a = new Alert("Received", obj.toString(), null, null);
                a.setTimeout(Alert.FOREVER);
                display.setCurrent(a, menu);

                Enumeration keys = obj.keys();
                while (keys.hasMoreElements()) {
                }

            } else if (message instanceof String) {
                String obj = (String) message;
                Alert a = new Alert("Received", obj.toString(), null, null);
                a.setTimeout(Alert.FOREVER);
                display.setCurrent(a, menu);
            } else if (message instanceof JSONArray) {
                JSONArray obj = (JSONArray) message;
                Alert a = new Alert("Received", obj.toString(), null, null);
                a.setTimeout(Alert.FOREVER);
                display.setCurrent(a, menu);
            }
        } catch (Exception e) {

        }

    }

    public void handler() {

        if (form == null)
            initForm();
        display.setCurrent(form);

    }

    public Command getCommand() {
        return cmd;
    }

}
