package com.pubnub.api.java.models.consumer.datasync.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

/**
 * A single JSON Patch (RFC-6902) operation used by {@code pubnub.dataSync().entity().patch()}.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@ToString
public class PNJsonPatchOperation {
    /**
     * The operation to perform (e.g. {@code add}, {@code remove}, {@code replace}, {@code move}, {@code copy}, {@code test}).
     */
    private String op;

    /**
     * JSON Pointer (RFC-6901) to the target location, e.g. {@code /status} or {@code /payload/role}.
     */
    private String path;

    /**
     * The value to add, replace, or test. Required for {@code add}/{@code replace}/{@code test}.
     */
    @Nullable
    private Object value;

    /**
     * JSON Pointer to the source location. Required for {@code move}/{@code copy}.
     */
    @Nullable
    private String from;
}