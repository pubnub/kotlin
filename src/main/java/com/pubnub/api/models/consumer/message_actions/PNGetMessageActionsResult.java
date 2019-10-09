package com.pubnub.api.models.consumer.message_actions;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class PNGetMessageActionsResult {

    private List<PNMessageAction> actions;
}
