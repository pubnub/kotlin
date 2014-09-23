package com.pubnub.api;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class ChannelGroup extends SubscriptionItem{
    ArrayList<String> channels = new ArrayList<String>();

    public static class RetrieveChannelsCallback extends Callback {
        private CountDownLatch latch;
        private JSONArray channels;

        RetrieveChannelsCallback(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void successCallback(String channel, Object channels) {
            this.channels = (JSONArray) channels;
            this.latch.countDown();
        }

        @Override
        public void errorCallback(String channel, PubnubError error) {
            this.channels = new JSONArray();
            this.latch.countDown();
        }

        public JSONArray getChannels() {
            return channels;
        }
    }

    ChannelGroup(String name, Callback callback, PubnubCore pubnub) {
        CountDownLatch latch = new CountDownLatch(1);

        this.name = name;
        this.callback = callback;
        this.connected = false;

        RetrieveChannelsCallback cb = new RetrieveChannelsCallback(latch);
        pubnub.channelGroup(name, cb);

        try {
            latch.await();
        } catch (InterruptedException e) {
            if (callback != null) {
                callback.errorCallback(
                        name,
                        PubnubError.getErrorObject(
                                PubnubError.PNERROBJ_INTERNAL_ERROR,
                                "Failed to retrieve " + name + "'s channel list"
                        )
                );
            }
        }

        JSONArray channelNames = cb.getChannels();

        for (int i = 0; i < channelNames.length(); i++) {
            try {
                channels.add(channelNames.get(i).toString());
            } catch (JSONException e) {
                if (callback != null) {
                    callback.errorCallback(
                            name,
                            PubnubError.getErrorObject(
                                    PubnubError.PNERROBJ_INTERNAL_ERROR,
                                    "Failed to retrieve " + name + "'s channel list"
                            )
                    );
                }
            }
        }
    }

    public ArrayList<String> getChannels() {
        return channels;
    }

}
