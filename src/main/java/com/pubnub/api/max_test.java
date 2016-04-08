package com.pubnub.api;

import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.core.PnConfiguration;
import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.models.PNMessageResult;
import com.pubnub.api.core.models.PNPresenceEventResult;
import com.pubnub.api.core.models.PNStatus;


public class max_test {

    public static void main(String[] args) {
        PnConfiguration pnConfiguration = new PnConfiguration();
        pnConfiguration.setSubscribeKey("demo-36");
        pnConfiguration.setPublishKey("demo-36");

        Pubnub pubnub = new Pubnub(pnConfiguration);
        pubnub.addListener(new SubscribeCallback() {
            @Override
            public void status(Pubnub pubnub, PNStatus status) {

            }

            @Override
            public void message(Pubnub pubnub, PNMessageResult message) {
                int moose = 10;
            }

            @Override
            public void presence(Pubnub pubnub, PNPresenceEventResult presence) {

            }
        });

        pubnub.subscribe().channel("max-ch1").withPresence().execute();

    }

}
