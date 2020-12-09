package com.pubnub.api.integration.managers.subscription;

import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.objects_api.channel.PNChannelMetadataResult;
import com.pubnub.api.models.consumer.objects_api.membership.PNMembershipResult;
import com.pubnub.api.models.consumer.objects_api.uuid.PNUUIDMetadataResult;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.pubnub.api.models.consumer.pubsub.PNSignalResult;
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult;
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult;

public class SubscribeCallbackAdapter extends SubscribeCallback {

    @Override
    public void status(final PubNub pubnub, final PNStatus pnStatus) {

    }

    @Override
    public void message(final PubNub pubnub, final PNMessageResult pnMessageResult) {

    }

    @Override
    public void presence(final PubNub pubnub, final PNPresenceEventResult pnPresenceEventResult) {

    }

    @Override
    public void signal(final PubNub pubnub, final PNSignalResult pnSignalResult) {

    }

    @Override
    public void uuid(final PubNub pubnub, final PNUUIDMetadataResult pnUUIDMetadataResult) {

    }

    @Override
    public void channel(final PubNub pubnub, final PNChannelMetadataResult pnChannelMetadataResult) {

    }

    @Override
    public void membership(final PubNub pubnub, final PNMembershipResult pnMembershipResult) {

    }

    @Override
    public void messageAction(final PubNub pubnub, final PNMessageActionResult pnMessageActionResult) {

    }

    @Override
    public void file(final PubNub pubnub, final PNFileEventResult pnFileEventResult) {

    }
}
