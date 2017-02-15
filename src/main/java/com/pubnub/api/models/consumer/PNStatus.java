package com.pubnub.api.models.consumer;

import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.enums.PNStatusCategory;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@ToString
public class PNStatus {

    private PNStatusCategory category;
    private PNErrorData errorData;
    private boolean error;

    // boolean automaticallyRetry;

    private int statusCode;
    private PNOperationType operation;

    private boolean tlsEnabled;

    private String uuid;
    private String authKey;
    private String origin;
    private Object clientRequest;

    // send back channel, channel groups that were affected by this operation
    private List<String> affectedChannels;
    private List<String> affectedChannelGroups;

    @Getter(AccessLevel.NONE)
    private Endpoint executedEndpoint;


    public void retry() {
        executedEndpoint.retry();
    }

    /*
    public void cancelAutomaticRetry() {
        // TODO
    }
    */

}
