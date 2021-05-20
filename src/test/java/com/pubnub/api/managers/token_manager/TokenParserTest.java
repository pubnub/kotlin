package com.pubnub.api.managers.token_manager;

import com.pubnub.api.PubNubException;
import com.pubnub.api.models.consumer.access_manager.v3.PNToken;
import org.junit.Test;

import java.util.Collections;

import static java.util.Collections.emptyMap;
import static org.junit.Assert.assertEquals;

public class TokenParserTest {

    private final TokenParser tokenParser = new TokenParser();

    @Test
    public void stringTokenIsProperlyDeserialized() throws PubNubException {
        //given
        String stringToken = "p0F2AkF0Gl043rhDdHRsCkNyZXOkRGNoYW6hZnNlY3JldAFDZ3JwoEN1c3KgQ3NwY6BDcGF0pERjaGFuoENncnCgQ3VzcqBDc3BjoERtZXRhoENzaWdYIGOAeTyWGJI-blahPGD9TuKlaW1YQgiB4uR_edmfq-61";
        PNToken expectedToken = new PNToken(2, 1564008120, 10,
                new PNToken.PNTokenResources(
                        Collections.singletonMap("secret",
                                new PNToken.PNResourcePermissions(false, true, false, false, false)),
                        emptyMap()),
                new PNToken.PNTokenResources(emptyMap(), emptyMap()));

        //when
        PNToken token = tokenParser.unwrapToken(stringToken);

        //then
        assertEquals(expectedToken, token);
    }
}
