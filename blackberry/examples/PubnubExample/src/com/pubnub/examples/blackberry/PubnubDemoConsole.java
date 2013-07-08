package com.pubnub.examples.blackberry;

import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;

/**
 * This class extends the UiApplication class, providing a
 * graphical user interface.
 */
public class PubnubDemoConsole extends UiApplication {
    /**
     * Entry point for application
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {

        // Create a new instance of the application and make the currently
        // running thread the application's event dispatch thread.
        PubnubDemoConsole theApp = new PubnubDemoConsole();
        theApp.enterEventDispatcher();
    }


    /**
     * Creates a new PubnubExample object
     */
    public PubnubDemoConsole() {
        Screen sc = new PubnubDemoConsoleScreen();
        // Push a screen onto the UI stack for rendering.
        pushScreen(sc);
    }

}
