package com.pubnub.docs.support;

import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.java.PubNub;
import com.pubnub.api.java.v2.callbacks.EventListener;
import com.pubnub.api.java.v2.callbacks.StatusListener;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.pubnub.api.models.consumer.pubsub.PNSignalResult;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;

/**
 * The waits of the Kotlin support (`Wait`) for the Java client {@code com.pubnub.api.java.PubNub} of a Java sample. Arm the
 * wait before the action, act, then call {@link Expectation#await()}. The Kotlin waits take the Kotlin client, the second
 * clients of a Java test are Kotlin clients and use {@code Kit}.
 */
public final class JavaWait {
    private JavaWait() {}

    /** One armed wait. */
    public static final class Expectation implements AutoCloseable {
        private final PubNub pubnub;
        private final Observer observer;
        private final String what;
        private final Function<Observer, Object> test;
        private final CountDownLatch latch = new CountDownLatch(1);
        private volatile Object result;

        Expectation(PubNub pubnub, Observer observer, String what, Function<Observer, Object> test) {
            this.pubnub = pubnub;
            this.observer = observer;
            this.what = what;
            this.test = test;
            observer.onChange(this::check);
        }

        private void check() {
            if (latch.getCount() > 0) {
                Object found = test.apply(observer);
                if (found != null) {
                    result = found;
                    latch.countDown();
                }
            }
        }

        public Object await() {
            return await(15_000);
        }

        /** Returns the first matching event or status, or fails with what was seen. */
        public Object await(long timeoutMs) {
            check();
            try {
                if (!latch.await(timeoutMs, TimeUnit.MILLISECONDS)) {
                    throw new AssertionError(
                            Redact.scrub(what + " did not arrive within " + (timeoutMs / 1000.0) + "s. " + observer.describe()));
                }
                return result;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new AssertionError(what + " was interrupted");
            } finally {
                close();
            }
        }

        @Override
        public void close() {
            pubnub.removeListener(observer);
        }
    }

    /** Records every status and event of one client. */
    public static final class Observer implements EventListener, StatusListener {
        final List<PNStatus> statuses = new CopyOnWriteArrayList<>();
        final List<Object> events = new CopyOnWriteArrayList<>();
        private final List<Runnable> watchers = new CopyOnWriteArrayList<>();

        void onChange(Runnable watcher) {
            watchers.add(watcher);
        }

        private void changed() {
            watchers.forEach(Runnable::run);
        }

        @Override
        public void status(@NotNull PubNub pubnub, @NotNull PNStatus status) {
            statuses.add(status);
            changed();
        }

        @Override
        public void message(@NotNull PubNub pubnub, @NotNull PNMessageResult result) {
            events.add(result);
            changed();
        }

        @Override
        public void signal(@NotNull PubNub pubnub, @NotNull PNSignalResult result) {
            events.add(result);
            changed();
        }

        @Override
        public void presence(@NotNull PubNub pubnub, @NotNull PNPresenceEventResult result) {
            events.add(result);
            changed();
        }

        String describe() {
            List<String> categories = new ArrayList<>();
            for (PNStatus status : statuses) {
                categories.add(String.valueOf(status.getCategory()));
            }
            return "statuses seen: " + (categories.isEmpty() ? "none" : String.join(", ", categories)) + ". events seen: "
                    + events.size();
        }
    }

    private static Observer observe(PubNub pubnub) {
        Observer observer = new Observer();
        pubnub.addListener((EventListener) observer);
        pubnub.addListener((StatusListener) observer);
        return observer;
    }

    /** Arms a wait for a connected status that covers every channel and group. Register before subscribing. */
    public static Expectation connected(PubNub pubnub, Collection<String> channels, Collection<String> channelGroups) {
        return new Expectation(pubnub, observe(pubnub), "connected status for " + channels + " " + channelGroups, observer -> {
            Set<String> seenChannels = new HashSet<>();
            Set<String> seenGroups = new HashSet<>();
            PNStatus last = null;
            for (PNStatus status : observer.statuses) {
                if (status.getCategory() == PNStatusCategory.PNConnectedCategory
                        || status.getCategory() == PNStatusCategory.PNSubscriptionChanged) {
                    seenChannels.addAll(status.getAffectedChannels());
                    seenGroups.addAll(status.getAffectedChannelGroups());
                    last = status;
                }
            }
            return last != null && seenChannels.containsAll(channels) && seenGroups.containsAll(channelGroups) ? last : null;
        });
    }

    /** Arms a wait for a message on [channel] whose payload contains [contains]. */
    public static Expectation message(PubNub pubnub, String channel, String contains) {
        return new Expectation(pubnub, observe(pubnub), "message on " + channel, observer -> {
            for (Object event : observer.events) {
                if (event instanceof PNMessageResult) {
                    PNMessageResult message = (PNMessageResult) event;
                    if (channel.equals(message.getChannel()) && message.getMessage().toString().contains(contains)) {
                        return message;
                    }
                }
            }
            return null;
        });
    }

    /** Arms a wait for a signal on [channel] whose payload contains [contains]. */
    public static Expectation signal(PubNub pubnub, String channel, String contains) {
        return new Expectation(pubnub, observe(pubnub), "signal on " + channel, observer -> {
            for (Object event : observer.events) {
                if (event instanceof PNSignalResult) {
                    PNSignalResult signal = (PNSignalResult) event;
                    if (channel.equals(signal.getChannel()) && signal.getMessage().toString().contains(contains)) {
                        return signal;
                    }
                }
            }
            return null;
        });
    }

    /** Arms a wait for a presence event [action] on [channel] from anybody. */
    public static Expectation presenceEvent(PubNub pubnub, String action, String channel) {
        return new Expectation(pubnub, observe(pubnub), "presence " + action + " on " + channel, observer -> {
            for (Object event : observer.events) {
                if (event instanceof PNPresenceEventResult) {
                    PNPresenceEventResult presence = (PNPresenceEventResult) event;
                    if (action.equals(presence.getEvent()) && channel.equals(presence.getChannel())) {
                        return presence;
                    }
                }
            }
            return null;
        });
    }

    /** Fails when the armed event arrives within [millis]. Call it only after a control client proved that the service delivers. */
    public static void assertNothingArrives(Expectation expectation, String message, long millis) {
        Object seen;
        try {
            seen = expectation.await(millis);
        } catch (AssertionError e) {
            return;
        }
        throw new AssertionError(message + " (" + seen.getClass().getSimpleName() + ")");
    }

    /** Arms a wait for a presence event [action] of [user] on [channel]. */
    public static Expectation presence(PubNub pubnub, String action, String user, String channel) {
        return new Expectation(pubnub, observe(pubnub), "presence " + action + " of " + user + " on " + channel, observer -> {
            for (Object event : observer.events) {
                if (event instanceof PNPresenceEventResult) {
                    PNPresenceEventResult presence = (PNPresenceEventResult) event;
                    if (action.equals(presence.getEvent()) && user.equals(presence.getUuid()) && channel.equals(presence.getChannel())) {
                        return presence;
                    }
                }
            }
            return null;
        });
    }
}
