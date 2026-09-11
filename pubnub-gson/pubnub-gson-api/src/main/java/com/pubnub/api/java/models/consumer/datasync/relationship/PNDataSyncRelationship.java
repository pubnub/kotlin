package com.pubnub.api.java.models.consumer.datasync.relationship;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * DataSync Relationship resource returned by {@code pubnub.dataSync().getRelationship()} /
 * {@code createRelationship()}.
 *
 * <p>A Relationship links two entities (entity A and entity B) under a relationship class. It is the general
 * counterpart to Membership, which is a fixed Channel↔User specialization.
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@ToString
public class PNDataSyncRelationship {
    /**
     * Relationship identifier.
     */
    private String id;

    /**
     * Identifier of entity A this relationship links.
     */
    private String entityAId;

    /**
     * Identifier of entity B this relationship links.
     */
    private String entityBId;

    /**
     * Relationship class identifier.
     */
    private String className;

    /**
     * Version of the relationship class.
     */
    private int classVersion;

    /**
     * Date and time the relationship was created.
     */
    private String createdAt;

    /**
     * Date and time the relationship was last updated.
     */
    private String updatedAt;

    /**
     * The relationship's content fingerprint used in conditional requests.
     */
    private String eTag;

    /**
     * Relationship status.
     */
    @Nullable
    private String status;

    /**
     * Date and time when the relationship expires (will be deleted automatically). Always present:
     * server-computed on every response, never client-settable.
     */
    private String expiresAt;

    /**
     * Arbitrary user-defined JSON object.
     */
    @Nullable
    private Map<String, Object> payload;
}
