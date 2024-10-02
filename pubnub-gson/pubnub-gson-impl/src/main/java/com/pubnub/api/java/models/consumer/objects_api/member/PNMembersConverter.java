package com.pubnub.api.java.models.consumer.objects_api.member;

import com.pubnub.api.java.models.consumer.objects_api.uuid.PNUUIDMetadataConverter;
import com.pubnub.api.models.consumer.objects.member.PNMember;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface PNMembersConverter {
    @Nullable
    static PNMembers from(@Nullable PNMember member) {
        if (member == null) {
            return null;
        }
        return new PNMembers()
                .setUuid(PNUUIDMetadataConverter.from(member.getUuid()))
                .setCustom(member.getCustom())
                .setUpdated(member.getUpdated())
                .setETag(member.getETag())
                .setStatus(member.getStatus());
    }

    static List<PNMembers> from(Collection<PNMember> members) {
        ArrayList<PNMembers> list = new ArrayList<>(members.size());
        for (PNMember member : members) {
            list.add(from(member));
        }
        return list;
    }
}
