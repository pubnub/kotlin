package com.pubnub.api.integration.pam;

import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.integration.util.BaseIntegrationTest;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.models.consumer.access_manager.v3.ChannelGrant;
import com.pubnub.api.java.models.consumer.access_manager.v3.ChannelGroupGrant;
import com.pubnub.api.java.models.consumer.access_manager.v3.DataSyncGrant;
import com.pubnub.api.java.models.consumer.access_manager.v3.TokenGrant;
import com.pubnub.api.java.models.consumer.access_manager.v3.UUIDGrant;
import com.pubnub.api.models.consumer.access_manager.v3.PNDataSyncProjectionScope;
import com.pubnub.api.models.consumer.access_manager.v3.PNDataSyncProjections;
import com.pubnub.api.models.consumer.access_manager.v3.PNGrantTokenResult;
import com.pubnub.api.models.consumer.access_manager.v3.PNToken;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class GrantTokenIT extends BaseIntegrationTest {

    @Test
    public void happyPath_channelsAndUuids() throws PubNubException {
        PubNub pubNubUnderTest = getServer();
        final int expectedTTL = 1337;
        String expectedChannelName = "channel01";
        String expectedUuidValue = "uuid01";
        String expectedChannelPattern = "channel.*";
        String expectedUuidPattern = "uuid.*";
        String expectedAuthorizedUuid = "authorizedUuid";
        PNGrantTokenResult grantTokenResult = pubNubUnderTest
                .grantToken(expectedTTL)
                .channels(Arrays.asList(ChannelGrant.name(expectedChannelName).delete(), ChannelGrant.pattern(expectedChannelPattern).read()))
                .uuids(Arrays.asList(UUIDGrant.id(expectedUuidValue).get(), UUIDGrant.pattern(expectedUuidPattern).get()))
                .authorizedUUID(expectedAuthorizedUuid)
                .sync();
        PNToken pnToken = pubNubUnderTest.parseToken(grantTokenResult.getToken());

        assertEquals(expectedTTL, pnToken.getTtl());
        assertEquals(new PNToken.PNResourcePermissions(false, false, false, true, false, false, false),
                pnToken.getResources().getChannels().get(expectedChannelName));
        assertEquals(new PNToken.PNResourcePermissions(true, false, false, false, false, false, false),
                pnToken.getPatterns().getChannels().get(expectedChannelPattern));
        assertEquals(new PNToken.PNResourcePermissions(false, false, false, false, true, false, false),
                pnToken.getResources().getUuids().get(expectedUuidValue));
        assertEquals(new PNToken.PNResourcePermissions(false, false, false, false, true, false, false),
                pnToken.getPatterns().getUuids().get(expectedUuidPattern));

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
    public void can_grantToken_for_datasync_resources_and_patterns() throws PubNubException {
        //given
        PubNub pubNubUnderTest = getServer();
        final int expectedTTL = 1337;
        final String entityName = "capy-001";
        final String membershipName = "user-123:channel-X";

        //when — full fluent Java-facing DataSync grant chain
        final PNGrantTokenResult grantTokenResponse = pubNubUnderTest
                .grantToken(expectedTTL)
                .authorizedUserId(new UserId("pam-debug-admin"))
                .grants(Arrays.<TokenGrant>asList(
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

    @Test
    public void grantToken_withAllGrantTypes_viaFlatList() throws PubNubException {
        // given — mint a single token through the new flat `.grants(...)` overload carrying EVERY grant type that
        // implements TokenGrant: ChannelGrant, ChannelGroupGrant and DataSyncGrant. Each grant type is
        // exercised in both exact and pattern form, and DataSync covers every namespace (channels, users, entities,
        // relationships, memberships).
        PubNub pubNubUnderTest = getServer();
        final int expectedTTL = 1337;
        final String channelId = "channelResource";
        final String channelPattern = "channel.*";
        final String channelGroupId = "channelGroup";
        final String channelGroupPattern = "channelGroup.*";
        final String dataSyncChannelId = "dsChannel01";
        final String dataSyncChannelPattern = "dsChannel.*";
        final String userId = "user01";
        final String userPattern = "user.*";
        final String entityId = "capy-001";
        final String entityPattern = "capy.*";
        final String relationshipId = "user.A:channel.X";
        final String relationshipPattern = "rel.*";
        final String membershipId = "user-123:channel-X";
        final String membershipPattern = "mem.*";

        // when
        final PNGrantTokenResult grantTokenResponse = pubNubUnderTest
                .grantToken(expectedTTL)
                .authorizedUserId(new UserId("pam-debug-admin"))
                .grants(Arrays.<TokenGrant>asList(
                        ChannelGrant.name(channelId).read().write(),
                        ChannelGrant.pattern(channelPattern).read(),
                        ChannelGroupGrant.id(channelGroupId).read().manage(),
                        ChannelGroupGrant.pattern(channelGroupPattern).read(),
                        DataSyncGrant.channel(dataSyncChannelId).get().update(),
                        DataSyncGrant.channelPattern(dataSyncChannelPattern).get().create(),
                        DataSyncGrant.user(userId).get().update(),
                        DataSyncGrant.userPattern(userPattern).get().create(),
                        DataSyncGrant.subscribe(userId, "adminProjection"),
                        DataSyncGrant.entity(entityId).get().update(),
                        DataSyncGrant.entityPattern(entityPattern).get(),
                        DataSyncGrant.relationship(relationshipId).get(),
                        DataSyncGrant.relationshipPattern(relationshipPattern).get(),
                        DataSyncGrant.membership(membershipId).get(),
                        DataSyncGrant.membershipPattern(membershipPattern).get()))
                .sync();

        // then — every grant survives the grant -> PAM -> parseToken round-trip in its own bucket.
        // PNResourcePermissions(read, write, manage, delete, get, update, join, create)
        final PNToken pnToken = pubNubUnderTest.parseToken(grantTokenResponse.getToken());
        assertEquals(expectedTTL, pnToken.getTtl());

        assertEquals(new PNToken.PNResourcePermissions(true, true, false, false, false, false, false, false),
                pnToken.getResources().getChannels().get(channelId));
        assertEquals(new PNToken.PNResourcePermissions(true, false, false, false, false, false, false, false),
                pnToken.getPatterns().getChannels().get(channelPattern));

        assertEquals(new PNToken.PNResourcePermissions(true, false, true, false, false, false, false, false),
                pnToken.getResources().getChannelGroups().get(channelGroupId));
        assertEquals(new PNToken.PNResourcePermissions(true, false, false, false, false, false, false, false),
                pnToken.getPatterns().getChannelGroups().get(channelGroupPattern));

        // DataSyncGrant.channel permissions land in the plain `channels` bucket.
        assertEquals(new PNToken.PNResourcePermissions(false, false, false, false, true, true, false, false),
                pnToken.getResources().getChannels().get(dataSyncChannelId));
        assertEquals(new PNToken.PNResourcePermissions(false, false, false, false, true, false, false, true),
                pnToken.getPatterns().getChannels().get(dataSyncChannelPattern));

        // DataSyncGrant.subscribe(id, projection) is a pub/sub read on the projection's ref-channel.
        assertEquals(new PNToken.PNResourcePermissions(true, false, false, false, false, false, false, false),
                pnToken.getResources().getChannels().get("__adminProjection__" + userId));

        // DataSyncGrant.user permissions land in the plain `users` bucket (not `uuids`).
        assertEquals(new PNToken.PNResourcePermissions(false, false, false, false, true, true, false, false),
                pnToken.getResources().getUsers().get(userId));
        assertEquals(new PNToken.PNResourcePermissions(false, false, false, false, true, false, false, true),
                pnToken.getPatterns().getUsers().get(userPattern));

        assertEquals(new PNToken.PNResourcePermissions(false, false, false, false, true, true, false, false),
                pnToken.getResources().getDatasyncEntities().get(entityId));
        assertEquals(new PNToken.PNResourcePermissions(false, false, false, false, true, false, false, false),
                pnToken.getPatterns().getDatasyncEntities().get(entityPattern));
        assertEquals(new PNToken.PNResourcePermissions(false, false, false, false, true, false, false, false),
                pnToken.getResources().getDatasyncRelationships().get(relationshipId));
        assertEquals(new PNToken.PNResourcePermissions(false, false, false, false, true, false, false, false),
                pnToken.getPatterns().getDatasyncRelationships().get(relationshipPattern));
        assertEquals(new PNToken.PNResourcePermissions(false, false, false, false, true, false, false, false),
                pnToken.getResources().getDatasyncMemberships().get(membershipId));
        assertEquals(new PNToken.PNResourcePermissions(false, false, false, false, true, false, false, false),
                pnToken.getPatterns().getDatasyncMemberships().get(membershipPattern));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void grantToken_carriesDataSyncProjectionsInMeta() throws PubNubException {
        // given — projections declared inline on the DataSync grants; the SDK derives the pn-projections meta.
        PubNub pubNubUnderTest = getServer();
        final int expectedTTL = 1337;
        final String adminProjection = "admin";
        final String defaultProjection = "__default__";
        final String entityId = "user.A";
        final String entityPatternId = "user.*";
        // relationship id keeps its natural colon separator — the SDK must pass it through verbatim into the key.
        final String relationshipId = "user.A:channel.X";
        final String relationshipPatternId = "rel.*";
        final String membershipId = "user-123:channel-X";
        final String membershipPatternId = "mem.*";

        // composite keys the SDK is expected to build: "<namespace>:<id>"
        final String entityKey = DataSyncGrant.DATASYNC_ENTITIES + ":" + entityId;
        final String entityPatternKey = DataSyncGrant.DATASYNC_ENTITIES + ":" + entityPatternId;
        final String relationshipKey = DataSyncGrant.DATASYNC_RELATIONSHIPS + ":" + relationshipId;
        final String relationshipPatternKey = DataSyncGrant.DATASYNC_RELATIONSHIPS + ":" + relationshipPatternId;
        final String membershipKey = DataSyncGrant.DATASYNC_MEMBERSHIPS + ":" + membershipId;
        final String membershipPatternKey = DataSyncGrant.DATASYNC_MEMBERSHIPS + ":" + membershipPatternId;

        // when
        final PNGrantTokenResult grantTokenResponse = pubNubUnderTest
                .grantToken(expectedTTL)
                .channels(Arrays.asList(ChannelGrant.name("anyChannel").read()))
                .grants(Arrays.<TokenGrant>asList(
                        DataSyncGrant.entity(entityId).get().update().projection(adminProjection),
                        DataSyncGrant.entityPattern(entityPatternId).get().projection(defaultProjection),
                        DataSyncGrant.relationship(relationshipId).get().projection(adminProjection),
                        DataSyncGrant.relationshipPattern(relationshipPatternId).get().projection(defaultProjection),
                        // memberships take no projection, so they must not add a pn-projections entry
                        DataSyncGrant.membership(membershipId).get(),
                        DataSyncGrant.membershipPattern(membershipPatternId).get()))
                .sync();

        // then — the pn-projections block must survive the round-trip and surface on the typed projections field.
        final PNToken pnToken = pubNubUnderTest.parseToken(grantTokenResponse.getToken());
        assertEquals(expectedTTL, pnToken.getTtl());

        // the parser lifts pn-projections into the typed field, split by namespace with bare ids as keys.
        final PNDataSyncProjections projections = pnToken.getProjections();
        final PNDataSyncProjectionScope typedRes = projections.getResources();
        assertEquals(adminProjection, typedRes.getEntities().get(entityId));
        assertEquals(adminProjection, typedRes.getRelationships().get(relationshipId)); // colon in id survives verbatim
        assertTrue(typedRes.getMemberships().isEmpty());

        final PNDataSyncProjectionScope typedPat = projections.getPatterns();
        assertEquals(defaultProjection, typedPat.getEntities().get(entityPatternId));
        assertEquals(defaultProjection, typedPat.getRelationships().get(relationshipPatternId));
        assertTrue(typedPat.getMemberships().isEmpty());
        // the membership grants themselves still land in the token
        assertTrue(pnToken.getResources().getDatasyncMemberships().containsKey(membershipId));
        assertTrue(pnToken.getPatterns().getDatasyncMemberships().containsKey(membershipPatternId));

        // the raw block also remains available under meta (additive, non-breaking).
        final Map<String, Object> meta = (Map<String, Object>) pnToken.getMeta();
        final Map<String, Object> rawProjections = (Map<String, Object>) meta.get("pn-projections");

        // every value is a flat composite key -> single projection-name string (entities, relationships, memberships alike)
        final Map<String, Object> res = (Map<String, Object>) rawProjections.get("res");
        assertEquals(adminProjection, res.get(entityKey));
        assertEquals(adminProjection, res.get(relationshipKey)); // colon in the relationship id survives verbatim
        assertFalse(res.containsKey(membershipKey));

        final Map<String, Object> pat = (Map<String, Object>) rawProjections.get("pat");
        assertEquals(defaultProjection, pat.get(entityPatternKey));
        assertEquals(defaultProjection, pat.get(relationshipPatternKey));
        assertFalse(pat.containsKey(membershipPatternKey));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void grantToken_keepsCallerMetaAlongsideProjections() throws PubNubException {
        // given — the caller supplies their own plain meta (a pn-projections key in it is rejected). The SDK must add
        // the grant-derived pn-projections block next to it without dropping the caller's keys.
        PubNub pubNubUnderTest = getServer();
        final int expectedTTL = 1337;
        final String adminProjection = "admin";
        final String entityId = "user.A";

        final String entityKey = DataSyncGrant.DATASYNC_ENTITIES + ":" + entityId;

        final Map<String, Object> callerMeta = new HashMap<>();
        callerMeta.put("caller-key", "caller-value");

        // when
        final PNGrantTokenResult grantTokenResponse = pubNubUnderTest
                .grantToken(expectedTTL)
                .meta(callerMeta)
                .grants(Arrays.<TokenGrant>asList(
                        DataSyncGrant.entity(entityId).get().update().projection(adminProjection)))
                .authorizedUserId(new UserId("pam-debug-admin"))
                .channels(Arrays.asList(ChannelGrant.name("anyChannel").read()))
                .sync();

        // then
        final PNToken pnToken = pubNubUnderTest.parseToken(grantTokenResponse.getToken());
        assertEquals(expectedTTL, pnToken.getTtl());

        final Map<String, Object> meta = (Map<String, Object>) pnToken.getMeta();
        // caller-supplied plain meta must survive alongside the generated pn-projections block
        assertEquals("caller-value", meta.get("caller-key"));

        final Map<String, Object> projections = (Map<String, Object>) meta.get("pn-projections");
        final Map<String, Object> res = (Map<String, Object>) projections.get("res");
        assertEquals(Collections.singletonMap(entityKey, adminProjection), res);

        // the block also surfaces on the typed field, split by namespace with bare ids as keys.
        assertEquals(adminProjection, pnToken.getProjections().getResources().getEntities().get(entityId));
    }

}
