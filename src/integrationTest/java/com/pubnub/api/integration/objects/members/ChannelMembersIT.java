package com.pubnub.api.integration.objects.members;

import com.pubnub.api.PubNubException;
import com.pubnub.api.integration.objects.ObjectsApiBaseIT;
import com.pubnub.api.models.consumer.objects_api.member.*;
import com.pubnub.api.models.consumer.objects_api.uuid.PNUUIDMetadata;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.pubnub.api.endpoints.objects_api.utils.Include.PNUUIDDetailsLevel.UUID_WITH_CUSTOM;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class ChannelMembersIT extends ObjectsApiBaseIT {
    private final static Logger LOG = LoggerFactory.getLogger(ChannelMembersIT.class);

    private final List<PNSetChannelMembersResult> createdMembersList = new ArrayList<>();

    private final String TEST_UUID1 = UUID.randomUUID().toString();
    private final String TEST_UUID2 = UUID.randomUUID().toString();

    private final String testChannelId = UUID.randomUUID().toString();

    @Test
    public void addChannelMembersHappyPath() throws PubNubException {
        //given
        Map<String, Object> customMembershipObject = customChannelMembershipObject();
        final Collection<PNUUID> channelMembers = Arrays.asList(PNUUID.uuid(TEST_UUID1),
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
            final Object custom = it.getCustom();
            if (custom != null) {
                receivedCustomObjects.add(custom);
            }
        }

        assertThat(returnedUUIDs, containsInAnyOrder(expectedUUIDs.toArray()));
        assertThat(receivedCustomObjects, hasSize(1));
    }

    @Test
    public void getChannelMembersHappyPath() throws PubNubException {
        //given
        final Collection<PNUUID> channelMembers = Arrays.asList(PNUUID.uuid(TEST_UUID1),
                PNUUID.uuidWithCustom(TEST_UUID2, customChannelMembershipObject()));

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
            final Object custom = it.getCustom();
            if (custom != null) {
                receivedCustomObjects.add(custom);
            }
        }
        assertThat(returnedUUIDs, containsInAnyOrder(expectedUUIDs.toArray()));
        assertThat(receivedCustomObjects, hasSize(1));


    }

    @Test
    public void removeChannelMembersHappyPath() throws PubNubException {
        //given
        final Collection<PNUUID> channelMembers = Arrays.asList(PNUUID.uuid(TEST_UUID1),
                PNUUID.uuidWithCustom(TEST_UUID2, customChannelMembershipObject()));

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
        final List<PNUUID> channelMembersToRemove = Collections.singletonList(
                PNUUID.uuidWithCustom(TEST_UUID1, customChannelMembershipObject()));

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
}
