package com.pubnub.docs.support;

import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.callbacks.EventListener;
import com.pubnub.api.models.consumer.pubsub.datasync.PNDataSyncEventResult;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import org.jetbrains.annotations.NotNull;

/**
 * Collects the DataSync events a Java client receives, the Java twin of {@code DataSyncKit.Events} (that one takes the Kotlin
 * client). Create it before the action and call {@link Events#await} after it.
 */
public final class DataSyncJavaKit {
    private DataSyncJavaKit() {}

    /** The DataSync events of one Java client. */
    public static final class Events implements EventListener, AutoCloseable {
        private final PubNub client;
        private final List<PNDataSyncEventResult> received = new CopyOnWriteArrayList<>();
        private final Object lock = new Object();

        public Events(PubNub client) {
            this.client = client;
            client.addListener(this);
        }

        @Override
        public void dataSync(@NotNull PubNub pubnub, @NotNull PNDataSyncEventResult result) {
            received.add(result);
            synchronized (lock) {
                lock.notifyAll();
            }
        }

        /** The first event that matches [test], or a failure that lists what arrived. */
        public PNDataSyncEventResult await(String what, long timeoutMs, Predicate<PNDataSyncEventResult> test) {
            long deadline = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(timeoutMs);
            while (true) {
                for (PNDataSyncEventResult event : received) {
                    if (test.test(event)) {
                        return event;
                    }
                }
                long left = TimeUnit.NANOSECONDS.toMillis(deadline - System.nanoTime());
                if (left <= 0) {
                    StringBuilder seen = new StringBuilder();
                    for (PNDataSyncEventResult event : received) {
                        seen.append(event.getChannel()).append(": ").append(event.getExtractedMessage().getType()).append("; ");
                    }
                    throw new AssertionError(what + " did not arrive within " + (timeoutMs / 1000.0) + "s. Events seen: "
                            + (seen.length() == 0 ? "none" : seen));
                }
                synchronized (lock) {
                    try {
                        lock.wait(Math.min(left, 500));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new AssertionError(what + " was interrupted");
                    }
                }
            }
        }

        @Override
        public void close() {
            client.removeListener(this);
        }
    }
}
