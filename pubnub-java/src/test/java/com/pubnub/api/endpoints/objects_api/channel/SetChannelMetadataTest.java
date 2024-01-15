package com.pubnub.api.endpoints.objects_api.channel;

import com.pubnub.api.PubNubException;
import com.pubnub.api.endpoints.objects_api.BaseObjectApiTest;
import com.pubnub.api.managers.token_manager.TokenManager;
import com.pubnub.api.models.consumer.objects_api.channel.PNSetChannelMetadataResult;
import com.pubnub.api.models.server.objects_api.SetChannelMetadataPayload;
import com.pubnub.api.services.ChannelMetadataService;
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


public class SetChannelMetadataTest extends BaseObjectApiTest {
    @Mock protected ChannelMetadataService channelMetadataServiceMock;
    @Captor private ArgumentCaptor<SetChannelMetadataPayload> channelMetadataPayloadArgumentCaptor;
    protected final String testChannelMetadataId = UUID.randomUUID().toString();

    @Before
    public void retrofitMocks() {
        when(retrofitManagerMock.getChannelMetadataService()).thenReturn(channelMetadataServiceMock);
        when(channelMetadataServiceMock.setChannelsMetadata(eq(testSubscriptionKey), eq(testChannelMetadataId), any(), any()))
                .thenAnswer(mockRetrofitSuccessfulCall(() -> {
                    final PNSetChannelMetadataResult envelope = new PNSetChannelMetadataResult() {

                    };
                    return envelope;
                }));
    }

    @Test
    public void setNameTest() throws PubNubException {
        //given
        final SetChannelMetadata setChannelMetadataUnderTest = SetChannelMetadata
                .builder(pubNubMock, telemetryManagerMock, retrofitManagerMock, new TokenManager())
                .channel(testChannelMetadataId);
        final String testName = RandomStringUtils.randomAlphabetic(20);

        //when
        setChannelMetadataUnderTest
                .name(testName)
                .sync();

        //then
        verify(channelMetadataServiceMock, times(1))
                .setChannelsMetadata(eq(testSubscriptionKey), eq(testChannelMetadataId),
                        channelMetadataPayloadArgumentCaptor.capture(), any());

        final SetChannelMetadataPayload capturedSetChannelMetadataPayload = channelMetadataPayloadArgumentCaptor
                .getValue();
        assertEquals(testName, capturedSetChannelMetadataPayload.getName());
    }

    @Test
    public void setDescriptionTest() throws PubNubException {
        //given
        final SetChannelMetadata setChannelMetadataUnderTest = SetChannelMetadata
                .builder(pubNubMock, telemetryManagerMock, retrofitManagerMock, new TokenManager())
                .channel(testChannelMetadataId);
        final String testDescription = RandomStringUtils.randomAlphabetic(20);

        //when
        setChannelMetadataUnderTest
                .description(testDescription)
                .sync();

        //then
        verify(channelMetadataServiceMock, times(1))
                .setChannelsMetadata(eq(testSubscriptionKey), eq(testChannelMetadataId),
                        channelMetadataPayloadArgumentCaptor.capture(), any());

        final SetChannelMetadataPayload capturedSetChannelMetadataPayload = channelMetadataPayloadArgumentCaptor
                .getValue();
        assertEquals(testDescription, capturedSetChannelMetadataPayload.getDescription());
    }

    @Test
    public void setCustomTest() throws PubNubException {
        //given
        final SetChannelMetadata setChannelMetadataUnderTest = SetChannelMetadata
                .builder(pubNubMock, telemetryManagerMock, retrofitManagerMock, new TokenManager())
                .channel(testChannelMetadataId);

        final Map<String, Object> custom = new HashMap<>();
        custom.put("key1", RandomStringUtils.random(10));
        custom.put("key2", RandomStringUtils.random(10));

        //when
        setChannelMetadataUnderTest
                .custom(custom)
                .sync();

        //then
        verify(channelMetadataServiceMock, times(1))
                .setChannelsMetadata(eq(testSubscriptionKey), eq(testChannelMetadataId),
                        channelMetadataPayloadArgumentCaptor.capture(), any());

        final SetChannelMetadataPayload capturedSetChannelMetadataPayload = channelMetadataPayloadArgumentCaptor
                .getValue();
        assertNotNull(capturedSetChannelMetadataPayload.getCustom());
    }
}
