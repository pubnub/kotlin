package com.pubnub.api.java.endpoints.datasync.user;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.models.consumer.datasync.user.PNGetUsersResult;
import org.jetbrains.annotations.Nullable;

/**
 * @see com.pubnub.api.java.datasync.DataSync#getUsers()
 */
public interface GetUsers extends Endpoint<PNGetUsersResult> {
    /**
     * Optional entity class identifier. When not set the whole User family is returned;
     * when set it narrows the results to that {@code User} subclass.
     */
    GetUsers entityClass(@Nullable String entityClass);

    /**
     * Optional entity class version. When not set the server uses the latest.
     */
    GetUsers entityClassVersion(@Nullable Integer entityClassVersion);

    /**
     * Optional level at which the entity class is defined (e.g. {@code SubKey} / {@code Global}).
     */
    GetUsers entityClassLevel(@Nullable String entityClassLevel);

    /**
     * Optional filter expression. Strongly consistent (always reflects the latest writes) but limited in the
     * number of conditionals per request — currently at most 10, which support may raise via keyset
     * configuration. A {@code filter} with more conditionals than allowed is rejected with an error; use
     * {@link #filterAdvanced(String)} for larger or more complex queries.
     */
    GetUsers filter(@Nullable String filter);

    /**
     * Optional advanced filter expression. Same syntax as {@link #filter(String)} but without the
     * conditional-count limit, at the cost of consistency: {@code filterAdvanced} is eventually consistent
     * (recent writes may not yet be reflected), whereas {@link #filter(String)} is strongly consistent.
     */
    GetUsers filterAdvanced(@Nullable String filterAdvanced);

    /**
     * Optional comma-separated sort fields, each optionally suffixed with a direction
     * ({@code :asc} or {@code :desc}, default {@code :asc}), e.g. {@code username:desc,email:asc}.
     */
    GetUsers sort(@Nullable String sort);

    /**
     * Optional page size (1–100, server default 20).
     */
    GetUsers limit(@Nullable Integer limit);

    /**
     * Optional opaque cursor for pagination (from a previous result's {@code next}).
     */
    GetUsers cursor(@Nullable String cursor);
}
