package com.pubnub.api;

import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.core.PnConfiguration;
import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.PubnubException;
import com.pubnub.api.core.models.consumer_facing.PNMessageResult;
import com.pubnub.api.core.models.consumer_facing.PNPresenceEventResult;
import com.pubnub.api.core.models.consumer_facing.PNStatus;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


@Slf4j
public class max_test {

    public static void main(String[] args) throws InterruptedException, PubnubException {
        PnConfiguration pnConfiguration = new PnConfiguration();
        pnConfiguration.setSubscribeKey("sub-c-4cec9f8e-01fa-11e6-8180-0619f8945a4f");
        pnConfiguration.setPublishKey("pub-c-d34a0058-6747-4c9d-b669-e651d7e0efb7");
        pnConfiguration.setSecretKey("sec-c-NDJkOWM2ZWItNzBhMS00YzllLWFlZjAtNGJlMjVkZjZlNzMy");
        // pnConfiguration.setCipherKey("testCipher");


        Pubnub pubnub = new Pubnub(pnConfiguration);

        pubnub.publish()
                .channel("coolChannel").usePOST(false).shouldStore(true)
                .message(Arrays.asList("m1", "m2")).sync();

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

        PNStatus pnErrorStatus = PNStatus.builder().build();;


        Map<String, Object> state = new HashMap<>();
        state.put("max", "moose");

        pubnub.subscribe().channels(Arrays.asList("coolChannel")).withPresence().execute();
        //pubnub.setPresenceState().channel("max-ch1").state(state).build().async(new PNCallback<PNSetStateResult>() {
        //    @Override
        //    public void onResponse(PNSetStateResult result, PNErrorStatus status) {
        //        int moose = 10;
        //    }
        //});

        /*
        try {
            Object moose = pubnub.grant().authKey("max")
                    .channel("ch1-max").channel("ch2-max")
                    .channelGroup("cg1-max").channelGroup("cg2-max")
                    .ttl(0).write(true)
                    .build().sync();
            int max = 11;
        } catch (PubnubException e) {
            e.printStackTrace();
        }
        */


        // PNHistoryResult moose = pubnub.history().channel("coolChannelENC2").build().sync();


        int max = 10;

        /*
        Object moose = pubnub.audit()
                .authKey("max").authKey("max2")
                .channel("ch1")
                .build().sync();
        */

        //Thread.sleep(5000);

        //pubnub.unsubscribe().channel("max-ch1").execute();

    }

}
