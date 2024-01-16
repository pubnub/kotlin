package com.pubnub.api.endpoints

import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.PubNubException
import com.pubnub.api.PubNubTestUtils
import com.pubnub.api.UserId
import com.pubnub.internal.callbacks.SubscribeCallback
import org.junit.Test
import org.mockito.Mockito
import java.util.Random
import java.util.UUID

/**
 * Example of how we could test that parameters from builders are passed to the right calls.
 */
class HistoryBatchEndpointTest {
    private val config = config()
    private val pubnubMock = pubNubImplMock(PubNubTestUtils.getInternalConfigOf(config))
    private val pubNubApi: PubNub = PubNubTestUtils.getApiPubNubOf(config, pubnubMock)
//    private val optionsCaptor: ArgumentCaptor<Map<String, String>> =
//        ArgumentCaptor.forClass<Map<String, String>, Map<*, *>>(
//            MutableMap::class.java
//        )

    @Test
    @Throws(PubNubException::class)
    fun forSingleChannelFetchMessagesAlwaysPassMaxWhenItIsInBound() {
        //given
        val fetchMessagesUnderTest = pubNubApi.fetchMessages()

        val maximumPerChannel = randomInt(MAX_FOR_FETCH_MESSAGES)
        //when
        fetchMessagesUnderTest
            .channels(listOf(TEST_CHANNEL_1))
            .maximumPerChannel(maximumPerChannel)
            .createAction()

        //then
        Mockito.verify(pubnubMock).fetchMessages(
            listOf(TEST_CHANNEL_1),
            maximumPerChannel,
            null,
            null,
            false,
            false,
            true
        )
    }

    @Throws(PubNubException::class)
    private fun config(): PNConfiguration {
        val pnConfiguration = PNConfiguration(UserId("pn-" + UUID.randomUUID()))
        pnConfiguration.setSubscribeKey(SUBSCRIBE_KEY)
        return pnConfiguration
    }

    @Throws(PubNubException::class)
    private fun pubNubImplMock(config: com.pubnub.internal.PNConfiguration): com.pubnub.internal.PubNub {
        val pubnub = Mockito.mock(com.pubnub.internal.PubNub::class.java)
        Mockito.`when`(pubnub.configuration).thenReturn(config)
        Mockito.doNothing().`when`(pubnub).addListener(anyClass(SubscribeCallback::class.java as Class<SubscribeCallback<com.pubnub.internal.PubNub>>))
        return pubnub
    }

    private fun <T> anyClass(type: Class<T>): T = Mockito.any<T>(type)


