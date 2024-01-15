package com.pubnub.api.integration;

import com.pubnub.api.integration.util.BaseIntegrationTest;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;

import static com.pubnub.api.integration.util.Utils.random;
import static com.pubnub.api.integration.util.Utils.randomChannel;
import static org.junit.Assert.*;

public class GroupManagementIntegrationTests extends BaseIntegrationTest {

    private String mChannel1;
    private String mChannel2;
    private String mChannel3;

    private String mGroup;

    @Override
    protected void onBefore() {
        mChannel1 = randomChannel();
        mChannel2 = randomChannel();
        mChannel3 = randomChannel();
        mGroup = "cg_".concat(random());
    }

    @Override
    protected void onAfter() {

    }

    @Test
    public void testRemoveChannelsFromGroup() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        addChannelsToGroup();

        pubNub.removeChannelsFromChannelGroup()
                .channelGroup(mGroup)
                .channels(Arrays.asList(mChannel1, mChannel2, mChannel3))
                .async((result, status) -> {
                    assertFalse(status.isError());
                    assert status.getAffectedChannels() != null;
                    assertEquals(3, status.getAffectedChannels().size());
                    assertEquals(0, pubNub.getSubscribedChannels().size());
                    assert status.getAffectedChannelGroups() != null;
                    assertEquals(1, status.getAffectedChannelGroups().size());
                    assertEquals(0, pubNub.getSubscribedChannelGroups().size());
                    signal.countDown();
                });

        signal.await();
    }

    @Test
    public void testRemoveChannelFromGroup() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        addChannelsToGroup();

        pubNub.removeChannelsFromChannelGroup()
                .channelGroup(mGroup)
                .channels(Collections.singletonList(mChannel1))
                .async((result, status) -> {
                    assertFalse(status.isError());
                    signal.countDown();
                });

        signal.await();
    }

    @Test
    public void testSubscribeToChannelGroup() throws InterruptedException {
        addChannelsToGroup();
        subscribeToChannelGroup(pubNub, mGroup);

        boolean isGroupSubscribed = false;
        for (int i = 0; i < pubNub.getSubscribedChannelGroups().size(); i++) {
            if (pubNub.getSubscribedChannelGroups().get(i).equals(mGroup)) {
                isGroupSubscribed = true;
            }
        }

        assertTrue(isGroupSubscribed);
    }

    @Test
    public void testUnsubscribeFromChannelGroup() throws InterruptedException {
        addChannelsToGroup();
        subscribeToChannelGroup(pubNub, mGroup);

        pubNub.unsubscribe()
                .channelGroups(Collections.singletonList(mGroup))
                .execute();

        pause(1);

        assertEquals(0, pubNub.getSubscribedChannelGroups().size());
    }

    @Test
    public void testGetAllChannelsFromGroup() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        addChannelsToGroup();

        pubNub.listChannelsForChannelGroup()
                .channelGroup(mGroup)
                .async((result, status) -> {
                    assertFalse(status.isError());
                    assert result != null;
                    assertEquals(3, result.getChannels().size());
                    signal.countDown();
                });

        signal.await();
    }

    @Test
    public void testAddChannelsToGroup() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        pubNub.addChannelsToChannelGroup()
                .channelGroup(mGroup)
                .channels(Arrays.asList(mChannel1, mChannel2, mChannel3))
                .async((result, status) -> {
                    assertFalse(status.isError());
                    assert status.getAffectedChannelGroups() != null;
                    assertEquals(1, status.getAffectedChannelGroups().size());
                    assert status.getAffectedChannels() != null;
                    assertEquals(3, status.getAffectedChannels().size());
                    signal.countDown();
                });

        signal.await();
    }

    private void addChannelsToGroup() throws InterruptedException {
        final CountDownLatch signal = new CountDownLatch(1);

        pubNub.addChannelsToChannelGroup()
                .channelGroup(mGroup)
                .channels(Arrays.asList(mChannel1, mChannel2, mChannel3))
                .async((result, status) -> {
                    assertFalse(status.isError());
                    signal.countDown();
                });

        signal.await();
    }

}
