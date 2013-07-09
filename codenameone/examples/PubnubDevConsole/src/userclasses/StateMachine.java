/**
 * Your application code goes here
 */

package userclasses;

import generated.StateMachineBase;
import com.codename1.ui.*;
import com.codename1.ui.events.*;
import com.codename1.ui.util.Resources;

import com.pubnub.api.*;


/**
 *
 * @author Your name here
 */
public class StateMachine extends StateMachineBase {
    Pubnub pubnub;
    public StateMachine(String resFile) {
        super(resFile);
        // do not modify, write code in initVars and initialize class members there,
        // the constructor might be invoked too late due to race conditions that might occur
    }

    /**
     * this method should be used to initialize variables instead of
     * the constructor/class scope to avoid race conditions
     */
    protected void initVars(Resources res) {
        pubnub = new Pubnub("demo", "demo");
    }




    @Override
    protected void onPublish_BtnPublishOkAction(Component c, ActionEvent event) {
        String channel = findTxtPublishChannel().getText();
        String message = findTxtPublishMessage().getText();
        pubnub.publish(channel, message, new Callback() {

            @Override
            public void successCallback(String channel, Object message) {
                new Dialog().show("Publish", message.toString() , Dialog.TYPE_INFO, null, "Ok", null);
            }
            @Override
            public void errorCallback(String channel, PubnubError message) {
                new Dialog().show("Publish", message.toString() , Dialog.TYPE_INFO, null, "Ok", null);
            }
        });
    }

    @Override
    protected void onUnsubscribePresence_BtnUnsubscribePresenceOkAction(Component c, ActionEvent event) {
        String channel = findTxtUnsubscribePresenceChannel().getText();
        pubnub.unsubscribePresence(channel);
    }

    @Override
    protected void onUnsubscribe_BtnUnsubscribeOkAction(Component c, ActionEvent event) {
        String channel = findTxtUnsubscribeChannel().getText();
        pubnub.unsubscribe(channel);
    }

    @Override
    protected void onSubscribe_TxtSubscribeOkAction(Component c, ActionEvent event) {
        try {
            String channel = findTxtSubscribeChannel().getText();

            pubnub.subscribe(channel , new Callback() {

                @Override
                public void successCallback(String channel, Object message) {
                    new Dialog().show("Subscribe", message.toString() , Dialog.TYPE_INFO, null, "Ok", null);
                }
                @Override
                public void errorCallback(String channel, PubnubError message) {
                    new Dialog().show("Subscribe", message.toString() , Dialog.TYPE_INFO, null, "Ok", null);
                }
            });
        } catch (PubnubException ex) {

        }

    }

    @Override
    protected void onPresence_TxtPresenceChannelAction(Component c, ActionEvent event) {
        try {
            String channel = findTxtPresenceChannel().getText();

            pubnub.presence(channel , new Callback() {

                @Override
                public void successCallback(String channel, Object message) {
                    new Dialog().show("Presence", message.toString() , Dialog.TYPE_INFO, null, "Ok", null);
                }
                @Override
                public void errorCallback(String channel, PubnubError message) {
                    new Dialog().show("Presence", message.toString() , Dialog.TYPE_INFO, null, "Ok", null);
                }
            });
        } catch (PubnubException ex) {

        }

    }

    @Override
    protected void onHistory_BtnHistoryOkAction(Component c, ActionEvent event) {
        String channel = findTxtHistoryChannel().getText();
        pubnub.history(channel, 1, new Callback() {

            @Override
            public void successCallback(String channel, Object message) {
                new Dialog().show("History", message.toString() , Dialog.TYPE_INFO, null, "Ok", null);
            }
            @Override
            public void errorCallback(String channel, PubnubError message) {
                new Dialog().show("History", message.toString() , Dialog.TYPE_INFO, null, "Ok", null);
            }
        });

    }

    @Override
    protected void onHereNow_TxtHereNowChannelAction(Component c, ActionEvent event) {
        String channel = findTxtHereNowChannel().getText();
        pubnub.hereNow(channel, new Callback() {

            @Override
            public void successCallback(String channel, Object message) {
                new Dialog().show("Here Now", message.toString() , Dialog.TYPE_INFO, null, "Ok", null);
            }
            @Override
            public void errorCallback(String channel, PubnubError message) {
                new Dialog().show("Here Now", message.toString() , Dialog.TYPE_INFO, null, "Ok", null);
            }
        });

    }


    @Override
    protected void onGUI1_BtnTimeAction(Component c, ActionEvent event) {
        pubnub.time(new Callback() {

            @Override
            public void successCallback(String channel, Object message) {
                new Dialog().show("Time", message.toString() , Dialog.TYPE_INFO, null, "Ok", null);
            }
            @Override
            public void errorCallback(String channel, PubnubError message) {
                new Dialog().show("Time", message.toString() , Dialog.TYPE_INFO, null, "Ok", null);
            }
        });

    }

    @Override
    protected void onGUI1_BtnDarAction(Component c, ActionEvent event) {
        pubnub.disconnectAndResubscribe();;
    }
}
