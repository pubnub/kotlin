package com.pubnub.api;

import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.core.PnConfiguration;
import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.models.PNPresenceEventResult;
import com.pubnub.api.core.models.PNStatus;
import com.pubnub.api.core.models.PNSubscriberData;


public class max_test {

    public static void main(String[] args) {
        PnConfiguration pnConfiguration = new PnConfiguration();
        pnConfiguration.setSubscribeKey("demo-36");
        pnConfiguration.setPublishKey("demo-36");

        Pubnub pubnub = new Pubnub(pnConfiguration);
        pubnub.addListener(new SubscribeCallback() {
            @Override
            public void status(PNStatus status) {

            }

            @Override
            public void message(PNSubscriberData message) {

            }

            @Override
            public void presence(PNPresenceEventResult presence) {

            }
        });

        pubnub.subscribe().channel("max-ch1").execute();

    }

}
