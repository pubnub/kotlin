package com.pubnub.api.endpoints.objects_api;

import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.managers.RetrofitManager;
import com.pubnub.api.managers.TelemetryManager;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import retrofit2.Call;
import retrofit2.Response;

import java.util.UUID;
import java.util.function.Supplier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public abstract class BaseObjectApiTest {
    private static final String TEST_PUBNUB_VERSION = "123";
    protected final String testSubscriptionKey = UUID.randomUUID().toString();
    protected final String testUUID = UUID.randomUUID().toString();

    @Mock
    private PNConfiguration configurationMock;
    @Mock protected PubNub pubNubMock;
    @Mock protected TelemetryManager telemetryManagerMock;
    @Mock protected RetrofitManager retrofitManagerMock;

    @NotNull
    protected static <T> Answer<Call<T>> mockRetrofitSuccessfulCall(final Supplier<T> block) {
        return invocation -> {
            final Call<T> mockCall = mock(Call.class);
            when(mockCall.execute()).thenAnswer(blockInvocation -> Response.success(block.get()));
            return mockCall;
        };
    }

    @Before
    public void configureMocks() {
        when(configurationMock.getSubscribeKey()).thenReturn(testSubscriptionKey);
        when(configurationMock.getUuid()).thenReturn(testUUID);
        when(pubNubMock.getConfiguration()).thenReturn(configurationMock);
        when(pubNubMock.getVersion()).thenReturn(TEST_PUBNUB_VERSION);
    }
}
