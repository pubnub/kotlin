package com.pubnub.examples.me;

import javax.microedition.midlet.MIDlet;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubException;

import java.util.Enumeration;
import java.util.Hashtable;
import javax.microedition.lcdui.*;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

public class PubnubDemoConsole extends MIDlet {

    private boolean midletPaused = false;

    Pubnub _pubnub;
    Display display;

    PubnubMenu menu;

    public PubnubDemoConsole() {
    }





    private void init() {
        _pubnub = new Pubnub("demo", "demo", "demo", false);
        display = Display.getDisplay(this);
        menu = new PubnubMenu(_pubnub, display, this);

    }

    /**
     * Performs an action assigned to the Mobile Device - MIDlet Started point.
     */
    public void startMIDlet() {
        init();
        switchDisplayable(null, menu.getMenu());
        _pubnub.setResumeOnReconnect(true);
    }

    /**
     * Performs an action assigned to the Mobile Device - MIDlet Resumed point.
     */
    public void resumeMIDlet() {
    }

    public void switchDisplayable(Alert alert, Displayable nextDisplayable) {
        Display display = this.display;
        if (alert == null) {
            display.setCurrent(nextDisplayable);
        } else {
            display.setCurrent(alert, nextDisplayable);
        }
    }

    /**
     * Exits MIDlet.
     */
    public void exitMIDlet() {
        switchDisplayable(null, null);
        destroyApp(true);
        notifyDestroyed();
    }

    /**
     * Called when MIDlet is started. Checks whether the MIDlet have been
     * already started and initialize/starts or resumes the MIDlet.
     */
    public void startApp() {

        if (midletPaused) {
            resumeMIDlet();
        } else {

            startMIDlet();
        }
        midletPaused = false;
    }

    /**
     * Called when MIDlet is paused.
     */
    public void pauseApp() {
        midletPaused = true;
    }

    /**
     * Called to signal the MIDlet to terminate.
     *
     * @param unconditional
     *            if true, then the MIDlet has to be unconditionally terminated
     *            and all resources has to be released.
     */
    public void destroyApp(boolean unconditional) {
    }


}
