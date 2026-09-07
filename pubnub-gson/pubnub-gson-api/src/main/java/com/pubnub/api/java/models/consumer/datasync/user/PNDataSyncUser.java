package com.pubnub.api.java.models.consumer.datasync.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * DataSync User resource returned by {@code pubnub.dataSync().getUser()} / {@code createUser()}.
 *
 * <p>A User is a specialized DataSync entity whose class defaults to {@code User}. The built-in {@code User}
 * class is defined at the {@code GLOBAL} level and its filterable/sortable set is {@code name}.
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@ToString
public class PNDataSyncUser {
    /**
     * User identifier.
     */
    private String id;

    /**
     * Entity class identifier (a {@code User} subclass, defaults to {@code User}).
     */
    private String className;

    /**
     * Version of the entity class.
     */
    private int classVersion;

    /**
     * The level at which the entity class is defined (e.g. {@code SubKey} / {@code Global}).
     */
    @Nullable
    private String classLevel;

    /**
     * Date and time the user was created.
     */
    private String createdAt;

    /**
     * Date and time the user was last updated.
     */
    private String updatedAt;

    /**
     * The user's content fingerprint used in conditional requests.
     */
    private String eTag;

    /**
     * User status.
     */
    @Nullable
    private String status;

    /**
     * Date and time when the user expires (will be deleted automatically). Always present —
     * server-computed from the entity class TTL and never client-settable.
     */
    private String expiresAt;

    /**
     * Arbitrary user-defined JSON object.
     */
    @Nullable
    private Map<String, Object> payload;
}
