package com.pubnub.api.managers;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.jayway.awaitility.Awaitility;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.models.consumer_facing.PNMessageResult;
import com.pubnub.api.core.models.consumer_facing.PNPresenceEventResult;
import com.pubnub.api.core.models.consumer_facing.PNStatus;
import com.pubnub.api.endpoints.TestHarness;
import org.junit.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class SubscriptionManagerTest extends TestHarness {

    private Pubnub pubnub;
    private SubscriptionManager instance;

    @Before
    public void beforeEach() throws IOException {
        pubnub = this.createPubNubInstance(8080);
        instance = new SubscriptionManager(pubnub);
    }

    @After
    public void afterEach() {
        pubnub.stop();
    }

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    @Test
    public void testSubscribeBuilder() {
        final AtomicInteger atomic = new AtomicInteger(0);
        stubFor(get(urlPathEqualTo("/v2/subscribe/mySubscribeKey/ch2,ch1/0"))
                .willReturn(aResponse().withBody("{\"t\":{\"t\":\"14607577960932487\",\"r\":1},\"m\":[{\"a\":\"4\",\"f\":0,\"i\":\"Client-g5d4g\",\"p\":{\"t\":\"14607577960925503\",\"r\":1},\"k\":\"sub-c-4cec9f8e-01fa-11e6-8180-0619f8945a4f\",\"c\":\"coolChannel\",\"d\":{\"text\":\"Enter Message Here\"},\"b\":\"coolChan-bnel\"}]}")));

        instance.addListener(new SubscribeCallback() {
            @Override
            public void status(Pubnub pubnub, PNStatus status) {
            }

            @Override
            public void message(Pubnub pubnub, PNMessageResult message) {
                List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
                Assert.assertEquals(1, requests.size());
                atomic.addAndGet(1);
            }

            @Override
            public void presence(Pubnub pubnub, PNPresenceEventResult presence) {
            }
        });

        instance.adaptSubscribeBuilder(Arrays.asList("ch1", "ch2"), new ArrayList<String>(), false);

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAtomic(atomic, org.hamcrest.core.IsEqual.equalTo(1));

    }

    @Test
    public void testSubscribePresenceBuilder() {
        final AtomicInteger atomic = new AtomicInteger(0);
        stubFor(get(urlPathEqualTo("/v2/subscribe/mySubscribeKey/ch2,ch2-pnpres,ch1,ch1-pnpres/0"))
                .willReturn(aResponse().withBody("{\"t\":{\"t\":\"14607577960932487\",\"r\":1},\"m\":[{\"a\":\"4\",\"f\":0,\"i\":\"Client-g5d4g\",\"p\":{\"t\":\"14607577960925503\",\"r\":1},\"k\":\"sub-c-4cec9f8e-01fa-11e6-8180-0619f8945a4f\",\"c\":\"coolChannel\",\"d\":{\"text\":\"Enter Message Here\"},\"b\":\"coolChan-bnel\"}]}")));

        instance.addListener(new SubscribeCallback() {
            @Override
            public void status(Pubnub pubnub, PNStatus status) {
            }

            @Override
            public void message(Pubnub pubnub, PNMessageResult message) {
                List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/v2/subscribe.*")));
                Assert.assertEquals(1, requests.size());
                Assert.assertEquals("{text=Enter Message Here}", message.getData().getMessage().toString());
                atomic.addAndGet(1);
            }

            @Override
            public void presence(Pubnub pubnub, PNPresenceEventResult presence) {
            }
        });

        instance.adaptSubscribeBuilder(Arrays.asList("ch1", "ch2"), new ArrayList<String>(), true);

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAtomic(atomic, org.hamcrest.core.IsEqual.equalTo(1));

    }

}
