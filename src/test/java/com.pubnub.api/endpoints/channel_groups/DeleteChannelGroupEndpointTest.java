package com.pubnub.api.endpoints.channel_groups;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.pubnub.api.core.PubnubException;
import com.pubnub.api.core.models.consumer_facing.PNChannelGroupsAllChannelsResult;
import com.pubnub.api.core.models.consumer_facing.PNChannelGroupsListAllResult;
import com.pubnub.api.endpoints.TestHarness;
import org.junit.Before;
import org.junit.Rule;

import java.io.IOException;
import java.util.Arrays;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertThat;

public class DeleteChannelGroupEndpointTest extends TestHarness {
    private DeleteChannelGroup partialDeleteChannelGroup;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    @Before
    public void beforeEach() throws IOException {
        partialDeleteChannelGroup = this.createPubNubInstance(8080).deleteChannelGroup();
    }

    @org.junit.Test
    public void testSyncSuccess() throws IOException, PubnubException, InterruptedException {
        stubFor(get(urlPathEqualTo("/v2/channel-registration/sub-key/mySubscribeKey/channel-group/groupA/remove"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"message\": \"OK\", \"payload\": {}, \"service\": \"ChannelGroups\"}")));

        boolean response = partialDeleteChannelGroup.group("groupA").sync();
        assertThat(response, org.hamcrest.Matchers.equalTo(true));
    }

    @org.junit.Test
    public void testSyncSuccessCustomUUID() throws IOException, PubnubException, InterruptedException {
        stubFor(get(urlPathEqualTo("/v2/channel-registration/sub-key/mySubscribeKey/channel-group/groupA/remove"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"message\": \"OK\", \"payload\": {}, \"service\": \"ChannelGroups\"}")));

        boolean response = partialDeleteChannelGroup.group("groupA").uuid("myCustomUUID").sync();
        assertThat(response, org.hamcrest.Matchers.equalTo(true));
    }

}
