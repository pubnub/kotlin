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
import com.pubnub.api.models.consumer.access_manager.v3.PNDataSyncProjectionScope;
import com.pubnub.api.models.consumer.access_manager.v3.PNDataSyncProjections;
import com.pubnub.api.models.consumer.access_manager.v3.PNGrantTokenResult;
import com.pubnub.api.models.consumer.access_manager.v3.PNToken;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
    public void can_grantToken_for_datasync_resources_and_patterns() throws PubNubException {
        //given
        PubNub pubNubUnderTest = getServer();
        final int expectedTTL = 1337;
        final String entityName = "capy-001";
        final String membershipName = "user-123:channel-X";

        //when — full fluent Java-facing DataSync grant chain
        final PNGrantTokenResult grantTokenResponse = pubNubUnderTest
                .grantToken(expectedTTL)
                .authorizedUUID("pam-debug-admin")
                .dataSync(Arrays.asList(
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
                .dataSync(Arrays.asList(
                        DataSyncGrant.entity(entityId).get().update().projection(adminProjection),
                        DataSyncGrant.entityPattern(entityPatternId).get().projection(defaultProjection),
                        DataSyncGrant.relationship(relationshipId).get().projection(adminProjection),
                        DataSyncGrant.relationshipPattern(relationshipPatternId).get().projection(defaultProjection),
                        DataSyncGrant.membership(membershipId).get().projection(adminProjection),
                        DataSyncGrant.membershipPattern(membershipPatternId).get().projection(defaultProjection)))
                .sync();

        // then — the pn-projections block must survive the round-trip and surface on the typed projections field.
        final PNToken pnToken = pubNubUnderTest.parseToken(grantTokenResponse.getToken());
        assertEquals(expectedTTL, pnToken.getTtl());

        // the parser lifts pn-projections into the typed field, split by namespace with bare ids as keys.
        final PNDataSyncProjections projections = pnToken.getProjections();
        final PNDataSyncProjectionScope typedRes = projections.getResources();
        assertEquals(adminProjection, typedRes.getEntities().get(entityId));
        assertEquals(adminProjection, typedRes.getRelationships().get(relationshipId)); // colon in id survives verbatim
        assertEquals(adminProjection, typedRes.getMemberships().get(membershipId)); // colon in id survives verbatim

        final PNDataSyncProjectionScope typedPat = projections.getPatterns();
        assertEquals(defaultProjection, typedPat.getEntities().get(entityPatternId));
        assertEquals(defaultProjection, typedPat.getRelationships().get(relationshipPatternId));
        assertEquals(defaultProjection, typedPat.getMemberships().get(membershipPatternId));

        // the raw block also remains available under meta (additive, non-breaking).
        final Map<String, Object> meta = (Map<String, Object>) pnToken.getMeta();
        final Map<String, Object> rawProjections = (Map<String, Object>) meta.get("pn-projections");

        // every value is a flat composite key -> single projection-name string (entities, relationships, memberships alike)
        final Map<String, Object> res = (Map<String, Object>) rawProjections.get("res");
        assertEquals(adminProjection, res.get(entityKey));
        assertEquals(adminProjection, res.get(relationshipKey)); // colon in the relationship id survives verbatim
        assertEquals(adminProjection, res.get(membershipKey)); // colon in the membership id survives verbatim

        final Map<String, Object> pat = (Map<String, Object>) rawProjections.get("pat");
        assertEquals(defaultProjection, pat.get(entityPatternKey));
        assertEquals(defaultProjection, pat.get(relationshipPatternKey));
        assertEquals(defaultProjection, pat.get(membershipPatternKey));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void grantToken_mergesCallerSuppliedProjectionsIntoMeta() throws PubNubException {
        // given — the caller supplies their own meta carrying both a plain value and a pn-projections block. The SDK
        // must overlay the grant-derived projections onto that meta: the plain value survives, a caller projection for
        // a key no grant carries survives verbatim, and a caller projection colliding with a grant loses to the grant.
        PubNub pubNubUnderTest = getServer();
        final int expectedTTL = 1337;
        final String adminProjection = "admin";
        final String entityId = "user.A";

        final String entityKey = DataSyncGrant.DATASYNC_ENTITIES + ":" + entityId;

        // a projection the caller injects directly into meta for a resource NO grant carries — it must survive verbatim.
        final String callerOnlyKey = DataSyncGrant.DATASYNC_MEMBERSHIPS + ":user-123:channel-X";
        final String callerOnlyProjection = "caller-only";
        // a projection the caller sets for the SAME key a grant also generates — the grant-derived value must win.
        final String callerColliding = "caller-should-lose";

        final Map<String, Object> callerRes = new HashMap<>();
        callerRes.put(callerOnlyKey, callerOnlyProjection);
        callerRes.put(entityKey, callerColliding);
        final Map<String, Object> callerProjections = new HashMap<>();
        callerProjections.put("res", callerRes);
        final Map<String, Object> callerMeta = new HashMap<>();
        callerMeta.put("caller-key", "caller-value");
        callerMeta.put("pn-projections", callerProjections);

        // when
        final PNGrantTokenResult grantTokenResponse = pubNubUnderTest
                .grantToken(expectedTTL)
                .meta(callerMeta)
                .channels(Arrays.asList(ChannelGrant.name("anyChannel").read()))
                .dataSync(Arrays.asList(
                        DataSyncGrant.entity(entityId).get().update().projection(adminProjection)))
                .sync();

        // then
        final PNToken pnToken = pubNubUnderTest.parseToken(grantTokenResponse.getToken());
        assertEquals(expectedTTL, pnToken.getTtl());

        final Map<String, Object> meta = (Map<String, Object>) pnToken.getMeta();
        // caller-supplied plain meta must survive the merge alongside the generated pn-projections block
        assertEquals("caller-value", meta.get("caller-key"));

        final Map<String, Object> projections = (Map<String, Object>) meta.get("pn-projections");
        final Map<String, Object> res = (Map<String, Object>) projections.get("res");
        assertEquals(adminProjection, res.get(entityKey)); // grant-derived value wins over the caller's colliding entry
        assertEquals(callerOnlyProjection, res.get(callerOnlyKey)); // caller projection for a key no grant carries survives

        // the merged block also surfaces on the typed field, split by namespace with bare ids as keys.
        final PNDataSyncProjectionScope typedRes = pnToken.getProjections().getResources();
        assertEquals(adminProjection, typedRes.getEntities().get(entityId)); // grant wins over caller's colliding entry
        // callerOnlyKey = "datasync:memberships:user-123:channel-X" -> membership bare id "user-123:channel-X"
        assertEquals(callerOnlyProjection, typedRes.getMemberships().get("user-123:channel-X"));
    }

}
