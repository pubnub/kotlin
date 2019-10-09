package com.pubnub.api.workers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.PubNubUtil;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.managers.DuplicationManager;
import com.pubnub.api.managers.ListenerManager;
import com.pubnub.api.managers.MapperManager;
import com.pubnub.api.models.consumer.PNErrorData;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.message_actions.PNMessageAction;
import com.pubnub.api.models.consumer.objects_api.space.PNSpace;
import com.pubnub.api.models.consumer.objects_api.user.PNUser;
import com.pubnub.api.models.consumer.pubsub.BasePubSubResult;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.pubnub.api.models.consumer.pubsub.PNSignalResult;
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult;
import com.pubnub.api.models.consumer.pubsub.objects.ObjectPayload;
import com.pubnub.api.models.consumer.pubsub.objects.PNMembershipResult;
import com.pubnub.api.models.consumer.pubsub.objects.PNSpaceResult;
import com.pubnub.api.models.consumer.pubsub.objects.PNUserResult;
import com.pubnub.api.models.server.PresenceEnvelope;
import com.pubnub.api.models.server.PublishMetaData;
import com.pubnub.api.models.server.SubscribeMessage;
import com.pubnub.api.vendor.Crypto;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;


@Slf4j
public class SubscribeMessageWorker implements Runnable {

    private final int typeMessage = 0;
    private final int typeSignal = 1;
    private final int typeObject = 2;
    private final int typeMessageAction = 3;

    private PubNub pubnub;
    private ListenerManager listenerManager;
    private LinkedBlockingQueue<SubscribeMessage> queue;
    private DuplicationManager duplicationManager;

    private boolean isRunning;

    public SubscribeMessageWorker(PubNub pubnubInstance,
                                  ListenerManager listenerManagerInstance,
                                  LinkedBlockingQueue<SubscribeMessage> queueInstance,
                                  DuplicationManager dupManager) {
        this.pubnub = pubnubInstance;
        this.listenerManager = listenerManagerInstance;
        this.queue = queueInstance;
        this.duplicationManager = dupManager;
    }

    @Override
    public void run() {
        takeMessage();
    }


    private void takeMessage() {
        this.isRunning = true;

        while (this.isRunning) {
            try {
                this.processIncomingPayload(this.queue.take());
            } catch (InterruptedException e) {
                this.isRunning = false;
                log.trace("take message interrupted", e);
            }
        }
    }

    private JsonElement processMessage(SubscribeMessage subscribeMessage) {
        JsonElement input = subscribeMessage.getPayload();

        // if we do not have a crypto key, there is no way to process the node; let's return.
        if (pubnub.getConfiguration().getCipherKey() == null) {
            return input;
        }

        // if the message couldn't possibly be encrypted in the first place, there is no way to process the node; let's
        // return.
        if (!subscribeMessage.supportsEncryption()) {
            return input;
        }

        Crypto crypto = new Crypto(pubnub.getConfiguration().getCipherKey());
        MapperManager mapper = this.pubnub.getMapper();
        String inputText;
        String outputText;
        JsonElement outputObject;

        if (mapper.isJsonObject(input) && mapper.hasField(input, "pn_other")) {
            inputText = mapper.elementToString(input, "pn_other");
        } else {
            inputText = mapper.elementToString(input);
        }

        try {
            outputText = crypto.decrypt(inputText);
        } catch (PubNubException e) {
            PNStatus pnStatus = PNStatus.builder().error(true)
                    .errorData(new PNErrorData(e.getMessage(), e))
                    .operation(PNOperationType.PNSubscribeOperation)
                    .category(PNStatusCategory.PNDecryptionErrorCategory)
                    .build();

            listenerManager.announce(pnStatus);
            return null;
        }

        try {
            outputObject = mapper.fromJson(outputText, JsonElement.class);
        } catch (PubNubException e) {
            PNStatus pnStatus = PNStatus.builder().error(true)
                    .errorData(new PNErrorData(e.getMessage(), e))
                    .operation(PNOperationType.PNSubscribeOperation)
                    .category(PNStatusCategory.PNMalformedResponseCategory)
                    .build();

            listenerManager.announce(pnStatus);
            return null;
        }

        // inject the decoded response into the payload
        if (mapper.isJsonObject(input) && mapper.hasField(input, "pn_other")) {
            JsonObject objectNode = mapper.getAsObject(input);
            mapper.putOnObject(objectNode, "pn_other", outputObject);
            outputObject = objectNode;
        }

        return outputObject;
    }

