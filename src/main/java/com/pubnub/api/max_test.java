package com.pubnub.api;

import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.core.PnConfiguration;
import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.models.consumer_facing.PNErrorStatus;
import com.pubnub.api.core.models.consumer_facing.PNStatus;
import com.pubnub.api.core.models.consumer_facing.PNMessageResult;
import com.pubnub.api.core.models.consumer_facing.PNPresenceEventResult;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;


@Slf4j
public class max_test {

    public static void main(String[] args) throws InterruptedException {
        PnConfiguration pnConfiguration = new PnConfiguration();
        pnConfiguration.setSubscribeKey("sub-c-82ab2196-b64f-11e5-8622-0619f8945a4f");
        pnConfiguration.setPublishKey("pub-c-8beb3658-0dfd-4032-8f4b-9c6b9ca4d803");


        Pubnub pubnub = new Pubnub(pnConfiguration);
        pubnub.addListener(new SubscribeCallback() {
            @Override
            public void status(Pubnub pubnub, PNStatus status) {
                int moose = 10;
            }

            @Override
            public void message(Pubnub pubnub, PNMessageResult message) {
                log.debug("result ->>", message.toString());
            }

            @Override
            public void presence(Pubnub pubnub, PNPresenceEventResult presence) {
                log.debug("presence ->>", presence.toString());
            }
        });

        Map<String, Object> state = new HashMap<>();
        state.put("max", "moose");

        pubnub.subscribe().channel("max-ch1").withPresence().execute();
        pubnub.setPresenceState().channel("max-ch1").state(state).build().async(new PNCallback<Boolean>() {
            @Override
            public void onResponse(Boolean result, PNErrorStatus status) {
                int moose = 10;
            }
        });

        Thread.sleep(5000);

        pubnub.unsubscribe().channel("max-ch1").execute();

    }

}
