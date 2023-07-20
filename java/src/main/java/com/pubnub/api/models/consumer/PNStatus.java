package com.pubnub.api.models.consumer;

import com.pubnub.api.endpoints.remoteaction.RemoteAction;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.core.Status;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

@Builder(toBuilder = true)
@Getter
@ToString
public class PNStatus implements Status {

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
    @Nullable
    private List<String> affectedChannels;
    @Nullable
    private List<String> affectedChannelGroups;

    @Getter(AccessLevel.NONE)
    @ToString.Exclude
    private RemoteAction<?> executedEndpoint;


    public void retry() {
        executedEndpoint.retry();
    }

    @Nullable
    @Override
    public Exception getException() {
        if (errorData != null) {
            return errorData.getThrowable();
        }
        return null;
    }

    /*
    public void cancelAutomaticRetry() {
        // TODO
    }
    */

}
