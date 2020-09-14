package com.pubnub.api.integration;

import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.endpoints.message_actions.GetMessageActions;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.integration.util.BaseIntegrationTest;
import com.pubnub.api.integration.util.RandomGenerator;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.history.PNFetchMessagesResult;
import com.pubnub.api.models.consumer.message_actions.PNAddMessageActionResult;
import com.pubnub.api.models.consumer.message_actions.PNGetMessageActionsResult;
import com.pubnub.api.models.consumer.message_actions.PNMessageAction;
import com.pubnub.api.models.consumer.objects_api.channel.PNChannelMetadataResult;
import com.pubnub.api.models.consumer.objects_api.membership.PNMembershipResult;
import com.pubnub.api.models.consumer.objects_api.uuid.PNUUIDMetadataResult;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.pubnub.api.models.consumer.pubsub.PNSignalResult;
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult;
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult;
import org.awaitility.Awaitility;
import org.awaitility.Durations;
import org.hamcrest.core.IsEqual;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static com.pubnub.api.builder.PubNubErrorBuilder.PNERROBJ_CHANNEL_MISSING;
import static com.pubnub.api.builder.PubNubErrorBuilder.PNERROBJ_MESSAGE_ACTION_MISSING;
import static com.pubnub.api.builder.PubNubErrorBuilder.PNERROBJ_MESSAGE_ACTION_TIMETOKEN_MISSING;
import static com.pubnub.api.builder.PubNubErrorBuilder.PNERROBJ_MESSAGE_ACTION_TYPE_MISSING;
import static com.pubnub.api.builder.PubNubErrorBuilder.PNERROBJ_MESSAGE_ACTION_VALUE_MISSING;
import static com.pubnub.api.builder.PubNubErrorBuilder.PNERROBJ_MESSAGE_TIMETOKEN_MISSING;
import static com.pubnub.api.integration.util.Utils.isSorted;
import static com.pubnub.api.integration.util.Utils.parseDate;
import static com.pubnub.api.integration.util.Utils.publish;
import static com.pubnub.api.integration.util.Utils.publishMixed;
import static com.pubnub.api.integration.util.Utils.random;
import static com.pubnub.api.integration.util.Utils.randomChannel;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class MessageActionsTest extends BaseIntegrationTest {

    private PNPublishResult publishResult;
    private static final String expectedChannel = random();

    @Override
    protected void onBefore() {
        try {
            publishResult = pubNub.publish()
                    .channel(expectedChannel)
                    .message(generatePayload())
                    .shouldStore(true)
                    .sync();
        } catch (PubNubException e) {
            e.printStackTrace();
            throw new RuntimeException("Message should have been published.");
        }
    }

    @Test
    public void testAddMessageAction() {
        final AtomicBoolean success = new AtomicBoolean();

        pubNub.addMessageAction()
                .channel(expectedChannel)
                .messageAction(new PNMessageAction()
                        .setType("reaction")
                        .setValue("smiley")
                        .setMessageTimetoken(publishResult.getTimetoken()))
                .async((result, status) -> {
                    assertFalse(status.isError());
                    assertEquals(PNOperationType.PNAddMessageAction, status.getOperation());
                    success.set(true);
                });

        listen(success);
    }

    @Test
    public void testGetMessageAction() throws PubNubException {
        final AtomicBoolean success = new AtomicBoolean();

        pubNub.addMessageAction()
                .channel(expectedChannel)
                .messageAction(new PNMessageAction()
                        .setType(random())
                        .setValue(random())
                        .setMessageTimetoken(publishResult.getTimetoken()))
                .sync();

        pubNub.getMessageActions()
                .channel(expectedChannel)
                .async((result, status) -> {
                    assertFalse(status.isError());
                    assertEquals(PNOperationType.PNGetMessageActions, status.getOperation());
                    success.set(true);
                });

        listen(success);
    }

    @Test
    public void testDeleteMessageAction() throws PubNubException {
        final AtomicBoolean success = new AtomicBoolean();

        final String expectedValue = UUID.randomUUID().toString();

        final PNAddMessageActionResult addMessageActionResult = pubNub.addMessageAction()
                .channel(expectedChannel)
                .messageAction(new PNMessageAction()
                        .setType("reaction")
                        .setValue(expectedValue)
                        .setMessageTimetoken(publishResult.getTimetoken()))
                .sync();

        assert addMessageActionResult != null;
        pubNub.removeMessageAction()
                .messageTimetoken(publishResult.getTimetoken())
                .actionTimetoken(addMessageActionResult.getActionTimetoken())
                .channel(expectedChannel)
                .async((result, status) -> {
                    assertFalse(status.isError());
                    assertEquals(PNOperationType.PNDeleteMessageAction, status.getOperation());
                    success.set(true);
                });

        listen(success);
    }

    @Test
    public void testAddGetMessageAction() throws PubNubException {
        final AtomicBoolean success = new AtomicBoolean();

        final String expectedValue = UUID.randomUUID().toString();

        final PNAddMessageActionResult addMessageActionResult = pubNub.addMessageAction()
                .channel(expectedChannel)
                .messageAction(new PNMessageAction()
                        .setType("REACTION")
                        .setValue(expectedValue)
                        .setMessageTimetoken(publishResult.getTimetoken()))
                .sync();

        pubNub.getMessageActions()
                .channel(expectedChannel)
                .async((result, status) -> {
                    assertFalse(status.isError());
                    assertEquals(PNOperationType.PNGetMessageActions, status.getOperation());
                    assert result != null;
                    result.getActions().forEach(pnAction -> {
                                assert addMessageActionResult != null;
                                if (pnAction.getActionTimetoken().equals(addMessageActionResult.getActionTimetoken())) {
                                    success.set(true);
                                }
                            }
                    );
                });

        listen(success);
    }

    @Test
    public void testAddGetMessageAction_Bulk() {
        final AtomicBoolean success = new AtomicBoolean();

        final int expectedMessageCount = 10;
        final String expectedChannel = random();

        publishMixed(pubNub, expectedMessageCount, expectedChannel).forEach(pnPublishResult -> {
            try {
                pubNub.addMessageAction()
                        .channel(expectedChannel)
                        .messageAction(new PNMessageAction()
                                .setType("reaction")
                                .setValue(RandomGenerator.emoji())
                                .setMessageTimetoken(pnPublishResult.getTimetoken()))
                        .sync();
            } catch (PubNubException e) {
                e.printStackTrace();
            }
        });

        pubNub.getMessageActions()
                .channel(expectedChannel)
                .async((result, status) -> {
                    assertFalse(status.isError());
                    assertEquals(PNOperationType.PNGetMessageActions, status.getOperation());
                    assert result != null;
                    assertEquals(expectedMessageCount, result.getActions().size());
                    success.set(true);
                });

        listen(success);
    }

    @Test
    public void testAddGetMessageAction_Bulk_Pagination() throws PubNubException, InterruptedException {
        final String expectedChannelName = random();

        final int messageCount = 10;

        final List<PNPublishResult> messages = publishMixed(pubNub, messageCount, expectedChannelName);

        assertEquals(10, messages.size());

        for (int i = 0; i < messages.size(); i++) {
            pause((int) Durations.ONE_HUNDRED_MILLISECONDS.getSeconds());
            pubNub.addMessageAction()
                    .channel(expectedChannelName)
                    .messageAction(new PNMessageAction()
                            .setType("reaction")
                            .setValue((i + 1) + "_" + RandomGenerator.emoji())
                            .setMessageTimetoken(messages.get(i).getTimetoken()))
                    .sync();
        }

        final AtomicBoolean success = new AtomicBoolean();
        final AtomicInteger count = new AtomicInteger(0);

        page(expectedChannelName, System.currentTimeMillis() * 10_000L, new Callback() {
            @Override
            public void onMore(List<PNMessageAction> actions) {
                count.set(count.get() + actions.size());
            }

            @Override
            public void onDone() {
                log.error(String.format("onDone %s", count.get()));
                success.set(count.get() == messageCount);
            }
        });

        listen(success);
    }

    void page(String channel, Long start, Callback callback) {
        pubNub.getMessageActions()
                .channel(channel)
                .start(start)
                .limit(5)
                .async((result, status) -> {
                    assert result != null;
                    if (!status.isError() && !result.getActions().isEmpty()) {
                        callback.onMore(result.getActions());
                        page(channel, result.getActions().get(0).getActionTimetoken(), callback);
                    } else {
                        callback.onDone();
                    }
                });
    }

    interface Callback {
        void onMore(List<PNMessageAction> actions);

        void onDone();
    }

    @Test
    public void loopActions() throws PubNubException {
        final List<Long> s1 = new ArrayList<>();
        s1.add(1L);
        s1.add(2L);
        s1.add(3L);
        s1.add(4L);

        final List<Long> s2 = new ArrayList<>();
        s2.add(3L);
        s2.add(4L);
        s2.add(1L);
        s2.add(2L);

        final List<Long> s3 = new ArrayList<>();
        s3.add(4L);
        s3.add(3L);
        s3.add(2L);
        s3.add(1L);

        assertTrue(isSorted(s1));
        assertFalse(isSorted(s2));
        assertTrue(isSorted(s3));

        final List<PNPublishResult> publishList = new ArrayList<>();

        for (int i = 0; i < 11; i++) {
            publishList.add(publish(pubNub, expectedChannel, i + 1));
        }

        for (int i = 0; i < publishList.size(); i++) {
            pubNub.addMessageAction()
                    .channel(expectedChannel)
                    .messageAction(new PNMessageAction()
                            .setType("REACTION")
                            .setValue((i + 1) + "_" + RandomGenerator.newValue(5))
                            .setMessageTimetoken(publishList.get(i).getTimetoken()))
                    .sync();
        }

        final int[] size = {0};

        final List<PNMessageAction> pnActionList = new ArrayList<>();

        pageActions(3, expectedChannel, null, new Callback() {
            @Override
            public void onMore(List<PNMessageAction> actions) {
                size[0] += actions.size();
                System.out.println("Moreee " + actions.size() + "/" + size[0]);

                final List<PNMessageAction> temp = new ArrayList<>(actions);
                actions.forEach(pnAction -> {
                    System.out.print(parseDate(pnAction.getActionTimetoken()));
                    System.out.println(" – " + pnAction.getValue());
                });

                Collections.reverse(temp);

                pnActionList.addAll(temp);


            }

            @Override
            public void onDone() {
                System.out.println("Done!");

                pnActionList.forEach(pnAction -> {
                    System.out.print(parseDate(pnAction.getActionTimetoken()));
                    System.out.println(" – " + pnAction.getValue());
                });

                final List<Long> tts = new ArrayList<>();
                pnActionList.forEach(pnAction -> tts.add(pnAction.getActionTimetoken()));

                assertTrue(isSorted(tts));
            }
        });
    }

    public void pageActions(int chunk, String channel, Long start, Callback callback) throws PubNubException {
        final GetMessageActions builder = pubNub.getMessageActions()
                .limit(chunk)
                .channel(channel);

        if (start != null) {
            builder.start(start);
        }

        final PNGetMessageActionsResult messageActionsResult = builder.sync();

        assert messageActionsResult != null;
        if (!messageActionsResult.getActions().isEmpty()) {
            callback.onMore(messageActionsResult.getActions());
            pageActions(chunk, channel, messageActionsResult.getActions().get(0).getActionTimetoken(), callback);
        } else {
            callback.onDone();
        }
    }


    @Test
    public void testFetchHistory() throws PubNubException {
        final String expectedChannel = random();
        final int expectedMessageCount = 10;

        final List<PNPublishResult> publishResultList = publishMixed(pubNub, expectedMessageCount, expectedChannel);

        for (int i = 0; i < publishResultList.size(); i++) {
            if (i % 2 == 0 && i % 3 == 0) {
                pubNub.addMessageAction()
                        .channel(expectedChannel)
                        .messageAction(new PNMessageAction()
                                .setType("receipt")
                                .setValue(RandomGenerator.emoji())
                                .setMessageTimetoken(publishResultList.get(i).getTimetoken()))
                        .sync();
            }
            if (i % 3 == 0) {
                pubNub.addMessageAction()
                        .channel(expectedChannel)
                        .messageAction(new PNMessageAction()
                                .setType("receipt")
                                .setValue(RandomGenerator.emoji())
                                .setMessageTimetoken(publishResultList.get(i).getTimetoken()))
                        .sync();
            }
            if (i % 2 == 0) {
                pubNub.addMessageAction()
                        .channel(expectedChannel)
                        .messageAction(new PNMessageAction()
                                .setType("receipt")
                                .setValue(RandomGenerator.emoji())
                                .setMessageTimetoken(publishResultList.get(i).getTimetoken()))
                        .sync();
            }
            if (i % 5 == 0) {
                pubNub.addMessageAction()
                        .channel(expectedChannel)
                        .messageAction(new PNMessageAction()
                                .setType("fiver")
                                .setValue(RandomGenerator.emoji())
                                .setMessageTimetoken(publishResultList.get(i).getTimetoken()))
                        .sync();
            }
        }

        final PNFetchMessagesResult fetchMessagesResultWithActions = pubNub.fetchMessages()
                .channels(Collections.singletonList(expectedChannel))
                .includeMeta(true)
                .includeMessageActions(true)
                .sync();

        assert fetchMessagesResultWithActions != null;
        fetchMessagesResultWithActions.getChannels().forEach((channel, item) -> {
            System.out.println("Channel: " + channel + ". Messages: " + item.size());
            item.forEach(pnFetchMessageItem -> {
                System.out.println("\tMessage: " + pnFetchMessageItem.getMessage());
                System.out.println("\tTimetoken: " + pnFetchMessageItem.getTimetoken());
                System.out.println("\tMeta: " + pnFetchMessageItem.getMeta());
                if (pnFetchMessageItem.getActions() == null) {
                    System.out.println("\t\tNo actions here.");
                    return;
                }
                System.out.println("\t\tTotal action types: " + pnFetchMessageItem.getActions().size());
                pnFetchMessageItem.getActions().forEach((type, map) -> {
                    System.out.println("\t\t\tAction type: " + type);
                    map.forEach((value, actions) -> {
                        System.out.println("\t\t\t\tAction value: " + value);
                        actions.forEach(action -> {
                            System.out.println("\t\t\t\tAction uuid: " + action.getUuid());
                            System.out.println("\t\t\t\tAction timetoken: " + action.getActionTimetoken());
                        });
                    });
                });
                System.out.println("--------------------");
            });
        });

        fetchMessagesResultWithActions.getChannels().forEach((s, pnFetchMessageItems) -> {
            pnFetchMessageItems.forEach(pnFetchMessageItem -> {
                assertNotNull(pnFetchMessageItem.getActions());
            });
        });

        final PNFetchMessagesResult fetchMessagesResultNoActions = pubNub.fetchMessages()
                .channels(Collections.singletonList(expectedChannel))
                .sync();

        assert fetchMessagesResultNoActions != null;
        fetchMessagesResultNoActions.getChannels().forEach((s, pnFetchMessageItems) -> {
            pnFetchMessageItems.forEach(pnFetchMessageItem -> {
                assertNull(pnFetchMessageItem.getActions());
            });
        });
    }

    @Test
    public void testActionReceive() throws PubNubException {
        final String expectedChannelName = randomChannel();

        final int expectedMessageCount = 2;

        final List<PNPublishResult> publishResultList = new ArrayList<>();

        for (int i = 1; i <= expectedMessageCount; i++) {
            final PNPublishResult pnPublishResult = pubNub.publish()
                    .channel(expectedChannelName)
                    .message(i + "_msg")
                    .meta(i % 2 == 0 ? generateMap() : null)
                    .shouldStore(true)
                    .sync();
            publishResultList.add(pnPublishResult);
        }

        assertEquals(expectedMessageCount, publishResultList.size());

        final AtomicInteger actionsCount = new AtomicInteger();

        pubNub.addListener(new SubscribeCallback() {
            @Override
            public void file(@NotNull PubNub pubnub, @NotNull PNFileEventResult pnFileEventResult) {

            }
            @Override
            public void status(@NotNull PubNub pubnub, @NotNull PNStatus pnStatus) {
                if (pnStatus.getCategory() == PNStatusCategory.PNConnectedCategory) {
                    if (pnStatus.getOperation() == PNOperationType.PNSubscribeOperation) {
                        for (PNPublishResult pnPublishResult : publishResultList) {
                            try {
                                pubNub.addMessageAction()
                                        .channel(expectedChannelName)
                                        .messageAction(new PNMessageAction()
                                                .setType("reaction")
                                                .setValue(RandomGenerator.emoji())
                                                .setMessageTimetoken(pnPublishResult.getTimetoken()))
                                        .sync();
                            } catch (PubNubException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            @Override
            public void message(@NotNull PubNub pubnub, @NotNull PNMessageResult pnMessageResult) {
                fail();
            }

            @Override
            public void presence(@NotNull PubNub pubnub, @NotNull PNPresenceEventResult pnPresenceEventResult) {

            }

            @Override
            public void signal(@NotNull PubNub pubnub, @NotNull PNSignalResult pnSignalResult) {
                fail();
            }

            @Override
            public void uuid(@NotNull final PubNub pubnub, @NotNull final PNUUIDMetadataResult pnUUIDMetadataResult) {
                fail();
            }

            @Override
            public void channel(@NotNull final PubNub pubnub, @NotNull final PNChannelMetadataResult pnChannelMetadataResult) {
                fail();
            }

            @Override
            public void membership(@NotNull final PubNub pubnub, @NotNull final PNMembershipResult pnMembershipResult) {
                fail();
            }

            @Override
            public void messageAction(@NotNull PubNub pubnub, @NotNull PNMessageActionResult pnActionResult) {
                assertEquals(expectedChannelName, pnActionResult.getChannel());
                actionsCount.incrementAndGet();
            }
        });

        pubNub.subscribe()
                .channels(Collections.singletonList(expectedChannelName))
                .withPresence()
                .execute();

        Awaitility.await()
                .atMost(Durations.TEN_SECONDS)
                .untilAtomic(actionsCount, IsEqual.equalTo(expectedMessageCount));


    }

    @Test
    public void testAddAction_NoChannel() {
        try {
            pubNub.addMessageAction()
                    .sync();
        } catch (PubNubException e) {
            assertException(PNERROBJ_CHANNEL_MISSING, e);
        }
    }

    @Test
    public void testAddAction_NoMessageActionObject() {
        try {
            pubNub.addMessageAction()
                    .channel(random())
                    .sync();
        } catch (PubNubException e) {
            assertException(PNERROBJ_MESSAGE_ACTION_MISSING, e);
        }
    }

    @Test
    public void testAddAction_NoMessageTimeToken() {
        try {
            pubNub.addMessageAction()
                    .channel(random())
                    .messageAction(new PNMessageAction()
                            .setType(random())
                            .setValue(random()))
                    .sync();
        } catch (PubNubException e) {
            assertException(PNERROBJ_MESSAGE_TIMETOKEN_MISSING, e);
        }
    }

    @Test
    public void testAddAction_NoMessageActionType() {
        try {
            pubNub.addMessageAction()
                    .channel(random())
                    .messageAction(new PNMessageAction()
                            .setValue(random())
                            .setMessageTimetoken(1L)
                    )
                    .sync();
        } catch (PubNubException e) {
            assertException(PNERROBJ_MESSAGE_ACTION_TYPE_MISSING, e);
        }
    }

    @Test
    public void testAddAction_NoMessageActionValue() {
        try {
            pubNub.addMessageAction()
                    .channel(random())
                    .messageAction(new PNMessageAction()
                            .setType(random())
                            .setMessageTimetoken(1L)
                    )
                    .sync();
        } catch (PubNubException e) {
            assertException(PNERROBJ_MESSAGE_ACTION_VALUE_MISSING, e);
        }
    }

    @Test
    public void testGetActions_NoChannel() {
        try {
            pubNub.getMessageActions()
                    .sync();
        } catch (PubNubException e) {
            assertException(PNERROBJ_CHANNEL_MISSING, e);
        }
    }

    @Test
    public void testRemoveAction_NoChannel() {
        try {
            pubNub.removeMessageAction()
                    .sync();
        } catch (PubNubException e) {
            assertException(PNERROBJ_CHANNEL_MISSING, e);
        }
    }

    @Test
    public void testRemoveAction_NoMessageTimeToken() {
        try {
            pubNub.removeMessageAction()
                    .channel(random())
                    .actionTimetoken(1L)
                    .sync();
        } catch (PubNubException e) {
            assertException(PNERROBJ_MESSAGE_TIMETOKEN_MISSING, e);
        }
    }

    @Test
    public void testRemoveAction_NoMessageActionTimeToken() {
        try {
            pubNub.removeMessageAction()
                    .channel(random())
                    .messageTimetoken(1L)
                    .sync();
        } catch (PubNubException e) {
            assertException(PNERROBJ_MESSAGE_ACTION_TIMETOKEN_MISSING, e);
        }
    }

    @Test
    public void testAddSameActionTwice() throws PubNubException {
        final String expectedChannel = random();
        final String expectedEmoji = RandomGenerator.emoji();

        final PNPublishResult pnPublishResult = pubNub.publish()
                .channel(expectedChannel)
                .message(random())
                .shouldStore(true)
                .sync();

        assert pnPublishResult != null;
        pubNub.addMessageAction()
                .channel(expectedChannel)
                .messageAction(new PNMessageAction()
                        .setType("reaction")
                        .setValue(expectedEmoji)
                        .setMessageTimetoken(pnPublishResult.getTimetoken())
                )
                .sync();

        final AtomicBoolean success = new AtomicBoolean();

        pubNub.addMessageAction()
                .channel(expectedChannel)
                .messageAction(new PNMessageAction()
                        .setType("reaction")
                        .setValue(expectedEmoji)
                        .setMessageTimetoken(pnPublishResult.getTimetoken())
                )
                .async((result, status) -> {
                    assertTrue(status.isError());
                    assertEquals(409, status.getStatusCode());
                    success.set(true);

                });

        listen(success);
    }
}
