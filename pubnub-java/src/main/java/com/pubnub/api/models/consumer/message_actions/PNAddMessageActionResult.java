package com.pubnub.api.models.consumer.message_actions;

import lombok.Builder;

public class PNAddMessageActionResult extends PNMessageAction {

    @Builder
    private PNAddMessageActionResult(PNMessageAction pnMessageAction) {
        super(pnMessageAction);
    }
}
