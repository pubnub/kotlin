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
    private String id;
    private String className;
    private int classVersion;

    /**
     * The level at which the entity class is defined (e.g. {@code SubKey} / {@code Global}).
     */
    @Nullable
    private String classLevel;

    private String createdAt;
    private String updatedAt;
    private String eTag;

    @Nullable
    private String status;

    @Nullable
    private String expiresAt;

    @Nullable
    private Map<String, Object> payload;
}
