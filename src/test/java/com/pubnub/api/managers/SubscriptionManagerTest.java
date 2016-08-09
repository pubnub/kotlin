package com.pubnub.api.managers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.http.QueryParameter;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.jayway.awaitility.Awaitility;
import com.pubnub.api.PubNubUtil;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.PubNub;
import com.pubnub.api.enums.PNHeartbeatNotificationOptions;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.presence.PNSetStateResult;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.endpoints.TestHarness;
import org.junit.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SubscriptionManagerTest extends TestHarness {

    private PubNub pubnub;

    @Before
    public void beforeEach() throws IOException {
        pubnub = this.createPubNubInstance(8080);
    }

    @After
    public void afterEach() {
        pubnub.stop();
    }

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    @Test
    public void testGetSubscribedChannels() {
        stubFor(get(urlPathEqualTo("/v2/subscribe/mySubscribeKey/ch2,ch1/0"))
                .willReturn(aResponse().withBody("{\"t\":{\"t\":\"14607577960932487\",\"r\":1},\"m\":[{\"a\":\"4\",\"f\":0,\"i\":\"Client-g5d4g\",\"p\":{\"t\":\"14607577960925503\",\"r\":1},\"k\":\"sub-c-4cec9f8e-01fa-11e6-8180-0619f8945a4f\",\"c\":\"coolChannel\",\"d\":{\"text\":\"Message\"},\"b\":\"coolChan-bnel\"}]}")));

        pubnub.subscribe().channels(Arrays.asList("ch1", "ch2")).execute();

        List<String> channels = pubnub.getSubscribedChannels();

        assertEquals(2, channels.size());
    }

    @Test
    public void testGetSubscribedChannelGroups() {
        stubFor(get(urlPathEqualTo("/v2/subscribe/mySubscribeKey/,/0"))
                .willReturn(aResponse().withBody("{\"t\":{\"t\":\"14607577960932487\",\"r\":1},\"m\":[{\"a\":\"4\",\"f\":0,\"i\":\"Client-g5d4g\",\"p\":{\"t\":\"14607577960925503\",\"r\":1},\"k\":\"sub-c-4cec9f8e-01fa-11e6-8180-0619f8945a4f\",\"c\":\"coolChannel\",\"d\":{\"text\":\"Enter Message Here\"},\"b\":\"coolChan-bnel\"}]}")));

        pubnub.subscribe().channelGroups(Arrays.asList("cg1", "cg2")).execute();

        List<String> groups = pubnub.getSubscribedChannelGroups();

        assertEquals(2, groups.size());
    }

    @Test
    public void testPubNubUnsubscribeAll() {

        pubnub.subscribe().channels(Arrays.asList("ch1", "ch2"))
                .channelGroups(Arrays.asList("grp1","grp2"))
                .withPresence()
                .execute();

        List<String> channels = pubnub.getSubscribedChannels();
        assertEquals(2, channels.size());

        List<String> groups = pubnub.getSubscribedChannelGroups();
        assertEquals(2, groups.size());

        pubnub.unsubscribeAll();

        channels = pubnub.getSubscribedChannels();
        assertEquals(0, channels.size());

        groups = pubnub.getSubscribedChannelGroups();
        assertEquals(0, groups.size());
    }

    @Test
    public void testSubscribeBuilder() {
        final AtomicInteger gotStatus = new AtomicInteger();
        final AtomicBoolean gotMessage = new AtomicBoolean();
        stubFor(get(urlPathEqualTo("/v2/subscribe/mySubscribeKey/ch2,ch1/0"))
                .willReturn(aResponse().withBody("{\"t\":{\"t\":\"14607577960932487\",\"r\":1},\"m\":[{\"a\":\"4\",\"f\":0,\"i\":\"Client-g5d4g\",\"p\":{\"t\":\"14607577960925503\",\"r\":1},\"k\":\"sub-c-4cec9f8e-01fa-11e6-8180-0619f8945a4f\",\"c\":\"coolChannel\",\"d\":{\"text\":\"Message\"},\"b\":\"coolChan-bnel\"}]}")));

        pubnub.addListener(new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {

                if (status.getCategory() == PNStatusCategory.PNConnectedCategory) {
                    gotStatus.addAndGet(1);
                }

            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/v2/subscribe.*")));

                assertEquals("Message", message.getMessage().get("text").asText());
                gotMessage.set(true);
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {
            }
        });


        pubnub.subscribe().channels(Arrays.asList("ch1", "ch2")).execute();

        Awaitility.await().atMost(2, TimeUnit.SECONDS).untilAtomic(gotMessage, org.hamcrest.core.IsEqual.equalTo(true));
        Awaitility.await().atMost(2, TimeUnit.SECONDS).untilAtomic(gotStatus, org.hamcrest.core.IsEqual.equalTo(1));

    }

    @Test
    public void testSubscribeSlidingBuilder() {
        final AtomicBoolean gotMessage1 = new AtomicBoolean();
        final AtomicBoolean gotMessage2 = new AtomicBoolean();
        final AtomicBoolean gotMessage3 = new AtomicBoolean();

        stubFor(get(urlPathEqualTo("/v2/subscribe/mySubscribeKey/ch2,ch1/0"))
                .withQueryParam("tt", matching("0"))
                .willReturn(aResponse().withBody("{\"t\":{\"t\":\"3\",\"r\":1},\"m\":[{\"a\":\"4\",\"f\":0,\"i\":\"Client-g5d4g\",\"p\":{\"t\":\"14607577960925503\",\"r\":1},\"k\":\"sub-c-4cec9f8e-01fa-11e6-8180-0619f8945a4f\",\"c\":\"coolChannel\",\"d\":{\"text\":\"Message\"},\"b\":\"coolChan-bnel\"}]}")));

        stubFor(get(urlPathEqualTo("/v2/subscribe/mySubscribeKey/ch2,ch1/0"))
                .withQueryParam("tt", matching("3"))
                .willReturn(aResponse().withBody("{\"t\":{\"t\":\"10\",\"r\":1},\"m\":[{\"a\":\"4\",\"f\":0,\"i\":\"Client-g5d4g\",\"p\":{\"t\":\"14607577960925503\",\"r\":1},\"k\":\"sub-c-4cec9f8e-01fa-11e6-8180-0619f8945a4f\",\"c\":\"coolChannel\",\"d\":{\"text\":\"Message3\"},\"b\":\"coolChan-bnel\"}]}")));

        stubFor(get(urlPathEqualTo("/v2/subscribe/mySubscribeKey/ch2,ch1/0"))
                .withQueryParam("tt", matching("10"))
                .willReturn(aResponse().withBody("{\"t\":{\"t\":\"20\",\"r\":1},\"m\":[{\"a\":\"4\",\"f\":0,\"i\":\"Client-g5d4g\",\"p\":{\"t\":\"14607577960925503\",\"r\":1},\"k\":\"sub-c-4cec9f8e-01fa-11e6-8180-0619f8945a4f\",\"c\":\"coolChannel\",\"d\":{\"text\":\"Message10\"},\"b\":\"coolChan-bnel\"}]}")));

        pubnub.addListener(new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {

            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/v2/subscribe.*")));

                if (message.getMessage().get("text").asText().equals("Message")) {
                    gotMessage1.set(true);
                } else if (message.getMessage().get("text").asText().equals("Message3")) {
                    gotMessage2.set(true);
                } else if (message.getMessage().get("text").asText().equals("Message10")) {
                    gotMessage3.set(true);
                }
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {
            }
        });


        pubnub.subscribe().channels(Arrays.asList("ch1", "ch2")).execute();

        Awaitility.await().atMost(2, TimeUnit.SECONDS).untilAtomic(gotMessage1, org.hamcrest.core.IsEqual.equalTo(true));
        Awaitility.await().atMost(2, TimeUnit.SECONDS).untilAtomic(gotMessage2, org.hamcrest.core.IsEqual.equalTo(true));
        Awaitility.await().atMost(2, TimeUnit.SECONDS).untilAtomic(gotMessage3, org.hamcrest.core.IsEqual.equalTo(true));
    }

    @Test
    public void testSubscribeBuilderNumber() {
        final AtomicInteger atomic = new AtomicInteger(0);
        stubFor(get(urlPathEqualTo("/v2/subscribe/mySubscribeKey/ch2,ch1/0"))
                .willReturn(aResponse().withBody("{\"t\":{\"t\":\"14607577960932487\",\"r\":1},\"m\":[{\"a\":\"4\",\"f\":0,\"i\":\"Client-g5d4g\",\"p\":{\"t\":\"14607577960925503\",\"r\":1},\"k\":\"sub-c-4cec9f8e-01fa-11e6-8180-0619f8945a4f\",\"c\":\"coolChannel\",\"d\": 10,\"b\":\"coolChan-bnel\"}]}")));

        pubnub.addListener(new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {
            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/v2/subscribe.*")));

                assertEquals(10, message.getMessage().asInt());
                atomic.addAndGet(1);
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {
            }
        });


        pubnub.subscribe().channels(Arrays.asList("ch1", "ch2")).execute();

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAtomic(atomic, org.hamcrest.Matchers.greaterThan(0));

    }

    @Test
    public void testSubscribeBuilderWithState() throws IOException {
        final AtomicInteger atomic = new AtomicInteger(0);

        final ObjectMapper mapper = new ObjectMapper();
        final String expectedPayload = PubNubUtil.urlDecode("%7B%22ch1%22%3A%5B%22p1%22%2C%22p2%22%5D%2C%22cg2%22%3A%5B%22p1%22%2C%22p2%22%5D%7D");
        final Map<String, Object> expectedMap = mapper.readValue(expectedPayload, Map.class);

        stubFor(get(urlPathEqualTo("/v2/subscribe/mySubscribeKey/ch2,ch1/0"))
                .willReturn(aResponse().withBody("{\"t\":{\"t\":\"14607577960932487\",\"r\":1},\"m\":[{\"a\":\"4\",\"f\":0,\"i\":\"Client-g5d4g\",\"p\":{\"t\":\"14607577960925503\",\"r\":1},\"k\":\"sub-c-4cec9f8e-01fa-11e6-8180-0619f8945a4f\",\"c\":\"coolChannel\",\"d\":{\"text\":\"Enter Message Here\"},\"b\":\"coolChan-bnel\"}]}")));

        pubnub.getConfiguration().setHeartbeatNotificationOptions(PNHeartbeatNotificationOptions.ALL);
        pubnub.addListener(new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {

                List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching(
                        "/v2/presence/sub-key/" + pubnub.getConfiguration().getSubscribeKey() + "/channel/ch2,ch1/heartbeat.*")));

                for (LoggedRequest request: requests) {
                    String stateString = PubNubUtil.urlDecode(request.queryParameter("state").firstValue());
                    Map<String, Object> actualMap = null;
                    try {
                        actualMap = mapper.readValue(stateString, Map.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (actualMap != null && actualMap.equals(expectedMap)) {
                        atomic.getAndAdd(1);
                    }
                }
            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {
            }
        });


        pubnub.subscribe().channels(Arrays.asList("ch1", "ch2")).channelGroups(Arrays.asList("cg1", "cg2")).execute();
        pubnub.setPresenceState().channels(Arrays.asList("ch1")).channelGroups(Arrays.asList("cg2"))
                .state(Arrays.asList("p1", "p2"))
                .async(new PNCallback<PNSetStateResult>() {
                    @Override
                    public void onResponse(PNSetStateResult result, PNStatus status) {}
                });

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAtomic(atomic, org.hamcrest.Matchers.greaterThan(0));

    }

    @Test
    public void testSubscribeChannelGroupBuilder() {
        final AtomicInteger atomic = new AtomicInteger(0);
        stubFor(get(urlPathEqualTo("/v2/subscribe/mySubscribeKey/,/0"))
                .willReturn(aResponse().withBody("{\"t\":{\"t\":\"14607577960932487\",\"r\":1},\"m\":[{\"a\":\"4\",\"f\":0,\"i\":\"Client-g5d4g\",\"p\":{\"t\":\"14607577960925503\",\"r\":1},\"k\":\"sub-c-4cec9f8e-01fa-11e6-8180-0619f8945a4f\",\"c\":\"coolChannel\",\"d\":{\"text\":\"Enter Message Here\"},\"b\":\"coolChan-bnel\"}]}")));

        pubnub.addListener(new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {
            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/v2/subscribe.*")));

                for (LoggedRequest request: requests) {
                    QueryParameter channelGroupQuery = request.queryParameter("channel-group");
                    if (channelGroupQuery != null && channelGroupQuery.firstValue().equals("cg1,cg2")) {
                        atomic.addAndGet(1);
                    }
                }

            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {
            }
        });


        pubnub.subscribe().channelGroups(Arrays.asList("cg1", "cg2")).execute();

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAtomic(atomic, org.hamcrest.Matchers.greaterThan(0));

    }

    @Test
    public void testSubscribeChannelGroupWithPresenceBuilder() {
        final AtomicInteger atomic = new AtomicInteger(0);
        stubFor(get(urlPathEqualTo("/v2/subscribe/mySubscribeKey/,/0"))
                .willReturn(aResponse().withBody("{\"t\":{\"t\":\"14607577960932487\",\"r\":1},\"m\":[{\"a\":\"4\",\"f\":0,\"i\":\"Client-g5d4g\",\"p\":{\"t\":\"14607577960925503\",\"r\":1},\"k\":\"sub-c-4cec9f8e-01fa-11e6-8180-0619f8945a4f\",\"c\":\"coolChannel\",\"d\":{\"text\":\"Enter Message Here\"},\"b\":\"coolChan-bnel\"}]}")));

        pubnub.addListener(new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {
            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/v2/subscribe.*")));

                for (LoggedRequest request: requests) {
                    String[] channelGroups = request.queryParameter("channel-group").firstValue().split(",");
                    Arrays.sort(channelGroups);
                    if ("cg1,cg1-pnpres,cg2,cg2-pnpres".equals(joinArray(channelGroups))) {
                        atomic.addAndGet(1);
                    }

                }

            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {
            }
        });


        pubnub.subscribe().channelGroups(Arrays.asList("cg1", "cg2")).withPresence().execute();

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAtomic(atomic, org.hamcrest.Matchers.greaterThan(0));

    }

    @Test
    public void testSubscribeWithTimeTokenBuilder() {
        final AtomicInteger atomic = new AtomicInteger(0);
        stubFor(get(urlPathEqualTo("/v2/subscribe/mySubscribeKey/ch2,ch1/0"))
                .willReturn(aResponse().withBody("{\"t\":{\"t\":\"14607577960932487\",\"r\":1},\"m\":[{\"a\":\"4\",\"f\":0,\"i\":\"Client-g5d4g\",\"p\":{\"t\":\"14607577960925503\",\"r\":1},\"k\":\"sub-c-4cec9f8e-01fa-11e6-8180-0619f8945a4f\",\"c\":\"coolChannel\",\"d\":{\"text\":\"Enter Message Here\"},\"b\":\"coolChan-bnel\"}]}")));

        pubnub.addListener(new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {
            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/v2/subscribe.*")));

                assertEquals("1337", requests.get(0).queryParameter("tt").firstValue());
                atomic.addAndGet(1);
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {
            }
        });


        pubnub.subscribe().channels(Arrays.asList("ch1", "ch2")).withTimetoken(1337L).execute();

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAtomic(atomic, org.hamcrest.Matchers.greaterThan(0));

    }

    @Test
    public void testSubscribeWithFilterExpressionBuilder() {
        final AtomicInteger atomic = new AtomicInteger(0);
        pubnub.getConfiguration().setFilterExpression("much=filtering");
        stubFor(get(urlPathEqualTo("/v2/subscribe/mySubscribeKey/ch2,ch1/0"))
                .withQueryParam("uuid", matching("myUUID"))
                .withQueryParam("pnsdk", matching("PubNub-Java-Unified/suchJava"))
                .withQueryParam("filter-expr", matching("much%3Dfiltering"))
                .withQueryParam("tt", matching("0"))
                .willReturn(aResponse().withBody("{\"t\":{\"t\":\"14607577960932487\",\"r\":1},\"m\":[{\"a\":\"4\",\"f\":0,\"i\":\"Client-g5d4g\",\"p\":{\"t\":\"14607577960925503\",\"r\":1},\"k\":\"sub-c-4cec9f8e-01fa-11e6-8180-0619f8945a4f\",\"c\":\"coolChannel\",\"d\":{\"text\":\"Enter Message Here\"},\"b\":\"coolChan-bnel\"}]}")));

        pubnub.addListener(new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {
            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/v2/subscribe.*")));
                assertTrue(requests.size() > 0);
                atomic.addAndGet(1);
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {
            }
        });


        pubnub.subscribe().channels(Arrays.asList("ch1", "ch2")).execute();

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAtomic(atomic, org.hamcrest.Matchers.greaterThan(0));

    }

    @Test
    public void testSubscribePresenceBuilder() {
        final AtomicInteger atomic = new AtomicInteger(0);
        stubFor(get(urlPathEqualTo("/v2/subscribe/mySubscribeKey/ch2,ch1,ch2-pnpres,ch1-pnpres/0"))
                .willReturn(aResponse().withBody("{\"t\":{\"t\":\"14607577960932487\",\"r\":1},\"m\":[{\"a\":\"4\",\"f\":0,\"i\":\"Client-g5d4g\",\"p\":{\"t\":\"14607577960925503\",\"r\":1},\"k\":\"sub-c-4cec9f8e-01fa-11e6-8180-0619f8945a4f\",\"c\":\"coolChannel\",\"d\":{\"text\":\"Enter Message Here\"},\"b\":\"coolChan-bnel\"}]}")));

        pubnub.addListener(new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {
                int moose = 10;
            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/v2/subscribe.*")));

                Assert.assertEquals("{\"text\":\"Enter Message Here\"}", message.getMessage().toString());
                atomic.addAndGet(1);
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {
            }
        });

        pubnub.subscribe().channels(Arrays.asList("ch1", "ch2")).withPresence().execute();

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAtomic(atomic, org.hamcrest.Matchers.greaterThan(0));

    }

    @Test
    public void testSubscribePresencePayloadBuilder() {
        final AtomicInteger atomic = new AtomicInteger(0);
        stubFor(get(urlPathEqualTo("/v2/subscribe/mySubscribeKey/ch2,ch1,ch2-pnpres,ch1-pnpres/0"))
                .willReturn(aResponse().withBody("{\"t\":{\"t\":\"14614512228786519\",\"r\":1},\"m\":[{\"a\":\"4\",\"f\":0,\"p\":{\"t\":\"14614512228418349\",\"r\":2},\"k\":\"sub-c-4cec9f8e-01fa-11e6-8180-0619f8945a4f\",\"c\":\"coolChannel-pnpres\",\"d\":{\"action\": \"join\", \"timestamp\": 1461451222, \"uuid\": \"4a6d5df7-e301-4e73-a7b7-6af9ab484eb0\", \"occupancy\": 1},\"b\":\"coolChannel-pnpres\"}]}\n")));

        pubnub.addListener(new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {
            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {
                if (atomic.get() == 0) {
                    assertEquals("join", presence.getEvent());
                    assertEquals("4a6d5df7-e301-4e73-a7b7-6af9ab484eb0", presence.getUuid());
                    Assert.assertTrue(presence.getOccupancy().equals(1));
                    Assert.assertTrue(presence.getTimestamp().equals(1461451222L));
                    atomic.incrementAndGet();
                }
            }
        });

        pubnub.subscribe().channels(Arrays.asList("ch1", "ch2")).withPresence().execute();

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAtomic(atomic, org.hamcrest.core.IsEqual.equalTo(1));

    }

    @Test
    public void testSubscribePresenceStateCallback() {
        final AtomicBoolean atomic = new AtomicBoolean();
        stubFor(get(urlPathEqualTo("/v2/subscribe/mySubscribeKey/ch10,ch10-pnpres/0"))
                .willReturn(aResponse().withBody("{\"t\":{\"t\":\"14637536741734954\",\"r\":1},\"m\":[{\"a\":\"4\",\"f\":512,\"p\":{\"t\":\"14637536740940378\",\"r\":1},\"k\":\"demo-36\",\"c\":\"ch10-pnpres\",\"d\":{\"action\": \"join\", \"timestamp\": 1463753674, \"uuid\": \"24c9bb19-1fcd-4c40-a6f1-522a8a1329ef\", \"occupancy\": 3},\"b\":\"ch10-pnpres\"},{\"a\":\"4\",\"f\":512,\"p\":{\"t\":\"14637536741726901\",\"r\":1},\"k\":\"demo-36\",\"c\":\"ch10-pnpres\",\"d\":{\"action\": \"state-change\", \"timestamp\": 1463753674, \"data\": {\"state\": \"cool\"}, \"uuid\": \"24c9bb19-1fcd-4c40-a6f1-522a8a1329ef\", \"occupancy\": 3},\"b\":\"ch10-pnpres\"}]}")));

        pubnub.addListener(new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {
            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {
                if (presence.getEvent().equals("state-change")) {
                    if (presence.getState().has("state") && presence.getState().get("state").asText().equals("cool")) {
                        atomic.set(true);
                    }
                }
            }
        });

        pubnub.subscribe().channels(Arrays.asList("ch10")).withPresence().execute();

        Awaitility.await().atMost(2, TimeUnit.SECONDS).untilAtomic(atomic, org.hamcrest.core.IsEqual.equalTo(true));

    }

    @Test
    public void testSubscribeRegionBuilder() {
        final AtomicBoolean atomic = new AtomicBoolean();
        stubFor(get(urlPathEqualTo("/v2/subscribe/mySubscribeKey/ch2,ch1,ch2-pnpres,ch1-pnpres/0"))
                .willReturn(aResponse().withBody("{\"t\":{\"t\":\"14607577960932487\",\"r\":8},\"m\":[{\"a\":\"4\",\"f\":0,\"i\":\"Client-g5d4g\",\"p\":{\"t\":\"14607577960925503\",\"r\":1},\"k\":\"sub-c-4cec9f8e-01fa-11e6-8180-0619f8945a4f\",\"c\":\"coolChannel\",\"d\":{\"text\":\"Enter Message Here\"},\"b\":\"coolChan-bnel\"}]}")));

        pubnub.addListener(new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {
            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/v2/subscribe.*")));

                if (requests.size() > 1) {
                    assertEquals("8", requests.get(1).queryParameter("tr").firstValue());
                    atomic.set(true);
                }
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {
            }
        });

        pubnub.subscribe().channels(Arrays.asList("ch1", "ch2")).withPresence().execute();

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAtomic(atomic, org.hamcrest.core.IsEqual.equalTo(true));

    }

    @Test
    public void testRemoveListener() {

        final AtomicInteger atomic = new AtomicInteger(0);

        SubscribeCallback sub1 = new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {
                atomic.addAndGet(1);
            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                atomic.addAndGet(1);
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {
                atomic.addAndGet(1);
            }
        };

        pubnub.addListener(sub1);
        pubnub.removeListener(sub1);

        pubnub.subscribe().channels(Arrays.asList("ch1", "ch2")).withPresence().execute();

        Awaitility.await().atMost(2, TimeUnit.SECONDS)
                .untilAtomic(atomic, org.hamcrest.core.IsEqual.equalTo(0));

    }

    @Test
    public void testUnsubscribe() {

        final AtomicBoolean statusRecieved = new AtomicBoolean();
        final AtomicBoolean messageRecieved = new AtomicBoolean();

        stubFor(get(urlPathEqualTo("/v2/subscribe/mySubscribeKey/ch2,ch1,ch2-pnpres,ch1-pnpres/0"))
                .willReturn(aResponse().withBody("{\"t\":{\"t\":\"14607577960932487\",\"r\":1},\"m\":[{\"a\":\"4\",\"f\":0,\"i\":\"Client-g5d4g\",\"p\":{\"t\":\"14607577960925503\",\"r\":1},\"k\":\"sub-c-4cec9f8e-01fa-11e6-8180-0619f8945a4f\",\"c\":\"coolChannel\",\"d\":{\"text\":\"Enter Message Here\"},\"b\":\"coolChan-bnel\"}]}")));

        stubFor(get(urlPathEqualTo("/v2/subscribe/mySubscribeKey/ch2,ch2-pnpres/0"))
                .willReturn(aResponse().withBody("{\"t\":{\"t\":\"14607577960932487\",\"r\":1},\"m\":[{\"a\":\"4\",\"f\":0,\"i\":\"Client-g5d4g\",\"p\":{\"t\":\"14607577960925503\",\"r\":1},\"k\":\"sub-c-4cec9f8e-01fa-11e6-8180-0619f8945a4f\",\"c\":\"coolChannel\",\"d\":{\"text\":\"Enter Message Here\"},\"b\":\"coolChan-bnel\"}]}")));

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/ch1/leave"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"message\": \"OK\", \"service\": \"Presence\", \"action\": \"leave\"}")));

        SubscribeCallback sub1 = new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {

                if (status.getCategory() == PNStatusCategory.PNConnectedCategory) {
                    pubnub.unsubscribe().channels(Arrays.asList("ch1")).execute();
                }

                if (status.getAffectedChannels().size() == 1 && status.getOperation() == PNOperationType.PNUnsubscribeOperation){
                    if (status.getAffectedChannels().get(0).equals("ch1")) {
                        statusRecieved.set(true);
                    }
                }
            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/v2/subscribe/mySubscribeKey/ch2,ch2-pnpres/0.*")));

                if (!requests.isEmpty()) {
                    messageRecieved.set(true);
                }

            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {
            }
        };

        pubnub.addListener(sub1);

        pubnub.subscribe().channels(Arrays.asList("ch1", "ch2")).withPresence().execute();

        Awaitility.await().atMost(2, TimeUnit.SECONDS).untilAtomic(messageRecieved, org.hamcrest.core.IsEqual.equalTo(true));
        Awaitility.await().atMost(2, TimeUnit.SECONDS).untilAtomic(statusRecieved, org.hamcrest.core.IsEqual.equalTo(true));
    }

    @Test
    public void testAllHeartbeats() {

        final AtomicBoolean statusRecieved = new AtomicBoolean();
        pubnub.getConfiguration().setHeartbeatNotificationOptions(PNHeartbeatNotificationOptions.ALL);

        stubFor(get(urlPathEqualTo("/v2/subscribe/mySubscribeKey/ch2,ch1,ch2-pnpres,ch1-pnpres/0"))
                .willReturn(aResponse().withBody("{\"t\":{\"t\":\"14607577960932487\",\"r\":1},\"m\":[{\"a\":\"4\",\"f\":0,\"i\":\"Client-g5d4g\",\"p\":{\"t\":\"14607577960925503\",\"r\":1},\"k\":\"sub-c-4cec9f8e-01fa-11e6-8180-0619f8945a4f\",\"c\":\"coolChannel\",\"d\":{\"text\":\"Enter Message Here\"},\"b\":\"coolChan-bnel\"}]}")));

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/ch2,ch1/heartbeat"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"message\": \"OK\", \"service\": \"Presence\", \"action\": \"leave\"}")));

        SubscribeCallback sub1 = new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {
                if (status.getOperation() == PNOperationType.PNHeartbeatOperation && !status.isError()) {
                    statusRecieved.set(true);
                }
            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {
            }
        };

        pubnub.addListener(sub1);

        pubnub.subscribe().channels(Arrays.asList("ch1", "ch2")).withPresence().execute();


        Awaitility.await().atMost(2, TimeUnit.SECONDS).untilAtomic(statusRecieved, org.hamcrest.core.IsEqual.equalTo(true));
    }

    @Test
    public void testSuccessOnFailureVerbosityHeartbeats() {

        final AtomicBoolean statusRecieved = new AtomicBoolean();
        pubnub.getConfiguration().setHeartbeatNotificationOptions(PNHeartbeatNotificationOptions.FAILURES);

        stubFor(get(urlPathEqualTo("/v2/subscribe/mySubscribeKey/ch2,ch2-pnpres,ch1,ch1-pnpres/0"))
                .willReturn(aResponse().withBody("{\"t\":{\"t\":\"14607577960932487\",\"r\":1},\"m\":[{\"a\":\"4\",\"f\":0,\"i\":\"Client-g5d4g\",\"p\":{\"t\":\"14607577960925503\",\"r\":1},\"k\":\"sub-c-4cec9f8e-01fa-11e6-8180-0619f8945a4f\",\"c\":\"coolChannel\",\"d\":{\"text\":\"Enter Message Here\"},\"b\":\"coolChan-bnel\"}]}")));


        SubscribeCallback sub1 = new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {
                if (status.getOperation() == PNOperationType.PNHeartbeatOperation) {
                    statusRecieved.set(true);
                }
            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {
            }
        };

        pubnub.addListener(sub1);

        pubnub.subscribe().channels(Arrays.asList("ch1", "ch2")).withPresence().execute();


        Awaitility.await().atMost(2, TimeUnit.SECONDS).untilAtomic(statusRecieved, org.hamcrest.core.IsEqual.equalTo(true));
    }

    @Test
    public void testFailedHeartbeats() {

        final AtomicBoolean statusRecieved = new AtomicBoolean();
        pubnub.getConfiguration().setHeartbeatNotificationOptions(PNHeartbeatNotificationOptions.ALL);

        stubFor(get(urlPathEqualTo("/v2/subscribe/mySubscribeKey/ch2,ch2-pnpres,ch1,ch1-pnpres/0"))
                .willReturn(aResponse().withBody("{\"t\":{\"t\":\"14607577960932487\",\"r\":1},\"m\":[{\"a\":\"4\",\"f\":0,\"i\":\"Client-g5d4g\",\"p\":{\"t\":\"14607577960925503\",\"r\":1},\"k\":\"sub-c-4cec9f8e-01fa-11e6-8180-0619f8945a4f\",\"c\":\"coolChannel\",\"d\":{\"text\":\"Enter Message Here\"},\"b\":\"coolChan-bnel\"}]}")));

        SubscribeCallback sub1 = new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {
                if (status.getOperation() == PNOperationType.PNHeartbeatOperation && status.isError()) {
                    statusRecieved.set(true);
                }
            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {
            }
        };

        pubnub.addListener(sub1);

        pubnub.subscribe().channels(Arrays.asList("ch1", "ch2")).withPresence().execute();


        Awaitility.await().atMost(2, TimeUnit.SECONDS).untilAtomic(statusRecieved, org.hamcrest.core.IsEqual.equalTo(true));
    }

    @Test
    public void testSilencedHeartbeats() {

        final AtomicBoolean statusRecieved = new AtomicBoolean();
        pubnub.getConfiguration().setHeartbeatNotificationOptions(PNHeartbeatNotificationOptions.NONE);

        stubFor(get(urlPathEqualTo("/v2/subscribe/mySubscribeKey/ch2,ch2-pnpres,ch1,ch1-pnpres/0"))
                .willReturn(aResponse().withBody("{\"t\":{\"t\":\"14607577960932487\",\"r\":1},\"m\":[{\"a\":\"4\",\"f\":0,\"i\":\"Client-g5d4g\",\"p\":{\"t\":\"14607577960925503\",\"r\":1},\"k\":\"sub-c-4cec9f8e-01fa-11e6-8180-0619f8945a4f\",\"c\":\"coolChannel\",\"d\":{\"text\":\"Enter Message Here\"},\"b\":\"coolChan-bnel\"}]}")));

        SubscribeCallback sub1 = new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {
                if (status.getOperation() == PNOperationType.PNHeartbeatOperation) {
                    statusRecieved.set(true);
                }
            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {
            }
        };

        pubnub.addListener(sub1);

        pubnub.subscribe().channels(Arrays.asList("ch1", "ch2")).withPresence().execute();


        Awaitility.await().atMost(2, TimeUnit.SECONDS).untilAtomic(statusRecieved, org.hamcrest.core.IsEqual.equalTo(false));
    }

    @Test
    public void testFailedNoneHeartbeats() {

        final AtomicBoolean statusRecieved = new AtomicBoolean();
        pubnub.getConfiguration().setHeartbeatNotificationOptions(PNHeartbeatNotificationOptions.NONE);

        stubFor(get(urlPathEqualTo("/v2/subscribe/mySubscribeKey/ch2,ch2-pnpres,ch1,ch1-pnpres/0"))
                .willReturn(aResponse().withBody("{\"t\":{\"t\":\"14607577960932487\",\"r\":1},\"m\":[{\"a\":\"4\",\"f\":0,\"i\":\"Client-g5d4g\",\"p\":{\"t\":\"14607577960925503\",\"r\":1},\"k\":\"sub-c-4cec9f8e-01fa-11e6-8180-0619f8945a4f\",\"c\":\"coolChannel\",\"d\":{\"text\":\"Enter Message Here\"},\"b\":\"coolChan-bnel\"}]}")));

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/ch2,ch1/heartbeat"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"message\": \"OK\", \"service\": \"Presence\", \"action\": \"leave\"}")));

        SubscribeCallback sub1 = new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {
                if (status.getOperation() == PNOperationType.PNHeartbeatOperation) {
                    statusRecieved.set(true);
                }
            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {
            }
        };

        pubnub.addListener(sub1);

        pubnub.subscribe().channels(Arrays.asList("ch1", "ch2")).withPresence().execute();


        Awaitility.await().atMost(2, TimeUnit.SECONDS).untilAtomic(statusRecieved, org.hamcrest.core.IsEqual.equalTo(false));
    }

    @Test
    public void testUnsubscribeAll() {

        final AtomicBoolean statusRecieved = new AtomicBoolean();
        final AtomicBoolean messageRecieved = new AtomicBoolean();

        stubFor(get(urlPathEqualTo("/v2/subscribe/mySubscribeKey/ch2,ch1,ch2-pnpres,ch1-pnpres/0"))
                .willReturn(aResponse().withBody("{\"t\":{\"t\":\"14607577960932487\",\"r\":1},\"m\":[{\"a\":\"4\",\"f\":0,\"i\":\"Client-g5d4g\",\"p\":{\"t\":\"14607577960925503\",\"r\":1},\"k\":\"sub-c-4cec9f8e-01fa-11e6-8180-0619f8945a4f\",\"c\":\"coolChannel\",\"d\":{\"text\":\"Enter Message Here\"},\"b\":\"coolChan-bnel\"}]}")));

        stubFor(get(urlPathEqualTo("/v2/subscribe/mySubscribeKey/ch2,ch2-pnpres/0"))
                .willReturn(aResponse().withBody("{\"t\":{\"t\":\"14607577960932487\",\"r\":1},\"m\":[{\"a\":\"4\",\"f\":0,\"i\":\"Client-g5d4g\",\"p\":{\"t\":\"14607577960925503\",\"r\":1},\"k\":\"sub-c-4cec9f8e-01fa-11e6-8180-0619f8945a4f\",\"c\":\"coolChannel\",\"d\":{\"text\":\"Enter Message Here\"},\"b\":\"coolChan-bnel\"}]}")));

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/ch1/leave"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"message\": \"OK\", \"service\": \"Presence\", \"action\": \"leave\"}")));

        stubFor(get(urlPathEqualTo("/v2/presence/sub-key/mySubscribeKey/channel/ch2/leave"))
                .willReturn(aResponse().withBody("{\"status\": 200, \"message\": \"OK\", \"service\": \"Presence\", \"action\": \"leave\"}")));

        SubscribeCallback sub1 = new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {

                if (status.getCategory() == PNStatusCategory.PNConnectedCategory) {
                    pubnub.unsubscribe().channels(Arrays.asList("ch1")).execute();
                }

                if (status.getAffectedChannels()!=null && status.getAffectedChannels().size() == 1 && status.getOperation() == PNOperationType.PNUnsubscribeOperation){
                    if (status.getAffectedChannels().get(0).equals("ch1")) {
                        pubnub.unsubscribe().channels(Arrays.asList("ch2")).execute();
                    }
                }

                if (status.getAffectedChannels()!=null && status.getAffectedChannels().size() == 1 && status.getOperation() == PNOperationType.PNUnsubscribeOperation){
                    if (status.getAffectedChannels().get(0).equals("ch2")) {
                        statusRecieved.set(true);
                    }
                }
            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {
            }
        };

        pubnub.addListener(sub1);

        pubnub.subscribe().channels(Arrays.asList("ch1", "ch2")).withPresence().execute();

        Awaitility.await().atMost(4, TimeUnit.SECONDS).untilAtomic(statusRecieved, org.hamcrest.core.IsEqual.equalTo(true));
    }


    private String joinArray(String[] arr) {
        StringBuilder builder = new StringBuilder();
        for(String s : arr) {
            if (builder.length() != 0) {
                builder.append(",");
            }
            builder.append(s);
        }
        return builder.toString();
    }
}
