package com.pubnub.api.integration.objects.members;

import com.pubnub.api.PubNubException;
import com.pubnub.api.integration.objects.ObjectsApiBaseIT;
import com.pubnub.api.java.models.consumer.objects_api.member.MemberInclude;
import com.pubnub.api.java.models.consumer.objects_api.member.PNGetChannelMembersResult;
import com.pubnub.api.java.models.consumer.objects_api.member.PNManageChannelMembersResult;
import com.pubnub.api.java.models.consumer.objects_api.member.PNMembers;
import com.pubnub.api.java.models.consumer.objects_api.member.PNRemoveChannelMembersResult;
import com.pubnub.api.java.models.consumer.objects_api.member.PNSetChannelMembersResult;
import com.pubnub.api.java.models.consumer.objects_api.member.PNUUID;
import com.pubnub.api.java.models.consumer.objects_api.member.PNUser;
import com.pubnub.api.java.models.consumer.objects_api.uuid.PNUUIDMetadata;
import com.pubnub.api.utils.PatchValue;
import org.apache.http.HttpStatus;
import org.jetbrains.annotations.Nullable;
import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.pubnub.api.integration.util.Utils.random;
import static com.pubnub.api.java.endpoints.objects_api.utils.Include.PNUUIDDetailsLevel.UUID_WITH_CUSTOM;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ChannelMembersIT extends ObjectsApiBaseIT {
    private static final Logger LOG = LoggerFactory.getLogger(ChannelMembersIT.class);
    private static final String STATUS_01 = "myStatus01";
    private static final String TYPE_01 = "myType01" + random();
    private static final String TYPE_02 = "myType02" + random();
    private static final String STATUS_02 = "myStatus02";

    private final List<PNSetChannelMembersResult> createdMembersList = new ArrayList<>();

    private final String TEST_UUID1 = UUID.randomUUID().toString();
    private final String TEST_UUID2 = UUID.randomUUID().toString();

    private final String testChannelId = UUID.randomUUID().toString();

    @Test
    public void addChannelMembersHappyPath() throws PubNubException {
        //given
        Map<String, Object> customMembershipObject = customChannelMembershipObject();
        final Collection<PNUser> channelMembers = Arrays.asList(
                PNUser.builder(TEST_UUID1).status(STATUS_01).type(TYPE_01).build(),
                PNUser.builder(TEST_UUID2).custom(customMembershipObject).status(STATUS_02).type(TYPE_02).build());

        //when
        final PNSetChannelMembersResult setChannelMembersResult = pubNubUnderTest.setChannelMembers(testChannelId, channelMembers)
                .include(MemberInclude.builder()
                        .includeCustom(true)
                        .includeStatus(true)
                        .includeType(true)
                        .includeTotalCount(true)
                        .includeUser(true)
                        .includeUserCustom(true)
                        .includeUserStatus(true)
                        .includeUserType(true)
                        .build())
                .sync();

        //then
        assertNotNull(setChannelMembersResult);
        assertEquals(HttpStatus.SC_OK, setChannelMembersResult.getStatus());
        createdMembersList.add(setChannelMembersResult);

        PNMembers pnMember01 = getChannelMemberByUserId(setChannelMembersResult, TEST_UUID1);
        assertEquals(STATUS_01, pnMember01.getStatus().getValue());
        assertEquals(TYPE_01, pnMember01.getType().getValue());
        PNMembers pnMember02 = getChannelMemberByUserId(setChannelMembersResult, TEST_UUID2);
        assertEquals(STATUS_02, pnMember02.getStatus().getValue());
        assertEquals(TYPE_02, pnMember02.getType().getValue());
        assertTrue(pnMember02.getCustom().getValue().containsKey("members_param1"));
        assertTrue(pnMember02.getCustom().getValue().containsKey("members_param2"));

    }

    @Test
    public void addChannelMembersHappyPathDeprecated() throws PubNubException {
        //given
        Map<String, Object> customMembershipObject = customChannelMembershipObject();
        final Collection<PNUUID> channelMembers = Arrays.asList(
                PNUUID.uuid(TEST_UUID1, STATUS_01),
                PNUUID.uuidWithCustom(TEST_UUID2, customMembershipObject));

        //when
        final PNSetChannelMembersResult setChannelMembersResult = pubNubUnderTest.setChannelMembers()
                .channel(testChannelId)
                .uuids(channelMembers)
                .includeTotalCount(true)
                .includeCustom(true)
                .includeUUID(UUID_WITH_CUSTOM)
                .sync();

        //then
        assertNotNull(setChannelMembersResult);
        assertEquals(HttpStatus.SC_OK, setChannelMembersResult.getStatus());
        createdMembersList.add(setChannelMembersResult);
        final List<String> returnedUUIDs = new ArrayList<>();
        for (final PNMembers pnMembers : setChannelMembersResult.getData()) {
            final PNUUIDMetadata uuid = pnMembers.getUuid();
            final String id = uuid.getId();
            returnedUUIDs.add(id);
        }
        final List<String> expectedUUIDs = new ArrayList<>();
        for (final PNUUID channelMember : channelMembers) {
            final PNUUID.UUIDId uuid = channelMember.getUuid();
            final String id = uuid.getId();
            expectedUUIDs.add(id);
        }

        final List<Object> receivedCustomObjects = new ArrayList<>();
        for (final PNMembers it : setChannelMembersResult.getData()) {
            PatchValue<@Nullable Map<String, Object>> custom = it.getCustom();
            if (custom != null && custom.getValue() != null) {
                receivedCustomObjects.add(custom);
            }
        }

        List<String> actualStatusList = setChannelMembersResult.getData()
                .stream()
                .map(PNMembers::getStatus)
                .map(PatchValue::getValue)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        assertThat(returnedUUIDs, containsInAnyOrder(expectedUUIDs.toArray()));
        assertThat(receivedCustomObjects, hasSize(1));
        assertThat(actualStatusList, containsInAnyOrder(STATUS_01));
    }

    @Test
    public void getChannelMembersHappyPath() throws PubNubException {
        //given
        final Collection<PNUser> channelMembers = Arrays.asList(
                PNUser.builder(TEST_UUID1).status(STATUS_01).type(TYPE_01).build(),
                PNUser.builder(TEST_UUID2).custom(customChannelMembershipObject()).status(STATUS_02).type(TYPE_02).build());

        final PNSetChannelMembersResult setChannelMembersResult = pubNubUnderTest
                .setChannelMembers(testChannelId, channelMembers)
                .include(MemberInclude.builder()
                        .includeTotalCount(true)
                        .includeCustom(true)
                        .includeUser(true)
                        .includeUserCustom(true)
                        .build())
                .sync();
        createdMembersList.add(setChannelMembersResult);

        //when
        final PNGetChannelMembersResult getMembersResult = pubNubUnderTest
                .getChannelMembers(testChannelId)
                .include(MemberInclude.builder()
                        .includeTotalCount(true)
                        .includeStatus(true)
                        .includeType(true)
                        .includeCustom(true)
                        .includeUser(true)
                        .includeUserCustom(true)
                        .build())
                .sync();


        //then
        assertNotNull(getMembersResult);
        assertEquals(HttpStatus.SC_OK, getMembersResult.getStatus());
        PNMembers pnMember01 = getChannelMemberByUserId(getMembersResult, TEST_UUID1);
        assertEquals(STATUS_01, pnMember01.getStatus().getValue());
        assertEquals(TYPE_01, pnMember01.getType().getValue());
        PNMembers pnMember02 = getChannelMemberByUserId(getMembersResult, TEST_UUID2);
        assertEquals(STATUS_02, pnMember02.getStatus().getValue());
        assertEquals(TYPE_02, pnMember02.getType().getValue());
        assertTrue(pnMember02.getCustom().getValue().containsKey("members_param1"));
        assertTrue(pnMember02.getCustom().getValue().containsKey("members_param2"));
    }

    @Test
    public void getChannelMembersHappyPathDeprecated() throws PubNubException {
        //given
        final Collection<PNUUID> channelMembers = Arrays.asList(
                PNUUID.uuid(TEST_UUID1),
                PNUUID.uuidWithCustom(TEST_UUID2, customChannelMembershipObject(), STATUS_02));

        final PNSetChannelMembersResult setChannelMembersResult = pubNubUnderTest.setChannelMembers()
                .channel(testChannelId)
                .uuids(channelMembers)
                .includeTotalCount(true)
                .includeCustom(true)
                .includeUUID(UUID_WITH_CUSTOM)
                .sync();
        createdMembersList.add(setChannelMembersResult);

        //when
        final PNGetChannelMembersResult getMembersResult = pubNubUnderTest.getChannelMembers()
                .channel(testChannelId)
                .includeTotalCount(true)
                .includeCustom(true)
                .includeUUID(UUID_WITH_CUSTOM)
                .sync();


        //then
        assertNotNull(getMembersResult);
        assertEquals(HttpStatus.SC_OK, getMembersResult.getStatus());
        final List<String> returnedUUIDs = new ArrayList<>();
        for (final PNMembers pnMembers : setChannelMembersResult.getData()) {
            final PNUUIDMetadata uuid = pnMembers.getUuid();
            final String id = uuid.getId();
            returnedUUIDs.add(id);
        }
        final List<String> expectedUUIDs = new ArrayList<>();
        for (final PNUUID channelMember : channelMembers) {
            final PNUUID.UUIDId uuid = channelMember.getUuid();
            String id = uuid.getId();
            expectedUUIDs.add(id);
        }
        final List<Object> receivedCustomObjects = new ArrayList<>();
        for (final PNMembers it : setChannelMembersResult.getData()) {
            PatchValue<@Nullable Map<String, Object>> custom = it.getCustom();
            if (custom != null && custom.getValue() != null) {
                receivedCustomObjects.add(custom);
            }
        }

        List<String> actualStatusList = setChannelMembersResult.getData()
                .stream()
                .map(PNMembers::getStatus)
                .map(PatchValue::getValue)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        assertThat(returnedUUIDs, containsInAnyOrder(expectedUUIDs.toArray()));
        assertThat(receivedCustomObjects, hasSize(1));
        assertThat(actualStatusList, containsInAnyOrder(STATUS_02));

    }
    @Test
    public void removeChannelMembersHappyPath() throws PubNubException {
        //given
        final Collection<PNUser> channelMembers = Arrays.asList(
                PNUser.builder(TEST_UUID1).status(STATUS_01).type(TYPE_01).build(),
                PNUser.builder(TEST_UUID2).custom(customChannelMembershipObject()).status(STATUS_02).type(TYPE_02).build());

        final PNSetChannelMembersResult setChannelMembersResult = pubNubUnderTest
                .setChannelMembers(testChannelId, channelMembers)
                .include(MemberInclude.builder()
                        .includeTotalCount(true)
                        .includeCustom(true)
                        .includeUser(true)
                        .includeUserCustom(true)
                        .build())
                .sync();
        createdMembersList.add(setChannelMembersResult);

        //when
        final PNRemoveChannelMembersResult removeMembersResult = pubNubUnderTest
                .removeChannelMembers(testChannelId, Collections.singletonList(PNUser.builder(TEST_UUID2).build()))
                .include(MemberInclude.builder()
                        .includeTotalCount(true)
                        .includeStatus(true)
                        .includeType(true)
                        .includeCustom(true)
                        .includeUser(true)
                        .includeUserCustom(true)
                        .build())
                .sync();

        //then
        final List<String> returnedUUIDs = new ArrayList<>();
        for (final PNMembers pnMembers : removeMembersResult.getData()) {
            final PNUUIDMetadata uuid = pnMembers.getUuid();
            final String id = uuid.getId();
            returnedUUIDs.add(id);
        }

        assertNotNull(removeMembersResult);
        assertEquals(HttpStatus.SC_OK, removeMembersResult.getStatus());
        assertThat(returnedUUIDs, not(hasItem(TEST_UUID2)));

        final PNGetChannelMembersResult getMembersResult = pubNubUnderTest
                .getChannelMembers(testChannelId)
                .include(MemberInclude.builder()
                        .includeTotalCount(true)
                        .includeStatus(true)
                        .includeType(true)
                        .includeCustom(true)
                        .includeUser(true)
                        .includeUserCustom(true)
                        .build())
                .sync();

        List<String> userIdsOfRemainingMembers = getMembersResult.getData().stream().map(pnMember -> pnMember.getUuid().getId()).collect(Collectors.toList());
        assertTrue(userIdsOfRemainingMembers.contains(TEST_UUID1));
        assertFalse(userIdsOfRemainingMembers.contains(TEST_UUID2));
    }

    @Test
    public void removeChannelMembersHappyPathDeprecated() throws PubNubException {
        //given
        final Collection<PNUUID> channelMembers = Arrays.asList(
                PNUUID.uuid(TEST_UUID1, STATUS_01),
                PNUUID.uuidWithCustom(TEST_UUID2, customChannelMembershipObject(), STATUS_02));

        final PNSetChannelMembersResult setChannelMembersResult = pubNubUnderTest.setChannelMembers()
                .channel(testChannelId)
                .uuids(channelMembers)
                .includeTotalCount(true)
                .includeCustom(true)
                .includeUUID(UUID_WITH_CUSTOM)
                .sync();
        createdMembersList.add(setChannelMembersResult);

        //when
        final PNRemoveChannelMembersResult removeMembersResult = pubNubUnderTest
                .removeChannelMembers()
                .channel(testChannelId)
                .uuids(Collections.singletonList(PNUUID.uuid(TEST_UUID2)))
                .includeTotalCount(true)
                .includeCustom(true)
                .includeUUID(UUID_WITH_CUSTOM)
                .sync();

        //then
        final List<String> returnedUUIDs = new ArrayList<>();
        for (final PNMembers pnMembers : removeMembersResult.getData()) {
            final PNUUIDMetadata uuid = pnMembers.getUuid();
            final String id = uuid.getId();
            returnedUUIDs.add(id);
        }

        assertNotNull(removeMembersResult);
        assertEquals(HttpStatus.SC_OK, removeMembersResult.getStatus());
        assertThat(returnedUUIDs, not(hasItem(TEST_UUID2)));
    }

    @Test
    public void manageChannelMembersHappyPath() throws PubNubException {
        //given
        final List<PNUser> channelMembersToRemove = Collections.singletonList(
                PNUser.builder(TEST_UUID1)
                        .custom(customChannelMembershipObject())
                        .status(STATUS_01)
                        .type(TYPE_01)
                        .build()
        );

        final PNSetChannelMembersResult setChannelMembersResult = pubNubUnderTest
                .setChannelMembers(testChannelId, channelMembersToRemove)
                .sync();
        createdMembersList.add(setChannelMembersResult);

        final List<PNUser> channelMembersToSet = Collections.singletonList(
                PNUser.builder(TEST_UUID2).custom(customChannelMembershipObject()).status(STATUS_02).type(TYPE_01).build());

        //when
        final PNManageChannelMembersResult manageChannelMembersResult = pubNubUnderTest
                .manageChannelMembers(testChannelId, channelMembersToSet, channelMembersToRemove)
                .include(MemberInclude.builder()
                        .includeCustom(true)
                        .includeStatus(true)
                        .includeType(true)
                        .includeTotalCount(true)
                        .includeUser(true)
                        .includeUserCustom(true)
                        .includeUserStatus(true)
                        .includeUserType(true)
                        .build())
                .sync();
        createdMembersList.add(new PNSetChannelMembersResult(manageChannelMembersResult));



        //then
        assertEquals(HttpStatus.SC_OK, manageChannelMembersResult.getStatus());
        assertEquals(1, manageChannelMembersResult.getData().size());
        PNMembers pnMembers = manageChannelMembersResult.getData().get(0);
        assertEquals(TEST_UUID2, pnMembers.getUuid().getId());
        assertEquals(STATUS_02, pnMembers.getStatus().getValue());
        assertEquals(TYPE_01, pnMembers.getType().getValue());


        assertThat(manageChannelMembersResult, allOf(
                notNullValue(),
                hasProperty("status", is(HttpStatus.SC_OK)),
                hasProperty("data", allOf(
                        hasItem(hasProperty("uuid",
                                hasProperty("id", is(channelMembersToSet.get(0).getUserId())))),
                        not(hasItem(hasProperty("uuid",
                                hasProperty("id", is(channelMembersToRemove.get(0).getUserId())))))))));
    }

    @Test
    public void manageChannelMembersHappyPathDeprecated() throws PubNubException {
        //given
        final List<PNUUID> channelMembersToRemove = Collections.singletonList(
                PNUUID.uuidWithCustom(TEST_UUID1, customChannelMembershipObject(), STATUS_01));

        final PNSetChannelMembersResult setChannelMembersResult = pubNubUnderTest.setChannelMembers()
                .channel(testChannelId)
                .uuids(channelMembersToRemove)
                .sync();
        createdMembersList.add(setChannelMembersResult);

        final List<PNUUID> channelMembersToSet = Collections.singletonList(
                PNUUID.uuidWithCustom(TEST_UUID2, customChannelMembershipObject()));

        //when
        final PNManageChannelMembersResult manageChannelMembersResult = pubNubUnderTest.manageChannelMembers()
                .channel(testChannelId)
                .set(channelMembersToSet)
                .remove(channelMembersToRemove)
                .includeTotalCount(true)
                .includeCustom(true)
                .includeUUID(UUID_WITH_CUSTOM)
                .sync();
        createdMembersList.add(new PNSetChannelMembersResult(manageChannelMembersResult));

        //then
        assertThat(manageChannelMembersResult, allOf(
                notNullValue(),
                hasProperty("status", is(HttpStatus.SC_OK)),
                hasProperty("data", allOf(
                        hasItem(hasProperty("uuid",
                                hasProperty("id", is(channelMembersToSet.get(0).getUuid().getId())))),
                        not(hasItem(hasProperty("uuid",
                                hasProperty("id", is(channelMembersToRemove.get(0).getUuid().getId())))))))));
    }

    @After
    public void cleanUp() {
        for (final PNSetChannelMembersResult createdMembers : createdMembersList) {
            try {
                final List<PNUUID> channelMembers = new ArrayList<>();
                for (final PNMembers it : createdMembers.getData()) {
                    final String id = it.getUuid().getId();
                    final PNUUID pnUUIDs = PNUUID.uuid(id);
                    channelMembers.add(pnUUIDs);
                }

                pubNubUnderTest.removeChannelMembers()
                        .channel(testChannelId)
                        .uuids(channelMembers)
                        .sync();

            } catch (Exception e) {
                LOG.warn("Could not cleanup {}", createdMembers, e);
            }
        }
    }

    private Map<String, Object> customChannelMembershipObject() {
        final Map<String, Object> customMap = new HashMap<>();
        customMap.putIfAbsent("members_param1", "val1");
        customMap.putIfAbsent("members_param2", "val2");
        return customMap;
    }

    private PNMembers getChannelMemberByUserId(PNSetChannelMembersResult setChannelMembersResult, String userId) {
        return setChannelMembersResult.getData().stream().filter(member -> userId.equals(member.getUuid().getId())).collect(Collectors.toList()).get(0);
    }

    private PNMembers getChannelMemberByUserId(PNGetChannelMembersResult setChannelMembersResult, String userId) {
        return setChannelMembersResult.getData().stream().filter(member -> userId.equals(member.getUuid().getId())).collect(Collectors.toList()).get(0);
    }
}
