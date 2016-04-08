package com.pubnub.api.core.models;

import com.pubnub.api.core.enums.PNOperationType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PNResult {

    private int statusCode;
    private PNOperationType operation;
    private boolean TLSEnabled;
    private String uuid;
    private String authKey;
    private String origin;
    private Object clientRequest;

}
