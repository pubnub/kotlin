package com.pubnub.api.core;


import com.pubnub.api.core.enums.PNHeartbeatNotificationOptions;
import lombok.AccessLevel;
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
     * In seconds, how long the server will consider this client to be onlien before issuing a leave event.
     */
    @Setter(AccessLevel.NONE)
    private int presenceTimeout;
    /**
     * In seconds, How often the client should announce it's existence via heartbeating.
     */
    @Setter(AccessLevel.NONE)
    private int heartbeatInterval;

    /**
     * set to true to switch the client to HTTPS:// based communications.
     */
    private boolean secure;
    /**
     * Subscribe Key provided by PubNub
     */
    private String subscribeKey;
    /**
     * Publish Key provided by PubNub.
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
     * toggle to enable verbose logging.
     */

    private boolean verboseLogging;

    /**
     * Stores the maximum number of seconds which the client should wait for connection before timing out.
     */
    private int connectTimeout;

    /**
     *  Reference on number of seconds which is used by client during non-subscription operations to
     check whether response potentially failed with 'timeout' or not.
     */
    private int nonSubscribeRequestTimeout;

    /**
     * verbosity of heartbeat configuration, by default only alerts on failed heartbeats
     */
    private PNHeartbeatNotificationOptions heartbeatNotificationOptions;

    /**
     * Initialize the PnConfiguration with default values
     */
    public PnConfiguration() {
        setPresenceTimingConfiguration(300);

        uuid = UUID.randomUUID().toString();

        nonSubscribeRequestTimeout = 10;
        subscribeTimeout = 310;
        connectTimeout = 5;

        heartbeatNotificationOptions = PNHeartbeatNotificationOptions.Failures;
    }

    /**
     *  set presence configurations for timeout and announce interval.
     * @param timeout presence timeout; how long before the server considers this client to be gone.
     * @param interval presence announce interval, how often the client should announce itself.
     * @return returns itself.
     */
    public PnConfiguration setPresenceTimingConfiguration(final int timeout, final int interval) {
        this.presenceTimeout = timeout;
        this.heartbeatInterval = interval;

        return this;
    }

    /**
     * set presence configurations for timeout and allow the client to pick the best interval
     * @param timeout presence timeout; how long before the server considers this client to be gone.
     * @return returns itself.
     */
    public PnConfiguration setPresenceTimingConfiguration(final int timeout) {
        return setPresenceTimingConfiguration(timeout,(timeout / 2) - 1);
    }

}
