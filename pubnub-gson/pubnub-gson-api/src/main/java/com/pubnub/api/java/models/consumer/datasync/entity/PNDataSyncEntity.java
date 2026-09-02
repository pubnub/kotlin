package com.pubnub.api.java.models.consumer.datasync.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * DataSync Entity resource returned by {@code pubnub.dataSync().getEntity()} / {@code createEntity()}.
 *
 * <p>An Entity has no default class: the caller always supplies its {@code className}.
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@ToString
public class PNDataSyncEntity {
    /**
     * Entity identifier.
     */
    private String id;

    /**
     * Entity class identifier.
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
     * Date and time the entity was created.
     */
    private String createdAt;

    /**
     * Date and time the entity was last updated.
     */
    private String updatedAt;

    /**
     * The entity's content fingerprint used in conditional requests.
     */
    private String eTag;

    /**
     * Entity status.
     */
    @Nullable
    private String status;

    /**
     * Date and time when the entity expires (will be deleted automatically). Always present —
     * server-computed from the entity class TTL and never client-settable.
     */
    private String expiresAt;

    /**
     * Arbitrary user-defined JSON object.
     */
    @Nullable
    private Map<String, Object> payload;
}
