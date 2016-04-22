package com.pubnub.api.core.models.consumer_facing;

import com.pubnub.api.core.enums.PNOperationType;
import com.pubnub.api.core.enums.PNStatusCategory;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PNStatus {

    PNStatusCategory category;
    PNErrorData errorData;
    boolean error;
    boolean automaticallyRetry;

    private int statusCode;
    private PNOperationType operation;
    private boolean TLSEnabled;
    private String uuid;
    private String authKey;
    private String origin;
    private Object clientRequest;


    /*
    public void retry(){
        // TODO
    }

    public void cancelAutomaticRetry() {
        // TODO
    }
    */

}
