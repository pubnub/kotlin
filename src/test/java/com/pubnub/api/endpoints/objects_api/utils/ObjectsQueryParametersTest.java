package com.pubnub.api.endpoints.objects_api.utils;

import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.endpoints.objects_api.BaseObjectApiTest;
import com.pubnub.api.endpoints.objects_api.CompositeParameterEnricher;
import com.pubnub.api.endpoints.objects_api.ObjectApiEndpoint;
import com.pubnub.api.endpoints.objects_api.utils.Include.ChannelIncludeAware;
import com.pubnub.api.endpoints.objects_api.utils.Include.CustomIncludeAware;
import com.pubnub.api.endpoints.objects_api.utils.Include.HavingChannelInclude;
import com.pubnub.api.endpoints.objects_api.utils.Include.HavingCustomInclude;
import com.pubnub.api.endpoints.objects_api.utils.Include.HavingUUIDInclude;
import com.pubnub.api.endpoints.objects_api.utils.Include.PNChannelDetailsLevel;
import com.pubnub.api.endpoints.objects_api.utils.Include.PNUUIDDetailsLevel;
import com.pubnub.api.endpoints.objects_api.utils.Include.UUIDIncludeAware;
import com.pubnub.api.endpoints.objects_api.utils.ListCapabilities.HavingListCapabilites;
import com.pubnub.api.endpoints.objects_api.utils.ListCapabilities.ListCapabilitiesAware;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.managers.RetrofitManager;
import com.pubnub.api.managers.TelemetryManager;
import com.pubnub.api.managers.token_manager.TokenManager;
import com.pubnub.api.models.consumer.PNPage;
import com.pubnub.api.models.server.objects_api.EntityArrayEnvelope;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.pubnub.api.builder.PubNubErrorBuilder.PNERR_PAGINATION_NEXT_OUT_OF_BOUNDS;
import static com.pubnub.api.builder.PubNubErrorBuilder.PNERR_PAGINATION_PREV_OUT_OF_BOUNDS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ObjectsQueryParametersTest extends BaseObjectApiTest {

    private final Random random = new Random();

    abstract class ExampleTestEndpoint extends ObjectApiEndpoint<Object, Object> implements
            ListCapabilitiesAware<ExampleTestEndpoint>,
            CustomIncludeAware<ExampleTestEndpoint>,
            ChannelIncludeAware<ExampleTestEndpoint>,
            UUIDIncludeAware<ExampleTestEndpoint> {
        Map<String, String> effectiveParams;

        public ExampleTestEndpoint(PubNub pubnubInstance, TelemetryManager telemetry, RetrofitManager retrofitInstance, CompositeParameterEnricher compositeParameterEnricher) {
            super(pubnubInstance, telemetry, retrofitInstance, compositeParameterEnricher, new TokenManager());
        }
    }

    class ExampleTestEndpointCommand extends ExampleTestEndpoint implements
            HavingListCapabilites<ExampleTestEndpoint>,
            HavingCustomInclude<ExampleTestEndpoint>,
            HavingChannelInclude<ExampleTestEndpoint>,
            HavingUUIDInclude<ExampleTestEndpoint> {
        public ExampleTestEndpointCommand(PubNub pubnubInstance, TelemetryManager telemetry, RetrofitManager retrofitInstance) {
            super(pubnubInstance, telemetry, retrofitInstance, CompositeParameterEnricher.createDefault());
        }

        @Override
        public CompositeParameterEnricher getCompositeParameterEnricher() {
            return super.getCompositeParameterEnricher();
        }

        @Override
        public Call<Object> executeCommand(Map<String, String> effectiveParams) throws PubNubException {
            this.effectiveParams = new HashMap<>(effectiveParams);
            final Call<Object> mockCall = mock(Call.class);
            try {
                when(mockCall.execute()).thenAnswer(invocation -> Response.success(new Object()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return mockCall;
        }

        @Override
        protected Object createResponse(Response<Object> input) throws PubNubException {
            return null;
        }

        @Override
        protected PNOperationType getOperationType() {
            return null;
        }
    }

    @Test
    public void noAdditionalParametersSpecified() throws PubNubException {
        //given
        final ExampleTestEndpoint exampleTestEndpoint = new ExampleTestEndpointCommand(pubNubMock, telemetryManagerMock, retrofitManagerMock);

        //when
        exampleTestEndpoint
                .sync();

        //then
        assertFalse(exampleTestEndpoint.effectiveParams.containsKey(Filter.FILTER_PARAM_NAME));
    }

    @Test
    public void filterParametersSpecified() throws PubNubException {
        //given
        final String randomFilterExpression = RandomStringUtils.randomAlphabetic(20);

        final ExampleTestEndpoint exampleTestEndpoint = new ExampleTestEndpointCommand(pubNubMock, telemetryManagerMock, retrofitManagerMock);

        //when
        exampleTestEndpoint
                .filter(randomFilterExpression)
                .sync();

        //then
        assertTrue(exampleTestEndpoint.effectiveParams.containsKey(Filter.FILTER_PARAM_NAME));
        assertEquals(randomFilterExpression, exampleTestEndpoint.effectiveParams.get(Filter.FILTER_PARAM_NAME));
    }

    @Test
    public void limitParametersSpecified() throws PubNubException {
        //given
        final Integer randomLimit = random.nextInt(100);

        final ExampleTestEndpoint exampleTestEndpoint = new ExampleTestEndpointCommand(pubNubMock, telemetryManagerMock, retrofitManagerMock);

        //when
        exampleTestEndpoint
                .limit(randomLimit)
                .sync();

        //then
        assertTrue(exampleTestEndpoint.effectiveParams.containsKey(Limiter.LIMIT_PARAM_NAME));
        assertEquals(randomLimit.toString(), exampleTestEndpoint.effectiveParams.get(Limiter.LIMIT_PARAM_NAME));
    }

    @Test
    public void sortParametersSpecified() throws PubNubException {
        //given
        final PNSortKey.Key randomKey = PNSortKey.Key.values()[random.nextInt(PNSortKey.Key.values().length)];
        final PNSortKey.Dir randomDir = PNSortKey.Dir.values()[random.nextInt(PNSortKey.Dir.values().length)];
        final PNSortKey randomSortKey = PNSortKey.of(randomKey, randomDir);

        final ExampleTestEndpoint exampleTestEndpoint = new ExampleTestEndpointCommand(pubNubMock, telemetryManagerMock,
                retrofitManagerMock);

        //when
        exampleTestEndpoint
                .sort(randomSortKey)
                .sync();

        //then
        assertTrue(exampleTestEndpoint.effectiveParams.containsKey(Sorter.SORT_PARAM_NAME));
        assertEquals(randomSortKey.toSortParameter(), exampleTestEndpoint.effectiveParams.get(Sorter.SORT_PARAM_NAME));
    }

    @Test
    public void nextPageParametersSpecified() throws PubNubException {
        //given
        final ExampleTestEndpoint exampleTestEndpoint = new ExampleTestEndpointCommand(pubNubMock, telemetryManagerMock,
                retrofitManagerMock);

        final EntityArrayEnvelope<Object> testEntityArrayEnvelope = new EntityArrayEnvelope<Object>() {
            {
                this.next = RandomStringUtils.randomAlphabetic(20);
                this.prev = RandomStringUtils.randomAlphabetic(20);
            }
        };

        final PNPage nextPage = testEntityArrayEnvelope.nextPage();

        //when
        exampleTestEndpoint
                .page(nextPage)
                .sync();

        //then
        assertTrue(exampleTestEndpoint.effectiveParams.containsKey(Pager.START_PARAM_NAME));
        assertEquals(testEntityArrayEnvelope.getNext(),
                exampleTestEndpoint.effectiveParams.get(Pager.START_PARAM_NAME));
    }

    @Test
    public void previousPageParametersSpecified() throws PubNubException {
        //given
        final ExampleTestEndpoint exampleTestEndpoint = new ExampleTestEndpointCommand(pubNubMock, telemetryManagerMock,
                retrofitManagerMock);

        final EntityArrayEnvelope<Object> testEntityArrayEnvelope = new EntityArrayEnvelope<Object>() {
            {
                this.next = RandomStringUtils.randomAlphabetic(20);
                this.prev = RandomStringUtils.randomAlphabetic(20);
            }
        };

        final PNPage prevoiousPage = testEntityArrayEnvelope.previousPage();

        //when
        exampleTestEndpoint
                .page(prevoiousPage)
                .sync();

        //then
        assertTrue(exampleTestEndpoint.effectiveParams.containsKey(Pager.END_PARAM_NAME));
        assertEquals(testEntityArrayEnvelope.getPrev(),
                exampleTestEndpoint.effectiveParams.get(Pager.END_PARAM_NAME));
    }

    @Test
    public void includeTotalCountParametersSpecified() throws PubNubException {
        //given
        final ExampleTestEndpoint exampleTestEndpoint = new ExampleTestEndpointCommand(pubNubMock, telemetryManagerMock,
                retrofitManagerMock);

        final Boolean randomBoolean = random.nextBoolean();

        //when
        exampleTestEndpoint
                .includeTotalCount(randomBoolean)
                .sync();

        //then
        assertTrue(exampleTestEndpoint.effectiveParams.containsKey(TotalCounter.COUNT_PARAM_NAME));
        assertEquals(randomBoolean.toString(), exampleTestEndpoint.effectiveParams.get(TotalCounter.COUNT_PARAM_NAME));
    }

    @Test
    public void includeCustomParametersSpecified() throws PubNubException {
        //given
        final ExampleTestEndpoint exampleTestEndpoint = new ExampleTestEndpointCommand(pubNubMock, telemetryManagerMock,
                retrofitManagerMock);

        final Boolean randomBoolean = random.nextBoolean();

        //when
        exampleTestEndpoint
                .includeCustom(true)
                .sync();

        //then
        assertTrue(exampleTestEndpoint.effectiveParams.containsKey(Include.INCLUDE_PARAM_NAME));
        assertTrue(Arrays.asList(exampleTestEndpoint.effectiveParams.get(Include.INCLUDE_PARAM_NAME).split(","))
                .contains(Include.INCLUDE_CUSTOM_PARAM_VALUE));
    }

    @Test
    public void includeChannelParametersSpecified() throws PubNubException {
        //given
        final ExampleTestEndpoint exampleTestEndpoint = new ExampleTestEndpointCommand(pubNubMock, telemetryManagerMock,
                retrofitManagerMock);

        final PNChannelDetailsLevel channelDetailsLevel = PNChannelDetailsLevel.CHANNEL;

        //when
        exampleTestEndpoint
                .includeChannel(channelDetailsLevel)
                .sync();

        //then
        assertTrue(exampleTestEndpoint.effectiveParams.containsKey(Include.INCLUDE_PARAM_NAME));
        assertTrue(Arrays.asList(exampleTestEndpoint.effectiveParams.get(Include.INCLUDE_PARAM_NAME).split(","))
                .contains(Include.INCLUDE_CHANNEL_PARAM_VALUE));
    }

    @Test
    public void includeChannelCustomParametersSpecified() throws PubNubException {
        //given
        final ExampleTestEndpoint exampleTestEndpoint = new ExampleTestEndpointCommand(pubNubMock, telemetryManagerMock,
                retrofitManagerMock);

        final PNChannelDetailsLevel channelWithCustomDetailsLevel = PNChannelDetailsLevel.CHANNEL_WITH_CUSTOM;

        //when
        exampleTestEndpoint
                .includeChannel(channelWithCustomDetailsLevel)
                .sync();

        //then
        assertTrue(exampleTestEndpoint.effectiveParams.containsKey(Include.INCLUDE_PARAM_NAME));
        assertTrue(Arrays.asList(exampleTestEndpoint.effectiveParams.get(Include.INCLUDE_PARAM_NAME).split(","))
                .contains(Include.INCLUDE_CHANNEL_CUSTOM_PARAM_VALUE));
    }

    @Test
    public void includeUUIDParametersSpecified() throws PubNubException {
        //given
        final ExampleTestEndpoint exampleTestEndpoint = new ExampleTestEndpointCommand(pubNubMock, telemetryManagerMock,
                retrofitManagerMock);

        final PNUUIDDetailsLevel uuidDetailsLevel = PNUUIDDetailsLevel.UUID;

        //when
        exampleTestEndpoint
                .includeUUID(uuidDetailsLevel)
                .sync();

        //then
        assertTrue(exampleTestEndpoint.effectiveParams.containsKey(Include.INCLUDE_PARAM_NAME));
        assertTrue(Arrays.asList(exampleTestEndpoint.effectiveParams.get(Include.INCLUDE_PARAM_NAME).split(","))
                .contains(Include.INCLUDE_UUID_PARAM_VALUE));
    }

    @Test
    public void includeUUIDCustomParametersSpecified() throws PubNubException {
        //given
        final ExampleTestEndpoint exampleTestEndpoint = new ExampleTestEndpointCommand(pubNubMock, telemetryManagerMock,
                retrofitManagerMock);

        final PNUUIDDetailsLevel uuidWithCustomDetailsLevel = PNUUIDDetailsLevel.UUID_WITH_CUSTOM;

        //when
        exampleTestEndpoint
                .includeUUID(uuidWithCustomDetailsLevel)
                .sync();

        //then
        assertTrue(exampleTestEndpoint.effectiveParams.containsKey(Include.INCLUDE_PARAM_NAME));
        assertTrue(Arrays.asList(exampleTestEndpoint.effectiveParams.get(Include.INCLUDE_PARAM_NAME).split(","))
                .contains(Include.INCLUDE_UUID_CUSTOM_PARAM_VALUE));
    }

    @Test
    public void validationErrorWhenPrevRequestedOnFirstPage() throws PubNubException {
        //given
        final ExampleTestEndpoint exampleTestEndpoint = new ExampleTestEndpointCommand(pubNubMock, telemetryManagerMock,
                retrofitManagerMock);

        final PNPage nonExistingPreviousPage = PNPage.previous(null);

        //when
        try {
            exampleTestEndpoint.page(nonExistingPreviousPage).sync();
            fail();
        }
        //then
        catch (PubNubException e) {
            assertEquals(PNERR_PAGINATION_PREV_OUT_OF_BOUNDS, e.getPubnubError().getErrorCode());
        }
    }

    @Test
    public void validationErrorWhenNextRequestedOnFirstPage() throws PubNubException {
        //given
        final ExampleTestEndpoint exampleTestEndpoint = new ExampleTestEndpointCommand(pubNubMock, telemetryManagerMock,
                retrofitManagerMock);

        final PNPage nonExistingNextPage = PNPage.next(null);

        //when
        try {
            exampleTestEndpoint.page(nonExistingNextPage).sync();
            fail();
        }
        //then
        catch (PubNubException e) {
            assertEquals(PNERR_PAGINATION_NEXT_OUT_OF_BOUNDS, e.getPubnubError().getErrorCode());
        }
    }
}
