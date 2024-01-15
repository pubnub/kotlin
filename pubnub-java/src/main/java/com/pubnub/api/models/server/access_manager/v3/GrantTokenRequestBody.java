package com.pubnub.api.models.server.access_manager.v3;

import com.pubnub.api.PubNubException;
import com.pubnub.api.models.TokenBitmask;
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant;
import com.pubnub.api.models.consumer.access_manager.v3.ChannelGroupGrant;
import com.pubnub.api.models.consumer.access_manager.v3.PNResource;
import com.pubnub.api.models.consumer.access_manager.v3.UUIDGrant;
import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class GrantTokenRequestBody {
    private final Integer ttl;
    private final GrantTokenPermissions permissions;

    @Data
    private static class GrantTokenPermissions {
        private final GrantTokenPermission resources;
        private final GrantTokenPermission patterns;
        private final Object meta;
        private final String uuid;
    }

    @Data
    public static class GrantTokenPermission {
        private final Map<String, Integer> channels;
        private final Map<String, Integer> groups;
        private final Map<String, Integer> uuids;
        private final Map<String, Integer> spaces = Collections.emptyMap();
        private final Map<String, Integer> users = Collections.emptyMap();
    }

    @Builder
    public static GrantTokenRequestBody of(Integer ttl,
                                           List<ChannelGrant> channels,
                                           List<ChannelGroupGrant> groups,
                                           List<UUIDGrant> uuids,
                                           Object meta,
                                           String uuid) throws PubNubException {

        GrantTokenPermission resources = new GrantTokenPermission(getResources(channels),
                getResources(groups), getResources(uuids));
        GrantTokenPermission patterns = new GrantTokenPermission(getPatterns(channels),
                getPatterns(groups), getPatterns(uuids));
        GrantTokenPermissions permissions = new GrantTokenPermissions(resources, patterns, meta == null ? Collections.emptyMap() : meta, uuid);
        return new GrantTokenRequestBody(ttl, permissions);
    }

    private static <T extends PNResource<?>> Map<String, Integer> getResources(List<T> resources) throws PubNubException {
        final Map<String, Integer> result = new HashMap<>();
        for (T resource : resources) {
            if (!resource.isPatternResource()) {
                result.put(resource.getId(), calculateBitmask(resource));
            }
        }
        return result;
    }

    private static <T extends PNResource<?>> Map<String, Integer> getPatterns(List<T> resources) throws PubNubException {
        final Map<String, Integer> result = new HashMap<>();
        for (T resource : resources) {
            if (resource.isPatternResource()) {
                result.put(resource.getId(), calculateBitmask(resource));
            }
        }
        return result;

    }

    private static int calculateBitmask(PNResource<?> resource) throws PubNubException {
        int sum = 0;
        if (resource.isRead()) {
            sum |= TokenBitmask.READ;
        }
        if (resource.isWrite()) {
            sum |= TokenBitmask.WRITE;
        }
        if (resource.isManage()) {
            sum |= TokenBitmask.MANAGE;
        }
        if (resource.isDelete()) {
            sum |= TokenBitmask.DELETE;
        }
        if (resource.isCreate()) {
            sum |= TokenBitmask.CREATE;
        }
        if (resource.isGet()) {
            sum |= TokenBitmask.GET;
        }
        if (resource.isJoin()) {
            sum |= TokenBitmask.JOIN;
        }
        if (resource.isUpdate()) {
            sum |= TokenBitmask.UPDATE;
        }
        return sum;
    }
}
