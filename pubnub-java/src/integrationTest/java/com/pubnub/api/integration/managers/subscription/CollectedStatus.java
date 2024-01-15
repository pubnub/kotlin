package com.pubnub.api.integration.managers.subscription;

import com.pubnub.api.models.consumer.PNStatus;

import java.time.Instant;
import java.util.Objects;

public class CollectedStatus {
    private final Instant timestamp;
    private final Exception exception;
    private final PNStatus pnStatus;

    public CollectedStatus(final PNStatus pnStatus, final Exception exception) {
        this.timestamp = Instant.now();
        this.pnStatus = pnStatus;
        this.exception = exception;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Exception getException() {
        return exception;
    }

    public StackTraceElement[] getStackTrace() {
        return exception.getStackTrace();
    }

    public PNStatus getPnStatus() {
        return pnStatus;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CollectedStatus that = (CollectedStatus) o;
        return timestamp.equals(that.timestamp) &&
                pnStatus.equals(that.pnStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, pnStatus);
    }
}
