package com.pubnub.api.models.consumer.pubsub.message_actions;

import com.pubnub.api.models.consumer.message_actions.PNMessageAction;
import com.pubnub.api.models.consumer.pubsub.BasePubSubResult;
import com.pubnub.api.models.consumer.pubsub.objects.ObjectResult;
import lombok.Builder;
import lombok.ToString;

@ToString(callSuper = true)
public class PNMessageActionResult extends ObjectResult<PNMessageAction> {

    @Builder(builderMethodName = "actionBuilder")
    private PNMessageActionResult(BasePubSubResult result, String event, PNMessageAction data) {
        super(result, event, data);
    }

    public PNMessageAction getMessageAction() {
        return data;
    }
}