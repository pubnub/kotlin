package com.pubnub.api.events;

/**
 * Created by Frederick on 3/29/16.
 */
public interface PnObjectEventListener {

    void receivedStatus(PnResultStatus status);

    void receivedMessage(PnResultStatus status);

    void receivedPresence(PnResultStatus status);

}
