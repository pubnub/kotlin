package com.pubnub.api.core.models.server.presence;

import lombok.Data;

import java.util.List;

@Data
public class WhereNowPayload {
    private List<String> channels;
}