    private void processIncomingPayload(SubscribeMessage message) {
        MapperManager mapper = this.pubnub.getMapper();

        String channel = message.getChannel();
        String subscriptionMatch = message.getSubscriptionMatch();
        PublishMetaData publishMetaData = message.getPublishMetaData();

        if (channel != null && channel.equals(subscriptionMatch)) {
            subscriptionMatch = null;
        }

        if (this.pubnub.getConfiguration().isDedupOnSubscribe()) {
            if (this.duplicationManager.isDuplicate(message)) {
                return;
            } else {
                this.duplicationManager.addEntry(message);
            }
        }

        if (message.getChannel().endsWith("-pnpres")) {
            PresenceEnvelope presencePayload = mapper.convertValue(message.getPayload(), PresenceEnvelope.class);

            String strippedPresenceChannel = null;
            String strippedPresenceSubscription = null;

            if (channel != null) {
                strippedPresenceChannel = PubNubUtil.replaceLast(channel, "-pnpres", "");
            }
            if (subscriptionMatch != null) {
                strippedPresenceSubscription = PubNubUtil.replaceLast(subscriptionMatch, "-pnpres", "");
            }

            JsonElement isHereNowRefresh = message.getPayload().getAsJsonObject().get("here_now_refresh");

            PNPresenceEventResult pnPresenceEventResult = PNPresenceEventResult.builder()
                    .event(presencePayload.getAction())
                    // deprecated
                    .actualChannel((subscriptionMatch != null) ? channel : null)
                    .subscribedChannel(subscriptionMatch != null ? subscriptionMatch : channel)
                    // deprecated
                    .channel(strippedPresenceChannel)
                    .subscription(strippedPresenceSubscription)
                    .state(presencePayload.getData())
                    .timetoken(publishMetaData.getPublishTimetoken())
                    .occupancy(presencePayload.getOccupancy())
                    .uuid(presencePayload.getUuid())
                    .timestamp(presencePayload.getTimestamp())
                    .join(getDelta(message.getPayload().getAsJsonObject().get("join")))
                    .leave(getDelta(message.getPayload().getAsJsonObject().get("leave")))
                    .timeout(getDelta(message.getPayload().getAsJsonObject().get("timeout")))
                    .hereNowRefresh(isHereNowRefresh != null && isHereNowRefresh.getAsBoolean())
                    .build();

            listenerManager.announce(pnPresenceEventResult);
        } else {
            JsonElement extractedMessage = processMessage(message);

            if (extractedMessage == null) {
                log.debug("unable to parse payload on #processIncomingMessages");
            }

            BasePubSubResult result = BasePubSubResult.builder()
                    // deprecated
                    .actualChannel((subscriptionMatch != null) ? channel : null)
                    .subscribedChannel(subscriptionMatch != null ? subscriptionMatch : channel)
                    // deprecated
                    .channel(channel)
                    .subscription(subscriptionMatch)
                    .timetoken(publishMetaData.getPublishTimetoken())
                    .publisher(message.getIssuingClientId())
                    .userMetadata(message.getUserMetadata())
                    .build();

            if (message.getType() == null) {
                listenerManager.announce(new PNMessageResult(result, extractedMessage));
            } else if (message.getType() == typeMessage) {
                listenerManager.announce(new PNMessageResult(result, extractedMessage));
            } else if (message.getType() == typeSignal) {
                listenerManager.announce(new PNSignalResult(result, extractedMessage));
            } else if (message.getType() == typeObject) {
                ObjectPayload objectPayload = mapper.convertValue(extractedMessage, ObjectPayload.class);
                String type = objectPayload.getType();
                switch (type) {
                    case "user":
                        listenerManager.announce(PNUserResult.userBuilder()
                                .result(result)
                                .event(objectPayload.getEvent())
                                .user(mapper.convertValue(objectPayload.getData(), PNUser.class))
                                .build());
                        break;
                    case "space":
                        listenerManager.announce(PNSpaceResult.spaceBuilder()
                                .result(result)
                                .event(objectPayload.getEvent())
                                .space(mapper.convertValue(objectPayload.getData(), PNSpace.class))
                                .build());
                        break;
                    case "membership":
                        listenerManager.announce(PNMembershipResult.membershipBuilder()
                                .result(result)
                                .event(objectPayload.getEvent())
                                .data(objectPayload.getData())
                                .build());
                        break;
                    default:
                }
            } else if (message.getType() == typeMessageAction) {
                ObjectPayload objectPayload = mapper.convertValue(extractedMessage, ObjectPayload.class);
                JsonObject data = objectPayload.getData().getAsJsonObject();
                if (!data.has("uuid")) {
                    data.addProperty("uuid", result.getPublisher());
                }
                listenerManager.announce(PNMessageActionResult.actionBuilder()
                        .result(result)
                        .event(objectPayload.getEvent())
                        .data(mapper.convertValue(data, PNMessageAction.class))
                        .build());
            }

        }
    }

    private List<String> getDelta(JsonElement delta) {
        List<String> list = new ArrayList<>();
        if (delta != null) {
            JsonArray jsonArray = delta.getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                list.add(jsonArray.get(i).getAsString());
            }
        }

        return list;

    }
}
