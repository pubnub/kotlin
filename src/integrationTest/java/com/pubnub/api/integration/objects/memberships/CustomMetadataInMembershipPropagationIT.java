package com.pubnub.api.integration.objects.memberships;

import com.pubnub.api.PubNubException;
import com.pubnub.api.integration.objects.ObjectsApiBaseIT;
import com.pubnub.api.models.consumer.objects_api.channel.PNSetChannelMetadataResult;
import com.pubnub.api.models.consumer.objects_api.membership.PNChannelMembership;
import com.pubnub.api.models.consumer.objects_api.membership.PNGetMembershipsResult;
import com.pubnub.api.models.consumer.objects_api.membership.PNSetMembershipResult;
import org.junit.After;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.pubnub.api.endpoints.objects_api.utils.Include.PNChannelDetailsLevel.CHANNEL;
import static com.pubnub.api.endpoints.objects_api.utils.Include.PNChannelDetailsLevel.CHANNEL_WITH_CUSTOM;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class CustomMetadataInMembershipPropagationIT extends ObjectsApiBaseIT {
    private final String testChannelMetadataId = UUID.randomUUID().toString();
    private final Map<String, Object> testCustomObjectForChannelMetadata = new HashMap() {{
        put("key1", "val1");
        put("key2", "val2");
    }};

    private final Map<String, Object> testCustomObjectForMembership = new HashMap() {{
        put("key3", "val3");
        put("key4", "val4");
    }};
    private PNSetChannelMetadataResult setChannelMetadataResult;
    private PNSetMembershipResult setMembershipResult;


    @Test
    public void setMembershipCustomHappyPath() throws PubNubException {
        final String testChannelName = "The Name of the Channel";
        final String testDescription = "Some interesting channel description";
        setChannelMetadataResult = pubNubUnderTest.setChannelMetadata()
                .channel(testChannelMetadataId)
                .name(testChannelName)
                .description(testDescription)
                .custom(testCustomObjectForChannelMetadata)
                .includeCustom(true)
                .sync();

        setMembershipResult = pubNubUnderTest.setMemberships()
                .channelMemberships(Collections.singletonList(
                        PNChannelMembership.channelWithCustom(setChannelMetadataResult.getData().getId(),
                                testCustomObjectForMembership)))
                .includeCustom(true)
                .includeChannel(CHANNEL_WITH_CUSTOM)
                .sync();

        final PNGetMembershipsResult getMembershipsResult = pubNubUnderTest.getMemberships()
                .includeCustom(false)
                .includeChannel(CHANNEL)
                .sync();

        assertThat(setChannelMetadataResult,
                hasProperty("data", hasProperty("custom", notNullValue())));
        assertThat(setMembershipResult, hasProperty("data",
                hasItem(
                        allOf(
                                hasProperty("custom", notNullValue()),
                                hasProperty("channel", allOf(
                                        allOf(
                                                hasProperty("id", is(testChannelMetadataId)),
                                                hasProperty("name", is(testChannelName)),
                                                hasProperty("description", is(testDescription)),
                                                hasProperty("custom", notNullValue()))))))));
        assertThat(getMembershipsResult, hasProperty("data",
                hasItem(
                        allOf(
                                hasProperty("custom", nullValue()),
                                hasProperty("channel", allOf(
                                        allOf(
                                                hasProperty("id", is(testChannelMetadataId)),
                                                hasProperty("name", is(testChannelName)),
                                                hasProperty("description", is(testDescription)),
                                                hasProperty("custom", nullValue()))))))));
    }

    @After
    public void cleanUp() {
        try {
            if (setChannelMetadataResult != null) {
                if (setMembershipResult != null) {
                    pubNubUnderTest.removeMemberships()
                            .channelMemberships(Collections.singletonList(PNChannelMembership.channel(setChannelMetadataResult.getData().getId())))
                            .sync();
                }

                pubNubUnderTest.removeChannelMetadata()
                        .channel(setChannelMetadataResult.getData().getId())
                        .sync();
            }
        } catch (PubNubException e) {
            e.printStackTrace();
        }
    }
}
