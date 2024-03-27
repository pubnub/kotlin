package com.pubnub.api.endpoints.access;

import com.pubnub.api.endpoints.Endpoint;
import kotlin.Unit;

public interface RevokeToken extends Endpoint<Unit> {
    RevokeToken token(String token);
}
