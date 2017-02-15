package com.pubnub.api.endpoints.push;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.jayway.awaitility.Awaitility;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.enums.PNPushType;
import com.pubnub.api.endpoints.TestHarness;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.push.PNPushAddChannelResult;
import com.pubnub.api.models.consumer.push.PNPushRemoveAllChannelsResult;
import com.pubnub.api.models.consumer.push.PNPushRemoveChannelResult;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;

public class ModifyPushChannelsForDeviceTest extends TestHarness {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();

    private PubNub pubnub;
    private RemoveAllPushChannelsForDevice instance;
    private AddChannelsToPush instanceAdd;
    private RemoveChannelsFromPush instanceRemove;


    @Before
    public void beforeEach() throws IOException {
        pubnub = this.createPubNubInstance(8080);
        instance = pubnub.removeAllPushNotificationsFromDeviceWithPushToken();
        instanceAdd = pubnub.addPushNotificationsOnChannels();
        instanceRemove = pubnub.removePushNotificationsFromChannels();
        wireMockRule.start();
    }

    @Test
    public void testAppleSuccessSyncRemoveAll() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice/remove"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]")));

        instance.deviceId("niceDevice").pushType(PNPushType.APNS).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("apns", requests.get(0).queryParameter("type").firstValue());
    }

    @Test
    public void testGoogleSuccessSyncRemoveAll() throws PubNubException, InterruptedException {
        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice/remove"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]")));

        instance.deviceId("niceDevice").pushType(PNPushType.GCM).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("gcm", requests.get(0).queryParameter("type").firstValue());
    }

    @Test
    public void testMicrosoftSuccessSyncRemoveAll() throws PubNubException, InterruptedException {
        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice/remove"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]")));

        instance.deviceId("niceDevice").pushType(PNPushType.MPNS).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("mpns", requests.get(0).queryParameter("type").firstValue());
    }

    @Test
    public void testIsAuthRequiredSuccessRemoveAll() throws IOException, PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice/remove"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]")));

        pubnub.getConfiguration().setAuthKey("myKey");
        instance.deviceId("niceDevice").pushType(PNPushType.MPNS).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("myKey", requests.get(0).queryParameter("auth").firstValue());
    }

    @Test
    public void testOperationTypeSuccessRemoveAll() throws IOException, PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice/remove"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]")));

        final AtomicInteger atomic = new AtomicInteger(0);

        instance.deviceId("niceDevice").pushType(PNPushType.MPNS).async(new PNCallback<PNPushRemoveAllChannelsResult>() {
            @Override
            public void onResponse(PNPushRemoveAllChannelsResult result, PNStatus status) {
                if (status != null && status.getOperation()== null) {
                    atomic.incrementAndGet();
                }
            }
        });

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAtomic(atomic, org.hamcrest.core.IsEqual.equalTo(1));
    }


    @Test(expected=PubNubException.class)
    public void testNullSubscribeKeyRemoveAll() throws IOException, PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice/remove"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]")));

        pubnub.getConfiguration().setSubscribeKey(null);
        instance.deviceId("niceDevice").pushType(PNPushType.MPNS).sync();
    }

    @Test(expected=PubNubException.class)
    public void testEmptySubscribeKeyRemoveAll() throws IOException, PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice/remove"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]")));

        pubnub.getConfiguration().setSubscribeKey("");
        instance.deviceId("niceDevice").pushType(PNPushType.MPNS).sync();
    }

    @Test(expected=PubNubException.class)
    public void testNullPushTypeRemoveAll() throws IOException, PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice/remove"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]")));

        instance.deviceId("niceDevice").sync();
    }

    @Test(expected=PubNubException.class)
    public void testNullDeviceIdRemoveAll() throws IOException, PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice/remove"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]")));

        instance.pushType(PNPushType.MPNS).sync();
    }

    @Test(expected=PubNubException.class)
    public void testEmptyDeviceIdRemoveAll() throws IOException, PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice/remove"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]")));

        instance.deviceId("").pushType(PNPushType.MPNS).sync();
    }

    @Test
    public void testAddAppleSuccessSync() throws PubNubException, InterruptedException {
        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]")));

        instanceAdd.deviceId("niceDevice").pushType(PNPushType.APNS)
                .channels(Arrays.asList("ch1", "ch2", "ch3"))
                .sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("ch1,ch2,ch3", requests.get(0).queryParameter("add").firstValue());
        assertEquals("apns", requests.get(0).queryParameter("type").firstValue());

    }

    @Test
    public void testAddGoogleSuccessSync() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]")));

        instanceAdd.deviceId("niceDevice").pushType(PNPushType.GCM)
                .channels(Arrays.asList("ch1", "ch2", "ch3"))
                .sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("ch1,ch2,ch3", requests.get(0).queryParameter("add").firstValue());
        assertEquals("gcm", requests.get(0).queryParameter("type").firstValue());

    }

    @Test
    public void testAddMicrosoftSuccessSync() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]")));

        instanceAdd.deviceId("niceDevice").pushType(PNPushType.MPNS)
                .channels(Arrays.asList("ch1", "ch2", "ch3"))
                .sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("ch1,ch2,ch3", requests.get(0).queryParameter("add").firstValue());
        assertEquals("mpns", requests.get(0).queryParameter("type").firstValue());
    }

    @Test
    public void testIsAuthRequiredSuccessAdd() throws IOException, PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]")));

        pubnub.getConfiguration().setAuthKey("myKey");
        instanceAdd.deviceId("niceDevice").pushType(PNPushType.MPNS)
                .channels(Arrays.asList("ch1", "ch2", "ch3"))
                .sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("myKey", requests.get(0).queryParameter("auth").firstValue());
    }

    @Test
    public void testOperationTypeSuccessAdd() throws IOException, PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]")));

        final AtomicInteger atomic = new AtomicInteger(0);

        instanceAdd.deviceId("niceDevice").pushType(PNPushType.MPNS)
                .channels(Arrays.asList("ch1", "ch2", "ch3"))
                .async(new PNCallback<PNPushAddChannelResult>() {
                    @Override
                    public void onResponse(PNPushAddChannelResult result, PNStatus status) {
                        if (status != null && status.getOperation()== PNOperationType.PNPushNotificationEnabledChannelsOperation) {
                            atomic.incrementAndGet();
                        }
                    }
                });


        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAtomic(atomic, org.hamcrest.core.IsEqual.equalTo(1));
    }

    @Test(expected=PubNubException.class)
    public void testNullSubscribeKeyAdd() throws IOException, PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]")));

        pubnub.getConfiguration().setSubscribeKey(null);
        instanceAdd.deviceId("niceDevice").pushType(PNPushType.MPNS)
                .channels(Arrays.asList("ch1", "ch2", "ch3"))
                .sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("ch1,ch2,ch3", requests.get(0).queryParameter("add").firstValue());
        assertEquals("mpns", requests.get(0).queryParameter("type").firstValue());
    }

    @Test(expected=PubNubException.class)
    public void testEmptySubscribeKeyAdd() throws IOException, PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]")));

        pubnub.getConfiguration().setSubscribeKey("");
        instanceAdd.deviceId("niceDevice").pushType(PNPushType.MPNS)
                .channels(Arrays.asList("ch1", "ch2", "ch3"))
                .sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("ch1,ch2,ch3", requests.get(0).queryParameter("add").firstValue());
        assertEquals("mpns", requests.get(0).queryParameter("type").firstValue());
    }

    @Test(expected=PubNubException.class)
    public void testNullPushTypeAdd() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]")));

        instanceAdd.deviceId("niceDevice")
                .channels(Arrays.asList("ch1", "ch2", "ch3"))
                .sync();
    }

    @Test(expected=PubNubException.class)
    public void testNullDeviceIdAdd() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]")));

        instanceAdd.pushType(PNPushType.MPNS)
                .channels(Arrays.asList("ch1", "ch2", "ch3"))
                .sync();
    }

    @Test(expected=PubNubException.class)
    public void testEmptyDeviceIdAdd() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]")));

        instanceAdd.deviceId("").pushType(PNPushType.MPNS)
                .channels(Arrays.asList("ch1", "ch2", "ch3"))
                .sync();
    }

    @Test(expected=PubNubException.class)
    public void testMissingChannelsAdd() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]")));

        instanceAdd.deviceId("niceDevice").pushType(PNPushType.MPNS)
                .sync();
    }



    @Test
    public void testRemoveAppleSuccessSync() throws PubNubException, InterruptedException {
        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]")));

        instanceRemove.deviceId("niceDevice").pushType(PNPushType.APNS)
                .channels(Arrays.asList("chr1", "chr2", "chr3")).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("apns", requests.get(0).queryParameter("type").firstValue());
        assertEquals("chr1,chr2,chr3", requests.get(0).queryParameter("remove").firstValue());

    }

    @Test
    public void testGoogleSuccessSync() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]")));

        instanceRemove.deviceId("niceDevice").pushType(PNPushType.GCM)
                .channels(Arrays.asList("chr1", "chr2", "chr3")).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("gcm", requests.get(0).queryParameter("type").firstValue());
        assertEquals("chr1,chr2,chr3", requests.get(0).queryParameter("remove").firstValue());

    }

    @Test
    public void testMicrosoftSuccessSync() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]")));

        instanceRemove.deviceId("niceDevice").pushType(PNPushType.MPNS)
                .channels(Arrays.asList("chr1", "chr2", "chr3")).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("mpns", requests.get(0).queryParameter("type").firstValue());
        assertEquals("chr1,chr2,chr3", requests.get(0).queryParameter("remove").firstValue());
    }

    @Test
    public void testIsAuthRequiredSuccessRemove() throws IOException, PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]")));

        pubnub.getConfiguration().setAuthKey("myKey");
        instanceRemove.deviceId("niceDevice").pushType(PNPushType.MPNS)
                .channels(Arrays.asList("chr1", "chr2", "chr3")).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("myKey", requests.get(0).queryParameter("auth").firstValue());
    }

    @Test
    public void testOperationTypeSuccessRemove() throws IOException, PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]")));

        final AtomicInteger atomic = new AtomicInteger(0);

        instanceRemove.deviceId("niceDevice").pushType(PNPushType.MPNS)
                .channels(Arrays.asList("chr1", "chr2", "chr3")).async(new PNCallback<PNPushRemoveChannelResult>() {
            @Override
            public void onResponse(PNPushRemoveChannelResult result, PNStatus status) {
                if (status != null && status.getOperation()== PNOperationType.PNRemovePushNotificationsFromChannelsOperation) {
                    atomic.incrementAndGet();
                }
            }
        });

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .untilAtomic(atomic, org.hamcrest.core.IsEqual.equalTo(1));
    }

    @Test(expected=PubNubException.class)
    public void testNullSubscribeKeyRemove() throws IOException, PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]")));

        pubnub.getConfiguration().setSubscribeKey(null);
        instanceRemove.deviceId("niceDevice").pushType(PNPushType.MPNS)
                .channels(Arrays.asList("chr1", "chr2", "chr3")).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("myKey", requests.get(0).queryParameter("auth").firstValue());
    }

    @Test(expected=PubNubException.class)
    public void testEmptySubscribeKeyRemove() throws IOException, PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]")));

        pubnub.getConfiguration().setSubscribeKey("");
        instanceRemove.deviceId("niceDevice").pushType(PNPushType.MPNS)
                .channels(Arrays.asList("chr1", "chr2", "chr3")).sync();

        List<LoggedRequest> requests = findAll(getRequestedFor(urlMatching("/.*")));
        assertEquals(1, requests.size());
        assertEquals("myKey", requests.get(0).queryParameter("auth").firstValue());
    }

    @Test(expected=PubNubException.class)
    public void testNullPushType() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]")));

        instanceRemove.deviceId("niceDevice").channels(Arrays.asList("chr1", "chr2", "chr3")).sync();

    }

    @Test(expected=PubNubException.class)
    public void testNullDeviceId() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]")));

        instanceRemove.pushType(PNPushType.MPNS)
                .channels(Arrays.asList("chr1", "chr2", "chr3")).sync();

    }

    @Test(expected=PubNubException.class)
    public void testEmptyDeviceId() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]")));

        instanceRemove.deviceId("").pushType(PNPushType.MPNS)
                .channels(Arrays.asList("chr1", "chr2", "chr3")).sync();

    }

    @Test(expected=PubNubException.class)
    public void testMissingChannels() throws PubNubException, InterruptedException {

        stubFor(get(urlPathEqualTo("/v1/push/sub-key/mySubscribeKey/devices/niceDevice"))
                .willReturn(aResponse().withBody("[1, \"Modified Channels\"]")));

        instanceRemove.deviceId("niceDevice").pushType(PNPushType.MPNS).sync();

    }

}
