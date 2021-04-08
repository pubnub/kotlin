package com.pubnub.api.managers;

import com.pubnub.api.PNConfiguration;
import com.pubnub.api.builder.dto.PresenceOperation;
import com.pubnub.api.builder.dto.PubSubOperation;
import com.pubnub.api.builder.dto.StateOperation;
import com.pubnub.api.builder.dto.SubscribeOperation;
import com.pubnub.api.managers.StateManager.SubscriptionStateData;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.pubnub.api.managers.StateManager.HeartbeatStateData;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class StateManagerTest {
    final private List<String> channelsToSubscribe = asList("sub1", "sub2");
    final private List<String> channelsToTracePresence = asList("pres1", "pres2");
    final private String state = "state";

    @Test
    public void heartbeatSendsAllChannelsWhenManualModeTurnedOff() {
        //given
        final PNConfiguration pnConfiguration = new PNConfiguration();
        final StateManager stateManagerUnderTest = new StateManager(pnConfiguration);

        //when
        stateManagerUnderTest.handleOperation(subscribeOperation(channelsToSubscribe),
                presenceOperation(channelsToTracePresence));

        final HeartbeatStateData heartbeatStateData = stateManagerUnderTest.heartbeatStateData();
        final SubscriptionStateData subscriptionStateData = stateManagerUnderTest.subscriptionStateData(true);

        //then
        assertThat(
                subscriptionStateData.getChannels(),
                hasItems(channelsToSubscribe.toArray(new String[]{}))
        );

        assertThat(
                heartbeatStateData.getHeartbeatChannels(),
                both(hasItems(channelsToTracePresence.toArray(new String[]{})))
                        .and(hasItems(channelsToSubscribe.toArray(new String[]{})))
        );
    }

    @Test
    public void heartbeatSendsOnlyPresenceChannelsWhenManualModeTurnedOn() {
        //given
        final PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setManagePresenceListManually(true);
        final StateManager stateManagerUnderTest = new StateManager(pnConfiguration);

        //when
        stateManagerUnderTest.handleOperation(subscribeOperation(channelsToSubscribe),
                presenceOperation(channelsToTracePresence));

        final HeartbeatStateData heartbeatStateData = stateManagerUnderTest
                .heartbeatStateData();
        final SubscriptionStateData subscriptionStateData = stateManagerUnderTest
                .subscriptionStateData(true);

        //then
        assertThat(
                subscriptionStateData.getChannels(),
                hasItems(channelsToSubscribe.toArray(new String[]{}))
        );

        assertThat(
                heartbeatStateData.getHeartbeatChannels(),
                both(hasItems(channelsToTracePresence.toArray(new String[]{})))
                        .and(not(hasItems(channelsToSubscribe.toArray(new String[]{}))))
        );
    }

    @Test
    public void whenManualModeStateOperationAddStateToSubscribedChannels() {
        //given
        StateManager stateManagerUnderTest = new StateManager(withManualPresenceMode(config()));

        //when
        stateManagerUnderTest.handleOperation(subscribeOperation(channelsToSubscribe),
                stateOperation(channelsToSubscribe, state));

        //then
        assertThat(stateManagerUnderTest.subscriptionStateData(false).getStatePayload(),
                Matchers.equalTo(mapChannelsToState(channelsToSubscribe, state)));
        assertThat(stateManagerUnderTest.heartbeatStateData().getStatePayload(),
                Matchers.equalTo(emptyMap()));

    }

    @Test
    public void whenManualModeStateOperationAddStateToHeartbeatChannels() {
        //given
        StateManager stateManagerUnderTest = new StateManager(withManualPresenceMode(config()));

        //when
        stateManagerUnderTest.handleOperation(presenceOperation(channelsToTracePresence),
                stateOperation(channelsToTracePresence, state));

        //then
        assertThat(stateManagerUnderTest.subscriptionStateData(false).getStatePayload(),
                Matchers.equalTo(emptyMap()));
        assertThat(stateManagerUnderTest.heartbeatStateData().getStatePayload(),
                Matchers.equalTo(mapChannelsToState(channelsToTracePresence, state)));

    }

    @Test
    public void whenManualModeOffStateOperationDoNotAddStateToHeartbeatChannels() {
        //given
        StateManager stateManagerUnderTest = new StateManager(withoutManualPresenceMode(config()));

        //when
        stateManagerUnderTest.handleOperation(presenceOperation(channelsToTracePresence),
                stateOperation(channelsToTracePresence, state));

        //then
        assertThat(stateManagerUnderTest.subscriptionStateData(false).getStatePayload(),
                Matchers.equalTo(emptyMap()));
        assertThat(stateManagerUnderTest.heartbeatStateData().getStatePayload(),
                Matchers.equalTo(emptyMap()));
    }

    @Test
    public void whenManualModeOffStateOperationAddStateToSubscribedChannels() {
        //given
        StateManager stateManagerUnderTest = new StateManager(withoutManualPresenceMode(config()));

        //when
        stateManagerUnderTest.handleOperation(subscribeOperation(channelsToSubscribe),
                stateOperation(channelsToSubscribe, state));

        //then
        assertThat(stateManagerUnderTest.subscriptionStateData(false).getStatePayload(),
                Matchers.equalTo(mapChannelsToState(channelsToSubscribe, state)));
        assertThat(stateManagerUnderTest.heartbeatStateData().getStatePayload(),
                Matchers.equalTo(emptyMap()));

    }

    @Test
    public void whenManualModeStateOperationAddStateToSubscribedAndHeartbeatIfBothPresent() {
        //given
        StateManager stateManagerUnderTest = new StateManager(withManualPresenceMode(config()));

        //when
        stateManagerUnderTest.handleOperation(subscribeOperation(channelsToSubscribe),
                presenceOperation(channelsToSubscribe),
                stateOperation(channelsToSubscribe, state));

        //then
        assertThat(stateManagerUnderTest.subscriptionStateData(false).getStatePayload(),
                Matchers.equalTo(mapChannelsToState(channelsToSubscribe, state)));
        assertThat(stateManagerUnderTest.heartbeatStateData().getStatePayload(),
                Matchers.equalTo(mapChannelsToState(channelsToSubscribe, state)));
    }

    private Map<String, Object> mapChannelsToState(List<String> channels, Object state) {
        HashMap<String, Object> result = new HashMap<>();

        for(String channel : channels) {
            result.put(channel, state);
        }

        return result;
    }

    private PNConfiguration config() {
        return new PNConfiguration();
    }

    private PNConfiguration withManualPresenceMode(PNConfiguration config) {
        //noinspection deprecation
        config.setManagePresenceListManually(true);
        return config;
    }

    private PNConfiguration withoutManualPresenceMode(PNConfiguration config) {
        //noinspection deprecation
        config.setManagePresenceListManually(false);
        return config;
    }

    private PresenceOperation presenceOperation(final List<String> channelsToTracePresence) {
        return PresenceOperation.builder()
                .channels(channelsToTracePresence)
                .connected(true)
                .build();
    }

    private SubscribeOperation subscribeOperation(final List<String> channelsToSubscribe) {
        return SubscribeOperation.builder()
                .channels(channelsToSubscribe)
                .build();
    }

    private PubSubOperation stateOperation(final List<String> channelsToSubscribe, Object state) {
        return StateOperation
                .builder()
                .channels(channelsToSubscribe)
                .state(state)
                .build();
    }
}
