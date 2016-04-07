package com.pubnub.api.core;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PnConfiguration {

    private String origin;
    private int subscribeTimeout;
    private int presenceTimeout;
    private int heartbeatInterval;
    private boolean secure;
    private String subscribeKey;
    private String publishKey;
    private String secretKey;
    private String cipherKey;
    private String authKey;
    private String UUID;
    private boolean cacheBusting;

}
