package com.pubnub.api.managers.token_manager;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class TokenManagerProperties {

    private PNResourceType pnResourceType;
    private String resourceId;
}
