package com.pubnub.api.java.models.consumer.datasync.membership;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * DataSync Membership resource returned by {@code pubnub.dataSync().getMembership()} /
 * {@code createMembership()}.
 *
 * <p>A Membership is a specialized DataSync relationship linking a Channel (entity A) and a User (entity B),
 * re-exposed under the domain-friendly names {@code channelId} / {@code userId}.
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@ToString
public class PNDataSyncMembership {
    /**
     * Membership identifier.
     */
    private String id;

    /**
     * Identifier of the Channel (entity A) this membership links.
     */
    private String channelId;

    /**
     * Identifier of the User (entity B) this membership links.
     */
    private String userId;

    /**
     * Relationship class identifier (always {@code Membership}).
     */
    private String className;

    /**
     * Version of the relationship class.
     */
    private int classVersion;

    /**
     * Date and time the membership was created.
     */
    private String createdAt;

    /**
     * Date and time the membership was last updated.
     */
    private String updatedAt;

    /**
     * The membership's content fingerprint used in conditional requests.
     */
    private String eTag;

    /**
     * Membership status.
     */
    @Nullable
    private String status;

    /**
     * Date and time when the membership expires (will be deleted automatically). Always present:
     * server-computed on every response, never client-settable.
     */
    private String expiresAt;

    /**
     * Arbitrary user-defined JSON object.
     */
    @Nullable
    private Map<String, Object> payload;
}