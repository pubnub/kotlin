package com.pubnub.api.models.consumer;

import lombok.Data;

@Data
public class PNBoundedPage {
    private final Long start;
    private final Long end;
    private final Integer limit;
}
