package com.pubnub.api.v2.callbacks

/**
 * Interface implemented by objects that manage the subscription connection to the PubNub network and can be monitored
 * for connection state changes.
 */
interface StatusEmitter : BaseStatusEmitter<StatusListener>
