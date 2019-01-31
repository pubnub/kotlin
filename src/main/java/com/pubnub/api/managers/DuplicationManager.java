package com.pubnub.api.managers;

import com.pubnub.api.PNConfiguration;
import com.pubnub.api.models.server.SubscribeMessage;

import java.util.ArrayList;

public class DuplicationManager {

    private ArrayList<String> hashHistory;
    private PNConfiguration pnConfiguration;

    public DuplicationManager(PNConfiguration pnc) {
        this.hashHistory = new ArrayList<>();
        this.pnConfiguration = pnc;
    }

    private String getKey(SubscribeMessage message) {
        return message.getPublishMetaData().getPublishTimetoken().toString().concat("-").concat(Integer.toString(message.getPayload().hashCode()));
    }

    public boolean isDuplicate(SubscribeMessage message) {
        return hashHistory.contains(this.getKey(message));
    }

    public void addEntry(SubscribeMessage message) {
        if (this.hashHistory.size() >= pnConfiguration.getMaximumMessagesCacheSize()) {
            hashHistory.remove(0);
        }

        hashHistory.add(this.getKey(message));
    }

    public void clearHistory() {
        this.hashHistory.clear();
    }

}
