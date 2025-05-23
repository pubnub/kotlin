package com.pubnub.api.integration.pam;

import com.pubnub.api.PubNubException;
import com.pubnub.api.integration.util.BaseIntegrationTest;
import com.pubnub.api.java.models.consumer.access_manager.PNAccessManagerGrantResult;
import com.pubnub.api.java.models.consumer.access_manager.PNAccessManagerKeyData;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class GrantIT extends BaseIntegrationTest {

    @Test
    public void grantWithoutAuthKeyDoesntCrash() throws PubNubException {
        getServer().grant().channels(Arrays.asList("abc")).write(true).read(false).ttl(0).sync();
    }

    @Test
    public void grantAllForUUID() throws PubNubException {
        String uuid = "uuid123";
        String authKey = "authKey123";
        int ttl = 120;
        PNAccessManagerGrantResult expectedResult = PNAccessManagerGrantResult.builder()
                .channelGroups(Collections.emptyMap())
                .channels(Collections.emptyMap())
                .level("uuid")
                .ttl(ttl)
                .subscribeKey(getServer().getConfiguration().getSubscribeKey())
                .uuids(Collections.singletonMap(uuid, Collections.singletonMap(authKey, PNAccessManagerKeyData.builder()
                        .getEnabled(true)
                        .deleteEnabled(true)
                        .updateEnabled(true)
                        .readEnabled(false)
                        .manageEnabled(false)
                        .joinEnabled(false)
                        .writeEnabled(false)
                        .build())))
                .build();

        PNAccessManagerGrantResult result2 = getServer()
                .grant()
                .uuids(Collections.singletonList(uuid))
                .authKeys(Collections.singletonList(authKey))
                .ttl(ttl)
                .get(true)
                .update(true)
                .delete(true)
                .sync();
        assertEquals(expectedResult, result2);
    }
}
