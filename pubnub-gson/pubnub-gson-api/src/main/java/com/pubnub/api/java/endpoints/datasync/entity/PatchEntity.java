package com.pubnub.api.java.endpoints.datasync.entity;

import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.java.models.consumer.datasync.entity.PNJsonPatchOperation;
import com.pubnub.api.java.models.consumer.datasync.entity.PNPatchEntityResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @see com.pubnub.api.java.datasync.EntityApi#patch(String, List)
 */
public interface PatchEntity extends Endpoint<PNPatchEntityResult> {
    /**
     * Optional eTag for optimistic concurrency ({@code If-Match} header).
     */
    PatchEntity ifMatch(@Nullable String ifMatch);
}