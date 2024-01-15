package com.pubnub.api.models.consumer.message_actions;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@ToString
@Accessors(chain = true)
public class PNMessageAction {

    @Setter
    private String type;
    @Setter
    private String value;
    @Setter
    private Long messageTimetoken;

    private String uuid;
    private Long actionTimetoken;

    public PNMessageAction() {

    }

    PNMessageAction(PNMessageAction pnMessageAction) {
        this.type = pnMessageAction.type;
        this.value = pnMessageAction.value;
        this.uuid = pnMessageAction.uuid;
        this.actionTimetoken = pnMessageAction.actionTimetoken;
        this.messageTimetoken = pnMessageAction.messageTimetoken;
    }
}
