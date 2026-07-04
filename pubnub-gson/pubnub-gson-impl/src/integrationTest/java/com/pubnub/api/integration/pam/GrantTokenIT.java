package com.pubnub.api.integration.pam;

import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.integration.util.BaseIntegrationTest;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.SpaceId;
import com.pubnub.api.java.models.consumer.access_manager.sum.SpacePermissions;
import com.pubnub.api.java.models.consumer.access_manager.sum.UserPermissions;
import com.pubnub.api.java.models.consumer.access_manager.v3.ChannelGrant;
import com.pubnub.api.java.models.consumer.access_manager.v3.ChannelGroupGrant;
import com.pubnub.api.java.models.consumer.access_manager.v3.DataSyncGrant;
import com.pubnub.api.models.consumer.access_manager.v3.PNGrantTokenResult;
import com.pubnub.api.models.consumer.access_manager.v3.PNToken;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class GrantTokenIT extends BaseIntegrationTest {

    @Test
    public void happyPath_SUM() throws PubNubException {
        PubNub pubNubUnderTest = getServer();
        final int expectedTTL = 1337;
        String expectedSpaceIdValue = "space01";
        String expectedUser01Value = "user01";
        String expectedSpaceIdPattern = "space.*";
        String expectedUserIdPattern = "user.*";
        String expectedAuthorizedUser = "authorizedUser";
        PNGrantTokenResult grantTokenResult = pubNubUnderTest
                .grantToken(expectedTTL)
                .spacesPermissions(Arrays.asList(SpacePermissions.id(new SpaceId(expectedSpaceIdValue)).delete(), SpacePermissions.pattern(expectedSpaceIdPattern).read()))
                .usersPermissions(Arrays.asList(UserPermissions.id(new UserId(expectedUser01Value)).get(), UserPermissions.pattern(expectedUserIdPattern).get()))
                .authorizedUserId(new UserId(expectedAuthorizedUser))
                .sync();
        PNToken pnToken = pubNubUnderTest.parseToken(grantTokenResult.getToken());

        assertEquals(expectedTTL, pnToken.getTtl());
        assertEquals(new PNToken.PNResourcePermissions(false, false, false, true, false, false, false),
                pnToken.getResources().getChannels().get(expectedSpaceIdValue));
        assertEquals(new PNToken.PNResourcePermissions(true, false, false, false, false, false, false),
                pnToken.getPatterns().getChannels().get(expectedSpaceIdPattern));
        assertEquals(new PNToken.PNResourcePermissions(false, false, false, false, true, false, false),
                pnToken.getResources().getUuids().get(expectedUser01Value));
        assertEquals(new PNToken.PNResourcePermissions(false, false, false, false, true, false, false),
                pnToken.getPatterns().getUuids().get(expectedUserIdPattern));

    }

    @SuppressWarnings("deprecation")
    @Test
    public void happyPath() throws PubNubException {
        //given
        PubNub pubNubUnderTest = getServer();
        final int expectedTTL = 1337;
        final String expectedChannelResourceName = "channelResource";
        final String expectedChannelPattern = "channel.*";
        final String expectedChannelGroupResourceId = "channelGroup";
        final String expectedChannelGroupPattern = "channelGroup.*";

        //when
        final PNGrantTokenResult grantTokenResponse = pubNubUnderTest
                .grantToken(expectedTTL)
                .channels(Arrays.asList(ChannelGrant.name(expectedChannelResourceName).delete(),
                        ChannelGrant.pattern(expectedChannelPattern).write()))
                .channelGroups(Arrays.asList(ChannelGroupGrant.id(expectedChannelGroupResourceId).read(),
                        ChannelGroupGrant.pattern(expectedChannelGroupPattern).manage()))
                .sync();

        final PNToken pnToken = pubNubUnderTest.parseToken(grantTokenResponse.getToken());

        //then
        assertEquals(expectedTTL, pnToken.getTtl());
        assertEquals(new PNToken.PNResourcePermissions(false, false, false, true, false, false, false),
                pnToken.getResources().getChannels().get(expectedChannelResourceName));
        assertEquals(new PNToken.PNResourcePermissions(true, false, false, false, false, false, false),
                pnToken.getResources().getChannelGroups().get(expectedChannelGroupResourceId));
        assertEquals(new PNToken.PNResourcePermissions(false, true, false, false, false, false, false),
                pnToken.getPatterns().getChannels().get(expectedChannelPattern));
        assertEquals(new PNToken.PNResourcePermissions(false, false, true, false, false, false, false),
                pnToken.getPatterns().getChannelGroups().get(expectedChannelGroupPattern));
    }

    @Test
    public void happyPath_datasync() throws PubNubException {
        //given
        PubNub pubNubUnderTest = getServer();
        final int expectedTTL = 1337;
        final String entityName = "capy-001";
        final String membershipName = "user-123:channel-X";

        //when — full fluent Java-facing DataSync grant chain
        final PNGrantTokenResult grantTokenResponse = pubNubUnderTest
                .grantToken(expectedTTL)
                .authorizedUUID("pam-debug-admin")
                .datasync(Arrays.asList(
                        DataSyncGrant.entity(entityName).get().update(),
                        DataSyncGrant.entityPattern(".*").get(),
                        DataSyncGrant.relationship("rel-1").get(),
                        DataSyncGrant.relationshipPattern(".*").create(),
                        DataSyncGrant.membershipPattern(".*").create(),
                        DataSyncGrant.membership(membershipName).get())
                )
                .sync();

        final PNToken pnToken = pubNubUnderTest.parseToken(grantTokenResponse.getToken());

        //then — the Lombok-generated getters surface the new datasync maps and the create flag
        assertEquals(expectedTTL, pnToken.getTtl());
        // PNResourcePermissions(read, write, manage, delete, get, update, join, create)
        assertEquals(new PNToken.PNResourcePermissions(false, false, false, false, true, true, false, false),
                pnToken.getResources().getDatasyncEntities().get(entityName));
        assertEquals(new PNToken.PNResourcePermissions(false, false, false, false, true, false, false, false),
                pnToken.getResources().getDatasyncMemberships().get(membershipName));
        assertEquals(new PNToken.PNResourcePermissions(false, false, false, false, true, false, false, false),
                pnToken.getPatterns().getDatasyncEntities().get(".*"));

        // create must survive the round-trip (regression guard for the historically-dropped bit 16)
        final PNToken.PNResourcePermissions relPerms = pnToken.getResources().getDatasyncRelationships().get("rel-1");
        final PNToken.PNResourcePermissions relGeneralPerms = pnToken.getPatterns().getDatasyncRelationships().get(".*");
        assertTrue(relPerms.getGet());
        assertTrue(relGeneralPerms.getCreate());
    }

}
