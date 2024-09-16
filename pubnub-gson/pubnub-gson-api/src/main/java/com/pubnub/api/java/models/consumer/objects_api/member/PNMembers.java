package com.pubnub.api.java.models.consumer.objects_api.member;

import com.pubnub.api.java.models.consumer.objects_api.uuid.PNUUIDMetadata;
import com.pubnub.api.models.consumer.objects.member.PNMember;
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

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class PNMembers {
    private PNUUIDMetadata uuid;

    protected Object custom;

    protected String updated;
    protected String eTag;
    protected String status;

    @Nullable
    public static PNMembers from(@Nullable PNMember member) {
        if (member == null) {
            return null;
        }
        return new PNMembers()
                .setUuid(PNUUIDMetadata.from(member.getUuid()))
                .setCustom(member.getCustom() != null ? member.getCustom().getValue() : null)
                .setUpdated(member.getUpdated())
                .setETag(member.getETag())
                .setStatus(member.getStatus() != null ? member.getStatus().getValue() : null);
    }

    public static List<PNMembers> from(Collection<PNMember> members) {
        ArrayList<PNMembers> list = new ArrayList<>(members.size());
        for (PNMember member : members) {
            list.add(from(member));
        }
        return list;
    }
}
