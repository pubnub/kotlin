package com.pubnub.examples.blackberry;

import net.rim.device.api.ui.container.MainScreen;

import com.pubnub.api.Pubnub;

/**
 * A class extending the MainScreen class, which provides default standard
 * behavior for BlackBerry GUI applications.
 */
public final class PubnubDemoConsoleScreen extends MainScreen {
    String channel = "hello_world";
    String[] channels = { "hello_world1", "hello_world2", "hello_world3",
                          "hello_world4"
                        };
    Pubnub _pubnub = new Pubnub("demo","demo","demo", false);
    /**
     * Creates a new PubnubExampleScreen object
     */
    public PubnubDemoConsoleScreen() {
        // Set the displayed title of the screen
        setTitle("Pubnub Demo Console");
        addMenuItem(new Publish(_pubnub).getMenuItem());
        addMenuItem(new Subscribe(_pubnub).getMenuItem());
        addMenuItem(new Presence(_pubnub).getMenuItem());
        addMenuItem(new Unsubscribe(_pubnub).getMenuItem());
        addMenuItem(new History(_pubnub).getMenuItem());
        addMenuItem(new DetailedHistory(_pubnub).getMenuItem());
        addMenuItem(new HereNow(_pubnub).getMenuItem());
        addMenuItem(new AuthKeyConfig(_pubnub).getMenuItem());
        addMenuItem(new DisconnectAndResubscribe(_pubnub).getMenuItem());
        addMenuItem(new ToggleResumeOnReconnect(_pubnub).getMenuItem());
        addMenuItem(new Time(_pubnub).getMenuItem());
    }


}