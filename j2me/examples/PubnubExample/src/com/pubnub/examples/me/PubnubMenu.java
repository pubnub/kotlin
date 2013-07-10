package com.pubnub.examples.me;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;

import com.pubnub.api.Pubnub;

public class PubnubMenu {
    private Form menu;
    private Pubnub pubnub;
    private Display display;
    private PubnubDemoConsole app;
    public PubnubMenu(Pubnub pubnub, Display display, PubnubDemoConsole app) {
        this.pubnub = pubnub;
        this.display = display;
        this.app = app;
    }

    public Form getMenu() {
        final Publish publish;
        final History history;
        final Subscribe subscribe;
        final DetailedHistory detailedHistory;
        final HereNow hereNow;
        final Presence presence;
        final Unsubscribe unsubscribe;

        final ToggleResumeOnReconnect toggleResumeOnReconnect;
        final Time time;
        final DisconnectAndResubscribe disconnectAndResubscribe;
        final AuthKeyConfig authKeyConfig;
        if (menu == null) {
            final Command exitCommand;
            final Command publishCommand;
            final Command historyCommand;
            final Command subscribeCommand;
            final Command timeCommand;

            final Command unsubscribeCommand;

            final Command hereNowCommand;
            final Command presenceCommand;
            final Command detailedHistoryCommand;
            final Command disconnectAndResubscribeCommand;
            final Command toggleResumeOnReconnectCommand;
            final Command authKeyConfigCommand;

            menu = new Form("Pubnub Demo Console");
            publish = new Publish(pubnub, display, menu);
            history = new History(pubnub, display, menu);
            subscribe = new Subscribe(pubnub,display,menu);
            presence = new Presence(pubnub,display,menu);
            detailedHistory = new DetailedHistory(pubnub,display,menu);
            hereNow = new HereNow(pubnub,display,menu);
            unsubscribe = new Unsubscribe(pubnub,display,menu);
            toggleResumeOnReconnect = new ToggleResumeOnReconnect(pubnub,display,menu);
            disconnectAndResubscribe = new DisconnectAndResubscribe(pubnub,display,menu);
            authKeyConfig = new AuthKeyConfig(pubnub,display,menu);
            time = new Time(pubnub,display,menu);


            toggleResumeOnReconnectCommand = new Command("ToggleResumeOnReconnect",    Command.ITEM, 0);
            menu.addCommand(toggleResumeOnReconnectCommand);
            disconnectAndResubscribeCommand = new Command("DisconnectAndResubscribe",Command.ITEM, 0);
            menu.addCommand(disconnectAndResubscribeCommand);
            detailedHistoryCommand = new Command("DetailedHistory",    Command.ITEM, 0);
            menu.addCommand(detailedHistoryCommand);
            presenceCommand = new Command("Presence", Command.ITEM, 0);
            menu.addCommand(presenceCommand);
            publishCommand = publish.getCommand();
            menu.addCommand(publishCommand);
            timeCommand = new Command("Time", Command.ITEM, 2);
            menu.addCommand(timeCommand);
            historyCommand = new Command("History", Command.ITEM, 1);
            menu.addCommand(historyCommand);
            subscribeCommand = new Command("Subscribe", Command.ITEM, 0);
            menu.addCommand(subscribeCommand);
            unsubscribeCommand = new Command("Unsubscribe", Command.ITEM, 0);
            menu.addCommand(unsubscribeCommand);

            hereNowCommand = new Command("HereNow", Command.ITEM, 1);
            menu.addCommand(hereNowCommand);
            exitCommand = new Command("Exit", Command.EXIT, 0);
            menu.addCommand(exitCommand);
            authKeyConfigCommand = new Command("Set Auth Key", Command.ITEM, 0);
            menu.addCommand(authKeyConfigCommand);

            menu.setCommandListener(new CommandListener() {

                public void commandAction(Command command, Displayable displayable) {
                    if (command == exitCommand) {
                        app.exitMIDlet();
                    } else if (command == historyCommand) {
                        history.handler();
                    } else if (command == publishCommand) {
                        publish.handler();
                    } else if (command == subscribeCommand) {
                        subscribe.handler();
                    } else if (command == timeCommand) {
                        time.handler();
                    } else if (command == unsubscribeCommand) {
                        unsubscribe.handler();
                    } else if (command == hereNowCommand) {
                        hereNow.handler();
                    } else if (command == presenceCommand) {
                        presence.handler();
                    } else if (command == detailedHistoryCommand) {
                        detailedHistory.handler();
                    } else if (command == disconnectAndResubscribeCommand) {
                        disconnectAndResubscribe.handler();
                    } else if (command == toggleResumeOnReconnectCommand) {
                        toggleResumeOnReconnect.handler();
                    } else if (command == authKeyConfigCommand) {
                        authKeyConfig.handler();
                    }


                }
            });
        }

        return menu;

    }
}