    companion object {
        private const val TEST_CHANNEL_1 = "TEST_CHANNEL_1"
        private const val TEST_CHANNEL_2 = "TEST_CHANNEL_2"
        private const val SUBSCRIBE_KEY = "SUB_KEY"
        private const val TEST_VERSION = "TEST_VERSION"
        private const val MAX_FOR_FETCH_MESSAGES = 100
        private const val MAX_FOR_FETCH_MESSAGES_WITH_ACTIONS = 25
        private const val MULTIPLE_CHANNELS_MAX_FOR_FETCH_MESSAGES = 25
        private const val EXPECTED_SINGLE_CHANNEL_DEFAULT_MESSAGES = 100
        private const val EXPECTED_MULTIPLE_CHANNEL_DEFAULT_MESSAGES = 25
        private const val EXPECTED_DEFAULT_MESSAGES_WITH_ACTIONS = 25
        private const val EXPECTED_MAX_MESSAGES_WITH_ACTIONS = 25

        //
        //    @Test
        //    public void forSingleChannelFetchMessagesAlwaysPassDefaultWhenNonPositiveMaxSpecified() throws PubNubException {
        //        //given
        //        final FetchMessages fetchMessagesUnderTest = new FetchMessages(pubnub, telemetryManager, retrofitManager,
        //                new TokenManager());
        //
        //        final int maximumPerChannel = -(randomInt(100) - 1);
        //        //when
        //        fetchMessagesUnderTest
        //                .channels(Collections.singletonList(TEST_CHANNEL_1))
        //                .maximumPerChannel(maximumPerChannel)
        //                .sync();
        //
        //        //then
        //        verify(historyService).fetchMessages(eq(SUBSCRIBE_KEY), any(), optionsCaptor.capture());
        //
        //        final Map<String, String> capturedOptions = optionsCaptor.getValue();
        //        assertThat(capturedOptions, allOf(
        //                notNullValue(),
        //                hasEntry(is("max"), is(Integer.toString(EXPECTED_SINGLE_CHANNEL_DEFAULT_MESSAGES)))));
        //    }
        //
        //
        //    @Test
        //    public void forSingleChannelFetchMessagesAlwaysPassDefaultWhenMaxNotSpecified() throws PubNubException {
        //        //given
        //        final FetchMessages fetchMessagesUnderTest = new FetchMessages(pubnub, telemetryManager, retrofitManager,
        //                new TokenManager());
        //
        //        //when
        //        fetchMessagesUnderTest
        //                .channels(Collections.singletonList(TEST_CHANNEL_1))
        //                .sync();
        //
        //        //then
        //        verify(historyService).fetchMessages(eq(SUBSCRIBE_KEY), any(), optionsCaptor.capture());
        //
        //        final Map<String, String> capturedOptions = optionsCaptor.getValue();
        //        assertThat(capturedOptions, allOf(
        //                notNullValue(),
        //                hasEntry(is("max"), is(Integer.toString(EXPECTED_SINGLE_CHANNEL_DEFAULT_MESSAGES)))));
        //    }
        //
        //    @Test
        //    public void forSingleChannelFetchMessagesAlwaysPassDefaultMaxWhenMaxExceeds() throws PubNubException {
        //        //given
        //        final FetchMessages fetchMessagesUnderTest = new FetchMessages(pubnub, telemetryManager, retrofitManager,
        //                new TokenManager());
        //
        //        final int maximumPerChannel = MAX_FOR_FETCH_MESSAGES + randomInt(MAX_FOR_FETCH_MESSAGES);
        //        //when
        //        fetchMessagesUnderTest
        //                .channels(Collections.singletonList(TEST_CHANNEL_1))
        //                .maximumPerChannel(maximumPerChannel)
        //                .sync();
        //
        //        //then
        //        verify(historyService).fetchMessages(eq(SUBSCRIBE_KEY), any(), optionsCaptor.capture());
        //
        //        final Map<String, String> capturedOptions = optionsCaptor.getValue();
        //        assertThat(capturedOptions, allOf(
        //                notNullValue(),
        //                hasEntry(is("max"), is(Integer.toString(EXPECTED_SINGLE_CHANNEL_DEFAULT_MESSAGES)))));
        //    }
        //
        //    @Test
        //    public void forMultipleChannelsFetchMessagesAlwaysPassMaxWhenItIsInBound() throws PubNubException {
        //        //given
        //        final FetchMessages fetchMessagesUnderTest = new FetchMessages(pubnub, telemetryManager, retrofitManager,
        //                new TokenManager());
        //
        //        final int maximumPerChannel = randomInt(MULTIPLE_CHANNELS_MAX_FOR_FETCH_MESSAGES);
        //        //when
        //        fetchMessagesUnderTest
        //                .channels(Arrays.asList(TEST_CHANNEL_1, TEST_CHANNEL_2))
        //                .maximumPerChannel(maximumPerChannel)
        //                .sync();
        //
        //        //then
        //        verify(historyService).fetchMessages(eq(SUBSCRIBE_KEY), any(), optionsCaptor.capture());
        //
        //        final Map<String, String> capturedOptions = optionsCaptor.getValue();
        //        assertThat(capturedOptions, allOf(
        //                notNullValue(),
        //                hasEntry(is("max"), is(Integer.toString(maximumPerChannel)))));
        //    }
        //
        //    @Test
        //    public void forMultipleChannelsFetchMessagesAlwaysPassDefaultWhenNonPositiveMaxSpecified() throws PubNubException {
        //        //given
        //        final FetchMessages fetchMessagesUnderTest = new FetchMessages(pubnub, telemetryManager, retrofitManager,
        //                new TokenManager());
        //
        //        final int maximumPerChannel = -(randomInt(100) - 1);
        //        //when
        //        fetchMessagesUnderTest
        //                .channels(Arrays.asList(TEST_CHANNEL_1, TEST_CHANNEL_2))
        //                .maximumPerChannel(maximumPerChannel)
        //                .sync();
        //
        //        //then
        //        verify(historyService).fetchMessages(eq(SUBSCRIBE_KEY), any(), optionsCaptor.capture());
        //
        //        final Map<String, String> capturedOptions = optionsCaptor.getValue();
        //        assertThat(capturedOptions, allOf(
        //                notNullValue(),
        //                hasEntry(is("max"), is(Integer.toString(EXPECTED_MULTIPLE_CHANNEL_DEFAULT_MESSAGES)))));
        //    }
        //
        //
        //    @Test
        //    public void forMultipleChannelsFetchMessagesAlwaysPassDefaultWhenMaxNotSpecified() throws PubNubException {
        //        //given
        //        final FetchMessages fetchMessagesUnderTest = new FetchMessages(pubnub, telemetryManager, retrofitManager,
        //                new TokenManager());
        //
        //        //when
        //        fetchMessagesUnderTest
        //                .channels(Arrays.asList(TEST_CHANNEL_1, TEST_CHANNEL_2))
        //                .sync();
        //
        //        //then
        //        verify(historyService).fetchMessages(eq(SUBSCRIBE_KEY), any(), optionsCaptor.capture());
        //
        //        final Map<String, String> capturedOptions = optionsCaptor.getValue();
        //        assertThat(capturedOptions, allOf(
        //                notNullValue(),
        //                hasEntry(is("max"), is(Integer.toString(EXPECTED_MULTIPLE_CHANNEL_DEFAULT_MESSAGES)))));
        //    }
        //
        //    @Test
        //    public void forMultipleChannelsFetchMessagesAlwaysPassDefaultMaxWhenMaxExceeds() throws PubNubException {
        //        //given
        //        final FetchMessages fetchMessagesUnderTest = new FetchMessages(pubnub, telemetryManager, retrofitManager,
        //                new TokenManager());
        //
        //        final int maximumPerChannel = MULTIPLE_CHANNELS_MAX_FOR_FETCH_MESSAGES + randomInt(MULTIPLE_CHANNELS_MAX_FOR_FETCH_MESSAGES);
        //        //when
        //        fetchMessagesUnderTest
        //                .channels(Arrays.asList(TEST_CHANNEL_1, TEST_CHANNEL_2))
        //                .maximumPerChannel(maximumPerChannel)
        //                .sync();
        //
        //        //then
        //        verify(historyService).fetchMessages(eq(SUBSCRIBE_KEY), any(), optionsCaptor.capture());
        //
        //        final Map<String, String> capturedOptions = optionsCaptor.getValue();
        //        assertThat(capturedOptions, allOf(
        //                notNullValue(),
        //                hasEntry(is("max"), is(Integer.toString(EXPECTED_MULTIPLE_CHANNEL_DEFAULT_MESSAGES)))));
        //    }
        //
        //    @Test
        //    public void forSingleChannelFetchMessagesWithActionAlwaysPassMaxWhenItIsInBound() throws PubNubException {
        //        //given
        //        final FetchMessages fetchMessagesUnderTest = new FetchMessages(pubnub, telemetryManager, retrofitManager,
        //                new TokenManager());
        //
        //        final int maximumPerChannel = randomInt(MAX_FOR_FETCH_MESSAGES_WITH_ACTIONS);
        //        //when
        //        fetchMessagesUnderTest
        //                .channels(Collections.singletonList(TEST_CHANNEL_1))
        //                .maximumPerChannel(maximumPerChannel)
        //                .includeMessageActions(true)
        //                .sync();
        //
        //        //then
        //        verify(historyService).fetchMessagesWithActions(eq(SUBSCRIBE_KEY), any(), optionsCaptor.capture());
        //
        //        final Map<String, String> capturedOptions = optionsCaptor.getValue();
        //        assertThat(capturedOptions, allOf(
        //                notNullValue(),
        //                hasEntry(is("max"), is(Integer.toString(maximumPerChannel)))));
        //    }
        //
        //    @Test
        //    public void forSingleChannelFetchMessagesWithActionAlwaysPassDefaultWhenMaxNotSpecified() throws PubNubException {
        //        //given
        //        final FetchMessages fetchMessagesUnderTest = new FetchMessages(pubnub, telemetryManager, retrofitManager,
        //                new TokenManager());
        //
        //        //when
        //        fetchMessagesUnderTest
        //                .channels(Collections.singletonList(TEST_CHANNEL_1))
        //                .includeMessageActions(true)
        //                .sync();
        //
        //        //then
        //        verify(historyService).fetchMessagesWithActions(eq(SUBSCRIBE_KEY), any(), optionsCaptor.capture());
        //
        //        final Map<String, String> capturedOptions = optionsCaptor.getValue();
        //        assertThat(capturedOptions, allOf(
        //                notNullValue(),
        //                hasEntry(is("max"), is(Integer.toString(EXPECTED_DEFAULT_MESSAGES_WITH_ACTIONS)))));
        //    }
        //
        //    @Test
        //    public void forSingleChannelFetchMessagesWithActionAlwaysPassDefaultMaxWhenMaxExceeds() throws PubNubException {
        //        //given
        //        final FetchMessages fetchMessagesUnderTest = new FetchMessages(pubnub, telemetryManager, retrofitManager,
        //                new TokenManager());
        //
        //        final int maximumPerChannel = MAX_FOR_FETCH_MESSAGES_WITH_ACTIONS +
        //                randomInt(MAX_FOR_FETCH_MESSAGES_WITH_ACTIONS);
        //        //when
        //        fetchMessagesUnderTest
        //                .channels(Collections.singletonList(TEST_CHANNEL_1))
        //                .maximumPerChannel(maximumPerChannel)
        //                .includeMessageActions(true)
        //                .sync();
        //
        //        //then
        //        verify(historyService).fetchMessagesWithActions(eq(SUBSCRIBE_KEY), any(), optionsCaptor.capture());
        //
        //        final Map<String, String> capturedOptions = optionsCaptor.getValue();
        //        assertThat(capturedOptions, allOf(
        //                notNullValue(),
        //                hasEntry(is("max"), is(Integer.toString(EXPECTED_MAX_MESSAGES_WITH_ACTIONS)))));
        //    }
        private fun randomInt(max: Int): Int {
            val random = Random()
            val randomInt = random.nextInt(max)
            return randomInt + 1
        }
    }
}

