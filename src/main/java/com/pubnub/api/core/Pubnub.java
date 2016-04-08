package com.pubnub.api.core;


import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.core.builder.SubscribeBuilder;
import com.pubnub.api.core.builder.UnsubscribeBuilder;
import com.pubnub.api.endpoints.History;
import com.pubnub.api.endpoints.Time;
import com.pubnub.api.endpoints.access.Audit;
import com.pubnub.api.endpoints.access.Grant;
import com.pubnub.api.endpoints.presence.*;
import com.pubnub.api.endpoints.pubsub.Publish;
import com.pubnub.api.endpoints.push.CreatePushNotification;
import com.pubnub.api.endpoints.push.ListProvisions;
import com.pubnub.api.endpoints.push.ModifyProvisions;
import com.pubnub.api.managers.BasePathManager;
import com.pubnub.api.managers.StateManager;
import com.pubnub.api.managers.SubscriptionManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


@Getter
@Slf4j
public class Pubnub {

    private PnConfiguration configuration;
    private StateManager stateManager;
    private SubscriptionManager subscriptionManager;
    private BasePathManager basePathManager;

    public Pubnub(final PnConfiguration initialConfig) {
        this.configuration = initialConfig;
        this.stateManager = new StateManager();
        this.subscriptionManager = new SubscriptionManager(this);
        this.basePathManager = new BasePathManager(initialConfig);
    }

    public String getBaseUrl() {
        return this.basePathManager.getBasePath();
    }


    //
    public final void addListener(SubscribeCallback listener) {
        subscriptionManager.addListener(listener);
    }

    public final void removeListener(SubscribeCallback listener) {
        subscriptionManager.removeListener(listener);
    }

    public final SubscribeBuilder subscribe() {
        return new SubscribeBuilder(this.subscriptionManager);
    }

    public final UnsubscribeBuilder unsubscribe() { return new UnsubscribeBuilder(this.subscriptionManager); }

    // start push

    public final ModifyProvisions.ModifyProvisionsBuilder modifyPushProvisions() {
        return ModifyProvisions.builder().pubnub(this);
    }

    public final ListProvisions.ListProvisionsBuilder listPushProvisions() {
        return ListProvisions.builder().pubnub(this);
    }

    public final CreatePushNotification.CreatePushNotificationBuilder createPushNotification() {
        return CreatePushNotification.builder().pubnub(this);
    }

    // end push

    public final WhereNow.WhereNowBuilder whereNow() {
        return WhereNow.builder().pubnub(this);
    }
    public final HereNow.HereNowBuilder hereNow() {
        return HereNow.builder().pubnub(this);
    }

    public final Time.TimeBuilder time() {
        return Time.builder().pubnub(this);
    }

    public final History.HistoryBuilder history() { return History.builder().pubnub(this); }

    public Leave.LeaveBuilder leave() {
        return Leave.builder()
            .pubnub(this)
            .stateManager(stateManager);
    }

    public final Audit.AuditBuilder audit() {
        return Audit.builder().pubnub(this);
    }
    public final Grant.GrantBuilder grant() {
        return Grant.builder().pubnub(this);
    }

    public GetState.GetStateBuilder getPresenceState() { return GetState.builder().pubnub(this); }
    public SetState.SetStateBuilder setPresenceState() { return SetState.builder().pubnub(this).stateManager(stateManager); }

    public Publish.PublishBuilder publish() { return Publish.builder().pubnub(this); }

}
