package com.pubnub.api.java.models.consumer.objects_api;

import com.pubnub.api.utils.PatchValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

@Getter
@Accessors(chain = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class PNObject {

    @EqualsAndHashCode.Include
    protected String id;

    @Setter
    protected PatchValue<@Nullable Object> custom;

    @Setter
    protected PatchValue<@Nullable String> updated;

    @Setter
    protected PatchValue<@Nullable String> eTag;

    protected PNObject(String id) {
        this.id = id;
    }

    protected PNObject() {
    }
}


