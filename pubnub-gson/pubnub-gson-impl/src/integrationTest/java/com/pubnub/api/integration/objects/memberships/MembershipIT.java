package com.pubnub.api.integration.objects.memberships;

import com.pubnub.api.PubNubException;
import com.pubnub.api.integration.objects.ObjectsApiBaseIT;
import com.pubnub.api.java.models.consumer.objects_api.membership.MembershipInclude;
import com.pubnub.api.java.models.consumer.objects_api.membership.PNChannelMembership;
import com.pubnub.api.java.models.consumer.objects_api.membership.PNGetMembershipsResult;
import com.pubnub.api.java.models.consumer.objects_api.membership.PNManageMembershipResult;
import com.pubnub.api.java.models.consumer.objects_api.membership.PNMembership;
import com.pubnub.api.java.models.consumer.objects_api.membership.PNRemoveMembershipResult;
import com.pubnub.api.java.models.consumer.objects_api.membership.PNSetMembershipResult;
import com.pubnub.api.utils.PatchValue;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.pubnub.api.integration.util.Utils.random;
import static com.pubnub.api.java.endpoints.objects_api.utils.Include.PNChannelDetailsLevel.CHANNEL_WITH_CUSTOM;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class MembershipIT extends ObjectsApiBaseIT {
    private static final Logger LOG = LoggerFactory.getLogger(MembershipIT.class);
    private static final String STATUS = "status";
    private static final String CHANNEL = "channel";

    private final String testChannelId1 = UUID.randomUUID().toString();
    private final String testChannelId2 = UUID.randomUUID().toString();

    private final String membership01ChannelId = random();
    private final String membership01Status = "active";
    private final String membership01Type = "admin";
    private final String membership02ChannelId = random();
    private final String membership02Status = "inactive";
    private final String membership02Type = "member";
    private final String membership03ChannelId = random();
    private final String membership04ChannelId = random();
    private final String membership05ChannelId = random();
    private final String membership06ChannelId = random();
    private final String userId = random().toString();

    private final List<PNSetMembershipResult> createdMembershipsList = new ArrayList<>();

    @Test
    public void setMembershipsHappyPath() throws PubNubException  {
        final List<PNChannelMembership> channelMemberships = buildChannelMemberships(
                membership01ChannelId,
                membership01Status,
                membership01Type,
                membership02ChannelId,
                membership02Status,
                membership02Type,
                membership03ChannelId,
                membership04ChannelId,
                membership05ChannelId,
                membership06ChannelId
        );

        PNSetMembershipResult setMembershipResult = pubNubUnderTest
                .setMemberships(channelMemberships)
                .userId(userId).limit(10)
                .include(MembershipInclude.builder()
                        .includeCustom(true)
                        .includeStatus(true)
                        .includeType(true)
                        .includeTotalCount(true)
                        .includeChannel(true)
                        .includeChannelCustom(true)
                        .includeChannelType(true)
                        .includeChannelStatus(true)
                        .build())
                .sync();

        createdMembershipsList.add(setMembershipResult);

        PNMembership pnMembership01 = getMembershipByChannelId(setMembershipResult, membership01ChannelId);
        assertEquals(membership01Status, pnMembership01.getStatus().getValue());
        assertEquals(membership01Type, pnMembership01.getType().getValue());

        PNMembership pnMembership02 = getMembershipByChannelId(setMembershipResult, membership02ChannelId);
        assertEquals(membership02Status, pnMembership02.getStatus().getValue());
        assertEquals(membership02Type, pnMembership02.getType().getValue());
        assertNotNull(pnMembership02.getCustom().getValue());

        PNMembership pnMembership03 = getMembershipByChannelId(setMembershipResult, membership03ChannelId);
        assertNull(pnMembership03.getStatus().getValue());
        assertNull(pnMembership03.getType().getValue());

        PNMembership pnMembership04 = getMembershipByChannelId(setMembershipResult, membership04ChannelId);
        assertNull(pnMembership04.getStatus().getValue());
        assertNull(pnMembership04.getType().getValue());
        assertNotNull(pnMembership04.getCustom().getValue());

        PNMembership pnMembership05 = getMembershipByChannelId(setMembershipResult, membership05ChannelId);
        assertNull(pnMembership05.getStatus().getValue());
        assertNull(pnMembership05.getType().getValue());
        assertNull(pnMembership05.getCustom().getValue());

        PNMembership pnMembership06 = getMembershipByChannelId(setMembershipResult, membership06ChannelId);
        assertNull(pnMembership06.getStatus().getValue());
        assertNull(pnMembership06.getType().getValue());
        assertNotNull(pnMembership06.getCustom().getValue());
    }

    @Test
    public void setMembershipsHappyPath_deprecated() throws PubNubException {
        //given
        final List<PNChannelMembership> channelMemberships = Arrays.asList(
                PNChannelMembership.channel(testChannelId1),
                PNChannelMembership.channelWithCustom(testChannelId2, customChannelMembershipObject()));

        //when
        final PNSetMembershipResult setMembershipResult = pubNubUnderTest.setMemberships()
                .channelMemberships(channelMemberships)
                .includeTotalCount(true)
                .includeCustom(true)
                .includeChannel(CHANNEL_WITH_CUSTOM)
                .sync();

        //then
        assertThat(setMembershipResult, allOf(
                notNullValue(),
                hasProperty(STATUS, is(HttpStatus.SC_OK))
        ));
        createdMembershipsList.add(setMembershipResult);

        assertThat(setMembershipResult, allOf(
                hasProperty("data",
                        hasItem(
                                allOf(
                                        hasProperty(CHANNEL,
                                                hasProperty("id", is(testChannelId1))),
                                        hasProperty("custom", is(PatchValue.of(null)))))),
                hasProperty("data",
                        hasItem(
                                allOf(
                                        hasProperty(CHANNEL,
                                                hasProperty("id", is(testChannelId2))),
                                        hasProperty("custom", notNullValue()))))));
    }

    @Test
    public void getMembershipsHappyPath() throws PubNubException {
        final List<PNChannelMembership> channelMemberships = buildChannelMemberships(
                membership01ChannelId,
                membership01Status,
                membership01Type,
                membership02ChannelId,
                membership02Status,
                membership02Type,
                membership03ChannelId,
                membership04ChannelId,
                membership05ChannelId,
                membership06ChannelId
        );

        PNSetMembershipResult setMembershipResult = pubNubUnderTest
                .setMemberships(channelMemberships)
                .userId(userId).limit(10)
                .sync();

        createdMembershipsList.add(setMembershipResult);

        //when
        final PNGetMembershipsResult getMembershipsResult = pubNubUnderTest.getMemberships(userId)
                .limit(10)
                .include(MembershipInclude.builder()
                        .includeCustom(true)
                        .includeStatus(true)
                        .includeType(true)
                        .includeTotalCount(true)
                        .includeChannel(true)
                        .includeChannelCustom(true)
                        .includeChannelType(true)
                        .includeChannelStatus(true)
                        .build())
                .sync();

        PNMembership pnMembership01 = getMembershipByChannelId(getMembershipsResult, membership01ChannelId);
        assertEquals(membership01Status, pnMembership01.getStatus().getValue());
        assertEquals(membership01Type, pnMembership01.getType().getValue());

        PNMembership pnMembership02 = getMembershipByChannelId(getMembershipsResult, membership02ChannelId);
        assertEquals(membership02Status, pnMembership02.getStatus().getValue());
        assertEquals(membership02Type, pnMembership02.getType().getValue());
        assertNotNull(pnMembership02.getCustom().getValue());

        PNMembership pnMembership03 = getMembershipByChannelId(getMembershipsResult, membership03ChannelId);
        assertNull(pnMembership03.getStatus().getValue());
        assertNull(pnMembership03.getType().getValue());

        PNMembership pnMembership04 = getMembershipByChannelId(getMembershipsResult, membership04ChannelId);
        assertNull(pnMembership04.getStatus().getValue());
        assertNull(pnMembership04.getType().getValue());
        assertNotNull(pnMembership04.getCustom().getValue());

        PNMembership pnMembership05 = getMembershipByChannelId(getMembershipsResult, membership05ChannelId);
        assertNull(pnMembership05.getStatus().getValue());
        assertNull(pnMembership05.getType().getValue());
        assertNull(pnMembership05.getCustom().getValue());

        PNMembership pnMembership06 = getMembershipByChannelId(getMembershipsResult, membership06ChannelId);
        assertEquals(membership06ChannelId, pnMembership06.getChannel().getId());
        assertNull(pnMembership06.getStatus().getValue());
        assertNull(pnMembership06.getType().getValue());
        assertNotNull(pnMembership06.getCustom().getValue());
    }

    private PNMembership getMembershipByChannelId(PNGetMembershipsResult getMembershipsResult, String membershipId) {
        return getMembershipsResult.getData().stream().filter(membership -> membershipId.equals(membership.getChannel().getId())).collect(Collectors.toList()).get(0);
    }

    private PNMembership getMembershipByChannelId(PNSetMembershipResult setMembershipResult, String membershipId) {
        return setMembershipResult.getData().stream().filter(membership -> membershipId.equals(membership.getChannel().getId())).collect(Collectors.toList()).get(0);
    }

    private PNMembership getMembershipByChannelId(PNRemoveMembershipResult removeMembershipsResult, String membershipId) {
        return removeMembershipsResult.getData().stream().filter(membership -> membershipId.equals(membership.getChannel().getId())).collect(Collectors.toList()).get(0);
    }

    @Test
    public void getMembershipsHappyPathDeprecated() throws PubNubException {
        //given
        final List<PNChannelMembership> channelMemberships = Arrays.asList(
                PNChannelMembership.channel(testChannelId1),
                PNChannelMembership.channelWithCustom(testChannelId2, customChannelMembershipObject()));

        final PNSetMembershipResult setMembershipResult = pubNubUnderTest.setMemberships()
                .channelMemberships(channelMemberships)
                .includeTotalCount(true)
                .includeCustom(true)
                .includeChannel(CHANNEL_WITH_CUSTOM)
                .sync();
        createdMembershipsList.add(setMembershipResult);

        //when
        final PNGetMembershipsResult getMembershipsResult = pubNubUnderTest.getMemberships()
                .includeTotalCount(true)
                .includeCustom(true)
                .includeChannel(CHANNEL_WITH_CUSTOM)
                .sync();

        //then
        assertThat(getMembershipsResult, allOf(
                notNullValue(),
                hasProperty(STATUS, is(HttpStatus.SC_OK)),
                hasProperty("data",
                        hasItem(hasProperty(CHANNEL,
                                hasProperty("id", is(testChannelId1))))),
                hasProperty("data",
                        hasItem(hasProperty(CHANNEL,
                                hasProperty("id", is(testChannelId2)))))));
    }

    @Test
    public void removeMembershipsHappyPath() throws PubNubException {
        //given
        final List<PNChannelMembership> channelMemberships = buildChannelMemberships(
                membership01ChannelId,
                membership01Status,
                membership01Type,
                membership02ChannelId,
                membership02Status,
                membership02Type,
                membership03ChannelId,
                membership04ChannelId,
                membership05ChannelId,
                membership06ChannelId
        );

        final PNSetMembershipResult setMembershipResult = pubNubUnderTest.setMemberships(channelMemberships)
                .include(MembershipInclude.builder()
                        .includeTotalCount(true)
                        .includeCustom(true)
                        .includeChannel(true)
                        .includeChannelCustom(true)
                        .build())
                .sync();
        createdMembershipsList.add(setMembershipResult);

        //when
        List<PNChannelMembership> channelMembershipsToRemove = Collections.singletonList(PNChannelMembership.builder(testChannelId2).build());
        final PNRemoveMembershipResult removeMembershipResult = pubNubUnderTest.removeMemberships(channelMembershipsToRemove)
                .include(MembershipInclude.builder()
                        .includeTotalCount(true)
                        .includeCustom(true)
                        .includeChannel(true)
                        .includeType(true)
                        .includeStatus(true)
                        .build())
                .sync();

        //then
        PNMembership pnMembership01 = getMembershipByChannelId(removeMembershipResult, membership01ChannelId);
        assertEquals(membership01Status, pnMembership01.getStatus().getValue());
        assertEquals(membership01Type, pnMembership01.getType().getValue());
    }

    @Test
    public void removeMembershipsHappyPathDeprecated() throws PubNubException {
        //given
        final List<PNChannelMembership> channelMemberships = Arrays.asList(PNChannelMembership.channel(testChannelId1),
                PNChannelMembership.channelWithCustom(testChannelId2, customChannelMembershipObject()));

        final PNSetMembershipResult setMembershipResult = pubNubUnderTest.setMemberships()
                .channelMemberships(channelMemberships)
                .includeTotalCount(true)
                .includeCustom(true)
                .includeChannel(CHANNEL_WITH_CUSTOM)
                .sync();
        createdMembershipsList.add(setMembershipResult);

        //when
        final PNRemoveMembershipResult removeMembershipResult = pubNubUnderTest.removeMemberships()
                .channelMemberships(Collections.singletonList(PNChannelMembership.channel(testChannelId2)))
                .includeTotalCount(true)
                .includeCustom(true)
                .includeChannel(CHANNEL_WITH_CUSTOM)
                .sync();

        //then
        assertThat(removeMembershipResult, allOf(
                notNullValue(),
                hasProperty(STATUS, is(HttpStatus.SC_OK)),
                hasProperty("data",
                        hasItem(hasProperty(CHANNEL,
                                hasProperty("id", is(testChannelId1))))),
                hasProperty("data",
                        hasItem(hasProperty(CHANNEL,
                                not(hasProperty("id", is(testChannelId2))))))));

    }

    @Test
    public void manageMembershipsHappyPath() throws PubNubException {
        //given
        final List<PNChannelMembership> channelMembershipsToRemove = Collections.singletonList(
                PNChannelMembership.channelWithCustom(testChannelId1, customChannelMembershipObject()));

        final PNSetMembershipResult setMembershipResult = pubNubUnderTest.setMemberships()
                .channelMemberships(channelMembershipsToRemove)
                .includeTotalCount(true)
                .includeCustom(true)
                .includeChannel(CHANNEL_WITH_CUSTOM)
                .sync();
        createdMembershipsList.add(setMembershipResult);

        final List<PNChannelMembership> channelMembershipsToSet = Collections.singletonList(
                PNChannelMembership.channelWithCustom(testChannelId2, customChannelMembershipObject()));

        //when
        final PNManageMembershipResult manageMembershipResult = pubNubUnderTest.manageMemberships()
                .set(channelMembershipsToSet)
                .remove(channelMembershipsToRemove)
                .includeTotalCount(true)
                .includeCustom(true)
                .includeChannel(CHANNEL_WITH_CUSTOM)
                .sync();

        createdMembershipsList.add(new PNSetMembershipResult(manageMembershipResult));

        //then
        assertThat(manageMembershipResult, allOf(
                notNullValue(),
                hasProperty(STATUS, is(HttpStatus.SC_OK)),
                hasProperty("data", allOf(
                        hasItem(hasProperty(CHANNEL,
                                hasProperty("id", is(channelMembershipsToSet.get(0).getChannel().getId())))),
                        not(hasItem(hasProperty(CHANNEL,
                                hasProperty("id", is(channelMembershipsToRemove.get(0).getChannel().getId())))))))));
    }

    @After
    public void cleanUp() {
        for (final PNSetMembershipResult createdMembership : createdMembershipsList) {
            try {
                final List<PNChannelMembership> channelMemberships = new ArrayList<>();
                for (final PNMembership it : createdMembership.getData()) {
                    final String id = it.getChannel().getId();
                    final PNChannelMembership pnChannelMembership = PNChannelMembership.channel(id);
                    channelMemberships.add(pnChannelMembership);
                }

                pubNubUnderTest.removeMemberships(channelMemberships).sync();

            } catch (Exception e) {
                LOG.warn("Could not cleanup {}", createdMembership, e);
            }
        }
    }

    private static Map<String, Object> customChannelMembershipObject() {
        final Map<String, Object> customMap = new HashMap<>();
        customMap.putIfAbsent("membership_param1", "val1");
        customMap.putIfAbsent("membership_param2", "val2");
        return customMap;
    }

    private  List<PNChannelMembership> buildChannelMemberships(String membership01ChannelId, String membership01Status, String membership01Type, String membership02ChannelId, String membership02Status, String membership02Type, String membership03ChannelId, String membership04ChannelId, String membership05ChannelId, String membership06ChannelId) {
        return Arrays.asList(
                PNChannelMembership.builder(membership01ChannelId)
                        .status(membership01Status)
                        .type(membership01Type)
                        .build(),
                PNChannelMembership.builder(membership02ChannelId)
                        .custom(customChannelMembershipObject())
                        .status(membership02Status)
                        .type(membership02Type)
                        .build(),
                PNChannelMembership.channel(membership03ChannelId), // this is deprecated usage
                PNChannelMembership.channelWithCustom(membership04ChannelId, customChannelMembershipObject()), // this is deprecated usage
                new PNChannelMembership.JustChannel(new PNChannelMembership.ChannelId(membership05ChannelId)), // this is deprecated usage
                new PNChannelMembership.ChannelWithCustom(new PNChannelMembership.ChannelId(membership06ChannelId), customChannelMembershipObject()) // this is deprecated usage
        );
    }
}
