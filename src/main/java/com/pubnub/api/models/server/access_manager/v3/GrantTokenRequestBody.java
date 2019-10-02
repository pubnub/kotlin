package com.pubnub.api.models.server.access_manager.v3;

import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.models.consumer.access_manager.v3.Channel;
import com.pubnub.api.models.consumer.access_manager.v3.Group;
import com.pubnub.api.models.consumer.access_manager.v3.PNResource;
import com.pubnub.api.models.consumer.access_manager.v3.Space;
import com.pubnub.api.models.consumer.access_manager.v3.User;
import lombok.Builder;
import lombok.extern.java.Log;

import java.util.List;

@Log
@Builder
public class GrantTokenRequestBody {

    private static final int READ = 1;
    private static final int WRITE = 2;
    private static final int MANAGE = 4;
    private static final int DELETE = 8;
    private static final int CREATE = 16;

    private Integer ttl;
    private List<Channel> channels;
    private List<Group> groups;
    private List<User> users;
    private List<Space> spaces;
    private Object meta;
    private PubNub pubNub;

    public JsonObject assemble() throws PubNubException {
        JsonObject payload = new JsonObject();

        payload.addProperty("ttl", this.ttl);

        JsonObject permissions = new JsonObject();

        JsonObject resources = new JsonObject();
        JsonObject patterns = new JsonObject();

        parse(channels, "channels", resources, patterns);
        parse(groups, "groups", resources, patterns);
        parse(users, "users", resources, patterns);
        parse(spaces, "spaces", resources, patterns);

        permissions.add("resources", resources);
        permissions.add("patterns", patterns);

        if (this.meta != null) {
            try {
                permissions.add("meta", pubNub.getMapper().convertValue(this.meta, JsonObject.class));
            } catch (PubNubException e) {
                throw PubNubException.builder()
                        .pubnubError(PubNubErrorBuilder.PNERROBJ_INVALID_META)
                        .build();
            }
        } else {
            permissions.add("meta", new JsonObject());
        }

        payload.add("permissions", permissions);

        return payload;
    }

    private void parse(List<? extends PNResource> list, String resourceSetName, JsonObject resources,
                       JsonObject patterns) throws PubNubException {
        if (list != null) {
            for (PNResource pnResource : list) {
                JsonObject resourceObject = new JsonObject();

                JsonObject determinedObject;

                if (pnResource.isPatternResource()) {
                    determinedObject = patterns;
                } else {
                    determinedObject = resources;
                }

                if (determinedObject.has(resourceSetName)) {
                    determinedObject.get(resourceSetName).getAsJsonObject()
                            .addProperty(pnResource.getId(), calculateBitmask(pnResource));
                } else {
                    resourceObject.addProperty(pnResource.getId(), calculateBitmask(pnResource));
                    determinedObject.add(resourceSetName, resourceObject);
                }
            }
        }

        if (!resources.has(resourceSetName)) {
            resources.add(resourceSetName, new JsonObject());
        }
        if (!patterns.has(resourceSetName)) {
            patterns.add(resourceSetName, new JsonObject());
        }
    }

    private int calculateBitmask(PNResource resource) throws PubNubException {
        int sum = 0;
        if (resource.isRead()) {
            sum += READ;
        }
        if (resource.isWrite()) {
            sum += WRITE;
        }
        if (resource.isManage()) {
            sum += MANAGE;
        }
        if (resource.isDelete()) {
            sum += DELETE;
        }
        if (resource.isCreate()) {
            sum += CREATE;
        }
        if (sum == 0) {
            throw PubNubException.builder()
                    .pubnubError(PubNubErrorBuilder.PNERROBJ_PERMISSION_MISSING)
                    .errormsg("No permissions specified for resource: ".concat(resource.getId()))
                    .build();
        }
        return sum;
    }
}
