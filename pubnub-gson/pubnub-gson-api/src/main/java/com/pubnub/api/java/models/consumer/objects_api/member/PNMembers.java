package com.pubnub.api.java.models.consumer.objects_api.member;

import com.pubnub.api.java.models.consumer.objects_api.uuid.PNUUIDMetadata;
import com.pubnub.api.utils.PatchValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class PNMembers { // todo why this is plural? Shouldn't be PNMember?
    private PNUUIDMetadata uuid;

    protected PatchValue<@Nullable Map<String, Object>> custom;

    protected String updated;
    protected String eTag;
    protected PatchValue<@Nullable String> status;
    protected PatchValue<@Nullable String> type;
}
