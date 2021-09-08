package com.pubnub.api.integration.pam;

import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.enums.PNLogVerbosity;
import com.pubnub.api.integration.util.BaseIntegrationTest;
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant;
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGroupGrant;
import com.pubnub.api.models.consumer.access_manager.v3.PNGrantTokenResult;
import com.pubnub.api.models.consumer.access_manager.v3.PNToken;
import org.junit.Test;

import java.util.Arrays;
import static org.junit.Assert.assertEquals;


public class GrantTokenIT extends BaseIntegrationTest {
    private final PubNub pubNubUnderTest = getServer();

    @Test
    public void happyPath() throws PubNubException {
        //given
        pubNubUnderTest.getConfiguration().setLogVerbosity(PNLogVerbosity.BODY);
        final int expectedTTL = 1337;
        final String expectedChannelResourceName = "channelResource";
        final String expectedChannelPattern = "channel.*";
        final String expectedChannelGroupResourceId = "channelGroup";
        final String expectedChannelGroupPattern = "channelGroup.*";

        //when
        final PNGrantTokenResult grantTokenResponse = pubNubUnderTest
                .grantToken()
                .ttl(expectedTTL)
                .channels(Arrays.asList(ChannelGrant.name(expectedChannelResourceName).delete(),
                        ChannelGrant.pattern(expectedChannelPattern).write()))
                .channelGroups(Arrays.asList(ChannelGroupGrant.id(expectedChannelGroupResourceId).read(),
                        ChannelGroupGrant.pattern(expectedChannelGroupPattern).manage()))
                .sync();

        final PNToken pnToken = pubNubUnderTest.parseToken(grantTokenResponse.getToken());

        //then
        assertEquals(expectedTTL, pnToken.getTtl());
        assertEquals(new PNToken.PNResourcePermissions(false, false, false, false, false, false, false),
                pnToken.getResources().getChannels().get(expectedChannelResourceName));
        assertEquals(new PNToken.PNResourcePermissions(false, true, false, false, false, false, false),
                pnToken.getResources().getChannelGroups().get(expectedChannelGroupResourceId));
        assertEquals(new PNToken.PNResourcePermissions(false, false, true, false, false, false, false),
                pnToken.getPatterns().getChannels().get(expectedChannelPattern));
        assertEquals(new PNToken.PNResourcePermissions(false, false, false, true, false, false, false),
                pnToken.getPatterns().getChannelGroups().get(expectedChannelGroupPattern));
    }

}
