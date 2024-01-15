package com.pubnub.api.managers;

public class PublishSequenceManager {


    private int maxSequence;
    private int nextSequence;

    public PublishSequenceManager(int providedMaxSequence) {
        this.maxSequence = providedMaxSequence;
    }

    public synchronized int getNextSequence() {
        if (maxSequence == nextSequence) {
            nextSequence = 1;
        } else {
            nextSequence += 1;
        }

        return nextSequence;
    }

}
