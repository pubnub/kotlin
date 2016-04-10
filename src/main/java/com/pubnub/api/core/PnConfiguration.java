package com.pubnub.api.core;


import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PnConfiguration {

    /**
     * By default, the origin is pointing directly to PubNub servers. If a proxy origin is neeeded, set a custom
     * origin using this parameter.
     */
    private String origin;
    private int subscribeTimeout;
    /**
     * In seconds, how long the server will consider this client to be onlien before issuing a leave event
     */
    private int presenceTimeout;
    /**
     * In seconds, How often the client should announce it's existance via heartbeating.
     */
    private int heartbeatInterval;
    /**
     * set to true to switch the client to HTTPS:// based communications
     */
    private boolean secure;
    /**
     * Subscribe Key provided by PubNub
     */
    private String subscribeKey;
    /**
     * Publish Key provided by PubNub
     */
    private String publishKey;
    private String secretKey;
    private String cipherKey;
    private String authKey;
    private String uuid;
    /**
     * If proxies are forcefully caching requests, set to true to allow the client to randomize the subdomain.
     * This configuration is not supported if custom origin is enabled.
     */
    private boolean cacheBusting;

    /**
     * Stores the maximum number of seconds which the client should wait for connection before timing out.
     */
    private int connectTimeout;

    /**
     * Stores reference on maximum number of seconds which client should wait for events from live feed
     */
    private int subscribeMaximumIdleTime;

    /**
     *  Reference on number of seconds which is used by client during non-subscription operations to
     check whether response potentially failed with 'timeout' or not.
     */
    private int nonSubscribeRequestTimeout;

    public PnConfiguration() {
        // TODO: work w/ dynamic configurations for intervals
        presenceTimeout = 300;
        heartbeatInterval = 148;
        uuid = UUID.randomUUID().toString();

        nonSubscribeRequestTimeout = 10;
        subscribeMaximumIdleTime = 310;
        connectTimeout = 5;

    }


}
