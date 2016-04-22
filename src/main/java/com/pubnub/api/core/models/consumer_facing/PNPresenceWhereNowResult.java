package com.pubnub.api.core.models.consumer_facing;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PNPresenceWhereNowResult {
    private List<String> channels;
}
