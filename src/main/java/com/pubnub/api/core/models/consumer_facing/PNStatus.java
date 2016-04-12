package com.pubnub.api.core.models.consumer_facing;

import com.pubnub.api.core.enums.PNStatusCategory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PNStatus extends PNResult {

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
