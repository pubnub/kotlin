package com.pubnub.api.models.consumer.objects_api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PNPatchPayload<T extends PropertyEnvelope> {

    private List<T> add;
    private List<T> update;
    private List<T> remove;

    public PNPatchPayload() {
        this.add = new ArrayList<>();
        this.update = new ArrayList<>();
        this.remove = new ArrayList<>();
    }

    public void setAdd(T[] add) {
        this.add.clear();
        this.add.addAll(Arrays.asList(add));
    }

    public void setUpdate(T[] update) {
        this.update.clear();
        this.update.addAll(Arrays.asList(update));
    }

    public void setRemove(T[] remove) {
        this.remove.clear();
        for (T t : remove) {
            t.custom(null);
            this.remove.add(t);
        }
    }
}
