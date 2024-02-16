package com.pubnub.api.endpoints.objects_api.utils;

import com.pubnub.api.endpoints.BuilderSteps;
import com.pubnub.api.models.consumer.objects_api.member.PNUUID;
import com.pubnub.api.models.consumer.objects_api.membership.PNChannelMembership;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface ObjectsBuilderSteps extends BuilderSteps {
    interface ChannelMembershipsStep<T> {
        T channelMemberships(@NotNull Collection<PNChannelMembership> channelMemberships);
    }

    interface UUIDsStep<T> {
        T uuids(@NotNull Collection<PNUUID> uuids);
    }

    interface RemoveOrSetStep<T, E> {
        RemoveStep<T, E> set(Collection<E> entitiesToSet);

        SetStep<T, E> remove(Collection<E> entitiesToRemove);

        interface RemoveStep<T, E> {
            T remove(Collection<E> entitiesToRemove);
        }

        interface SetStep<T, E> {
            T set(Collection<E> entitiesToSet);
        }
    }
}
