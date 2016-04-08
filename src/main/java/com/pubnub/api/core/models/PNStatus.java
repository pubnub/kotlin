package com.pubnub.api.core.models;

import com.pubnub.api.core.enums.PNStatusCategory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PNStatus {

    PNStatusCategory category;
    boolean error;
    boolean automaticallyRetry;

    public void retry(){
        // TODO
    }

    public void cancelAutomaticRetry() {
        // TODO
    }


}
