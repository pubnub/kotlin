package com.pubnub.api.endpoints.objects_api.uuid;

import com.pubnub.api.PubNubException;
import com.pubnub.api.endpoints.objects_api.BaseObjectApiTest;
import com.pubnub.api.managers.token_manager.TokenManager;
import com.pubnub.api.models.consumer.objects_api.uuid.PNSetUUIDMetadataResult;
import com.pubnub.api.models.server.objects_api.SetUUIDMetadataPayload;
import com.pubnub.api.services.UUIDMetadataService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class SetUUIDMetadataTest extends BaseObjectApiTest {
    @Mock protected UUIDMetadataService uuidMetadataServiceMock;
    @Captor private ArgumentCaptor<SetUUIDMetadataPayload> uuidMetadataPayloadArgumentCaptor;

    @Before
    public void retrofitMocks() {
        when(retrofitManagerMock.getUuidMetadataService()).thenReturn(uuidMetadataServiceMock);
        when(uuidMetadataServiceMock.setUUIDsMetadata(eq(testSubscriptionKey), eq(testUUID), any(), any()))
                .thenAnswer(mockRetrofitSuccessfulCall(() -> {
                    final PNSetUUIDMetadataResult envelope = new PNSetUUIDMetadataResult() {

                    };
                    return envelope;
                }));
    }

    @Test
    public void setNameTest() throws PubNubException {
        //given
        final SetUUIDMetadata setUUIDMetadataUnderTest = SetUUIDMetadata.create(pubNubMock,
                telemetryManagerMock, retrofitManagerMock, new TokenManager());
        final String testName = RandomStringUtils.randomAlphabetic(20);

        //when
        setUUIDMetadataUnderTest
                .name(testName)
                .sync();

        //then
        verify(uuidMetadataServiceMock, times(1))
                .setUUIDsMetadata(eq(testSubscriptionKey), eq(testUUID),
                        uuidMetadataPayloadArgumentCaptor.capture(), any());

        final SetUUIDMetadataPayload capturedSetUUIDMetadataPayload = uuidMetadataPayloadArgumentCaptor.getValue();
        assertEquals(testName, capturedSetUUIDMetadataPayload.getName());
    }

    @Test
    public void setEmailTest() throws PubNubException {
        //given
        final SetUUIDMetadata setUUIDMetadataUnderTest = SetUUIDMetadata.create(pubNubMock,
                telemetryManagerMock, retrofitManagerMock, new TokenManager());
        final String testEmail = RandomStringUtils.randomAlphabetic(10) + "@example.com";

        //when
        setUUIDMetadataUnderTest
                .email(testEmail)
                .sync();

        //then
        verify(uuidMetadataServiceMock, times(1))
                .setUUIDsMetadata(eq(testSubscriptionKey), eq(testUUID),
                        uuidMetadataPayloadArgumentCaptor.capture(), any());

        final SetUUIDMetadataPayload capturedSetUUIDMetadataPayload = uuidMetadataPayloadArgumentCaptor.getValue();
        assertEquals(testEmail, capturedSetUUIDMetadataPayload.getEmail());
    }

    @Test
    public void setExternalIdTest() throws PubNubException {
        //given
        final SetUUIDMetadata setUUIDMetadataUnderTest = SetUUIDMetadata.create(pubNubMock,
                telemetryManagerMock, retrofitManagerMock, new TokenManager());
        final String testExternalId = UUID.randomUUID().toString();

        //when
        setUUIDMetadataUnderTest
                .externalId(testExternalId)
                .sync();

        //then
        verify(uuidMetadataServiceMock, times(1))
                .setUUIDsMetadata(eq(testSubscriptionKey), eq(testUUID),
                        uuidMetadataPayloadArgumentCaptor.capture(), any());

        final SetUUIDMetadataPayload capturedSetUUIDMetadataPayload = uuidMetadataPayloadArgumentCaptor.getValue();
        assertEquals(testExternalId, capturedSetUUIDMetadataPayload.getExternalId());
    }

    @Test
    public void setProfileUrlTest() throws PubNubException {
        //given
        final SetUUIDMetadata setUUIDMetadataUnderTest = SetUUIDMetadata.create(pubNubMock,
                telemetryManagerMock, retrofitManagerMock, new TokenManager());
        final String profileUrl = "http://" + RandomStringUtils.randomAlphabetic(5) + ".example.com";

        //when
        setUUIDMetadataUnderTest
                .profileUrl(profileUrl)
                .sync();

        //then
        verify(uuidMetadataServiceMock, times(1))
                .setUUIDsMetadata(eq(testSubscriptionKey), eq(testUUID),
                        uuidMetadataPayloadArgumentCaptor.capture(), any());

        final SetUUIDMetadataPayload capturedSetUUIDMetadataPayload = uuidMetadataPayloadArgumentCaptor.getValue();
        assertEquals(profileUrl, capturedSetUUIDMetadataPayload.getProfileUrl());
    }

    @Test
    public void setCustomTest() throws PubNubException {
        //given
        final SetUUIDMetadata setUUIDMetadataUnderTest = SetUUIDMetadata.create(pubNubMock,
                telemetryManagerMock, retrofitManagerMock, new TokenManager());
        final Map<String, Object> custom = new HashMap<>();
        custom.put("key1", RandomStringUtils.random(10));
        custom.put("key2", RandomStringUtils.random(10));

        //when
        setUUIDMetadataUnderTest
                .custom(custom)
                .sync();

        //then
        verify(uuidMetadataServiceMock, times(1))
                .setUUIDsMetadata(eq(testSubscriptionKey), eq(testUUID),
                        uuidMetadataPayloadArgumentCaptor.capture(), any());

        final SetUUIDMetadataPayload capturedSetUUIDMetadataPayload = uuidMetadataPayloadArgumentCaptor.getValue();
        assertNotNull(capturedSetUUIDMetadataPayload.getCustom());
    }
}
