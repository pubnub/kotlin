package com.pubnub.api.models.consumer.objects_api.member;

import com.google.gson.annotations.JsonAdapter;
import com.pubnub.api.models.consumer.objects_api.util.CustomPayloadJsonInterceptor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public abstract class PNUUID {
    @AllArgsConstructor
    @EqualsAndHashCode
    @Getter
    public static class UUIDId {
        private String id;
    }

    @Getter
    private final UUIDId uuid;

    public static PNUUID uuid(final String uuid) {
        return new JustUUID(new UUIDId(uuid));
    }
    public static PNUUID uuidWithCustom(final String uuid, final Map<String, Object> custom) {
        return new UUIDWithCustom(new UUIDId(uuid), new HashMap<>(custom));
    }

    @Getter
    @EqualsAndHashCode(callSuper = true)
    public static class JustUUID extends PNUUID {
        JustUUID(UUIDId uuid) {
            super(uuid);
        }
    }

    @Getter
    @EqualsAndHashCode(callSuper = true)
    public static class UUIDWithCustom extends PNUUID {
        @JsonAdapter(CustomPayloadJsonInterceptor.class)
        private final Object custom;

        UUIDWithCustom(UUIDId uuid, Object custom) {
            super(uuid);
            this.custom = custom;
        }
    }
}
