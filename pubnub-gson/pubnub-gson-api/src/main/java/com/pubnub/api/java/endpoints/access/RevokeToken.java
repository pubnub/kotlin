package com.pubnub.api.java.endpoints.access;

import com.pubnub.api.java.endpoints.Endpoint;
import kotlin.Unit;

public interface RevokeToken extends Endpoint<Unit> {
    RevokeToken token(String token);
}
