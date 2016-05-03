package com.pubnub.api.endpoints.channel_groups;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.pubnub.api.PubNubException;
import com.pubnub.api.endpoints.TestHarness;
import com.pubnub.api.models.consumer.channel_group.PNChannelGroupsAddChannelResult;
import org.junit.Before;
import org.junit.Rule;

import java.io.IOException;
import java.util.Arrays;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;

public class AddChannelChannelGroupEndpointTest extends TestHarness {
    private AddChannelChannelGroup partialAddChannelChannelGroup;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    @Before
    public void beforeEach() throws IOException {
        partialAddChannelChannelGroup = this.createPubNubInstance(8080).addChannelsToChannelGroup();
    }

    @org.junit.Test
    public void testSyncSuccess() throws IOException, PubNubException, InterruptedException {
        stubFor(get(urlPathEqualTo("/v1/channel-registration/sub-key/mySubscribeKey/channel-group/groupA"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"message\": \"OK\", \"payload\": {} , \"service\": \"ChannelGroups\"}")));

        boolean response = partialAddChannelChannelGroup.channelGroup("groupA").channels(Arrays.asList("ch1", "ch2")).sync();
        assertThat(response, org.hamcrest.Matchers.equalTo(true));
    }

//    @org.junit.Test(expected=PubNubException.class)
//    public void testSyncGroupMissing() throws IOException, PubNubException, InterruptedException {
//        stubFor(get(urlPathEqualTo("/v1/channel-registration/sub-key/mySubscribeKey/channel-group/groupA"))
//                .willReturn(aResponse().withBody("{\"status\": 200, \"message\": \"OK\", \"payload\": {} , \"service\": \"ChannelGroups\"}")));
//
//        boolean response = partialAddChannelChannelGroup.channels(Arrays.asList("ch1", "ch2")).sync();
//    }
//
//    @org.junit.Test(expected=PubNubException.class)
//    public void testSyncGroupIsEmpty() throws IOException, PubNubException, InterruptedException {
//        stubFor(get(urlPathEqualTo("/v1/channel-registration/sub-key/mySubscribeKey/channel-group/groupA"))
//                .willReturn(aResponse().withBody("{\"status\": 200, \"message\": \"OK\", \"payload\": {} , \"service\": \"ChannelGroups\"}")));
//
//        boolean response = partialAddChannelChannelGroup.channelGroup("").channels(Arrays.asList("ch1", "ch2")).sync();
//    }
//
//    @org.junit.Test(expected=PubNubException.class)
//    public void testSyncChannelMissing() throws IOException, PubNubException, InterruptedException {
//        stubFor(get(urlPathEqualTo("/v1/channel-registration/sub-key/mySubscribeKey/channel-group/groupA"))
//                .willReturn(aResponse().withBody("{\"status\": 200, \"message\": \"OK\", \"payload\": {} , \"service\": \"ChannelGroups\"}")));
//
//        boolean response = partialAddChannelChannelGroup.channelGroup("groupA").sync();
//    }

}
