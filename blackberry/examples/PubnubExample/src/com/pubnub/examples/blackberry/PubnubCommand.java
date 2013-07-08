package com.pubnub.examples.blackberry;

import net.rim.device.api.command.Command;
import net.rim.device.api.command.CommandHandler;
import net.rim.device.api.command.ReadOnlyCommandMetadata;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.util.StringProvider;

import org.json.me.JSONArray;
import org.json.me.JSONObject;

import com.pubnub.api.Pubnub;

public abstract class PubnubCommand {

    protected Pubnub _pubnub;

    protected MainScreen screen;

    private UiApplication uiapp;

    private MenuItem menuItem;



    /**
     * Presents a dialog to the user with a given message
     *
     * @param message
     *            The text to display
     */
    private void alertDialog(final String message) {
        uiapp.invokeLater(new Runnable() {
            public void run() {
                Dialog.alert(message);
            }
        });
    }

    public PubnubCommand(Pubnub pubnub, String command) {
        this._pubnub = pubnub;
        this.uiapp = UiApplication.getUiApplication();
        this.menuItem = new MenuItem(new StringProvider(command), 0x230010, 0);
        this.menuItem.setCommand(new Command(new CommandHandler() {
            public void execute(ReadOnlyCommandMetadata metadata, Object context) {
                handler();
            }
        }));
    }

    protected void notifyUser(Object message) {
        try {
            if (message instanceof JSONObject) {
                JSONObject obj = (JSONObject) message;
                alertDialog(obj.toString());
            } else if (message instanceof String) {
                String obj = (String) message;
                alertDialog(obj.toString());
            } else if (message instanceof JSONArray) {
                JSONArray obj = (JSONArray) message;
                alertDialog(obj.toString());
            }
        } catch (Exception e) {

        }

    }

    protected abstract void initScreen();

    public void handler() {

        if (screen == null)
            initScreen();
        uiapp.pushScreen(screen);

    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    protected void close() {
        this.uiapp.popScreen(screen);
    }
}
