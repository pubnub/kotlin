package com.pubnub.api.java.models.consumer.objects_api.member;

import com.pubnub.api.java.models.consumer.objects_api.uuid.PNUUIDMetadata;
import com.pubnub.api.models.consumer.objects.member.PNMember;
import com.pubnub.api.utils.PatchValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class PNMembers {
    private PNUUIDMetadata uuid;

    protected PatchValue<@Nullable Map<String, Object>> custom;

    protected String updated;
    protected String eTag;
    protected PatchValue<@Nullable String> status;

    @Nullable
    public static PNMembers from(@Nullable PNMember member) {
        if (member == null) {
            return null;
        }
        return new PNMembers()
                .setUuid(PNUUIDMetadata.from(member.getUuid()))
                .setCustom(member.getCustom())
                .setUpdated(member.getUpdated())
                .setETag(member.getETag())
                .setStatus(member.getStatus());
    }

    public static List<PNMembers> from(Collection<PNMember> members) {
        ArrayList<PNMembers> list = new ArrayList<>(members.size());
        for (PNMember member : members) {
            list.add(from(member));
        }
        return list;
    }
}
