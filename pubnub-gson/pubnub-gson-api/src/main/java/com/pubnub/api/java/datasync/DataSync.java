package com.pubnub.api.java.datasync;

import com.pubnub.api.java.endpoints.datasync.channel.CreateChannel;
import com.pubnub.api.java.endpoints.datasync.channel.GetChannel;
import com.pubnub.api.java.endpoints.datasync.channel.GetChannels;
import com.pubnub.api.java.endpoints.datasync.channel.RemoveChannel;
import com.pubnub.api.java.endpoints.datasync.channel.SetChannel;
import com.pubnub.api.java.endpoints.datasync.channel.UpdateChannel;
import com.pubnub.api.java.endpoints.datasync.entity.CreateEntity;
import com.pubnub.api.java.endpoints.datasync.entity.GetEntities;
import com.pubnub.api.java.endpoints.datasync.entity.GetEntity;
import com.pubnub.api.java.endpoints.datasync.entity.UpdateEntity;
import com.pubnub.api.java.endpoints.datasync.entity.RemoveEntity;
import com.pubnub.api.java.endpoints.datasync.entity.SetEntity;
import com.pubnub.api.java.endpoints.datasync.user.CreateUser;
import com.pubnub.api.java.endpoints.datasync.user.GetUser;
import com.pubnub.api.java.endpoints.datasync.user.GetUsers;
import com.pubnub.api.java.endpoints.datasync.user.UpdateUser;
import com.pubnub.api.java.endpoints.datasync.user.RemoveUser;
import com.pubnub.api.java.endpoints.datasync.user.SetUser;
import com.pubnub.api.java.models.consumer.datasync.entity.PNJsonPatchOperation;

import java.util.List;

/**
 * Entry point for the DataSync API, reached via {@code pubnub.dataSync()}.
 */
public interface DataSync {
    /**
     * Get a DataSync entity by its id.
     *
     * @param entityId Identifier of the entity to fetch.
     */
    GetEntity getEntity(String entityId);

    /**
     * Create a DataSync entity. Optional fields are set via the returned builder.
     *
     * <p>The optional {@code classLevel} builder setter is create-only.
     *
     * @param className    Entity class identifier.
     * @param classVersion Version of the entity class.
     */
    CreateEntity createEntity(String className, int classVersion);

    /**
     * Remove a DataSync entity by its id.
     *
     * @param entityId Identifier of the entity to remove.
     */
    RemoveEntity removeEntity(String entityId);

    /**
     * List DataSync entities of a class. Optional filters/paging are set via the returned builder.
     *
     * <p>Filtering and sorting are only allowed on the entity class's properties whose filtering mode is not
     * disabled (i.e. those the class marks as filterable); using any other property in a {@code filter}/
     * {@code sort} returns a server error. An Entity has no built-in default class, so the filterable set is
     * whatever the supplied {@code className} declares.
     *
     * @param className Entity class identifier (required).
     */
    GetEntities getEntities(String className);

    /**
     * Partially update a DataSync entity via JSON Patch (RFC-6902). Optional {@code ifMatch} is set via the returned builder.
     *
     * @param entityId   Identifier of the entity to patch.
     * @param operations Non-empty list of JSON Patch operations to apply.
     */
    UpdateEntity updateEntity(String entityId, List<PNJsonPatchOperation> operations);

    /**
     * Replaces an entity in full. Optional fields are set via the returned builder.
     * <p>
     * Every mutable field is overwritten. Omitting {@code status} or {@code payload} clears the stored value rather
     * than preserving it, so a read-modify-write must send back every field it wants to keep. Use
     * {@link #updateEntity(String, List)} to change part of an entity.
     *
     * @param entityId     Identifier of the entity to replace.
     * @param classVersion Version of the class the payload conforms to.
     */
    SetEntity setEntity(String entityId, int classVersion);

    /**
     * Get a DataSync user by its id.
     *
     * <p>A User is a specialized DataSync entity of class {@code User}.
     *
     * @param userId Identifier of the user to fetch.
     */
    GetUser getUser(String userId);

    /**
     * Create a DataSync user. Optional fields are set via the returned builder.
     *
     * <p>The optional {@code classLevel} builder setter is create-only. The built-in {@code User} class is
     * defined at the {@code GLOBAL} class level.
     *
     * @param classVersion Version of the entity class.
     */
    CreateUser createUser(int classVersion);

    /**
     * Remove a DataSync user by its id.
     *
     * @param userId Identifier of the user to remove.
     */
    RemoveUser removeUser(String userId);

    /**
     * List DataSync users. Optional filters/paging are set via the returned builder.
     *
     * <p>Filtering and sorting are only allowed on the entity class's properties whose filtering mode is not
     * disabled (i.e. those the class marks as filterable); using any other property in a {@code filter}/
     * {@code sort} returns a server error. For the built-in {@code User} class this set is {@code name}
     * ({@code username} / {@code email} are custom-class properties, not built-in {@code User} fields). The
     * built-in {@code User} class is defined at the {@code GLOBAL} class level.
     */
    GetUsers getUsers();

    /**
     * Partially update a DataSync user via JSON Patch (RFC-6902). Optional {@code ifMatch} is set via the returned builder.
     *
     * @param userId     Identifier of the user to patch.
     * @param operations Non-empty list of JSON Patch operations to apply.
     */
    UpdateUser updateUser(String userId, List<PNJsonPatchOperation> operations);

    /**
     * Replaces a user in full. Optional fields are set via the returned builder.
     * <p>
     * Every mutable field is overwritten. Omitting {@code status} or {@code payload} clears the stored value rather
     * than preserving it, so a read-modify-write must send back every field it wants to keep. Use
     * {@link #updateUser(String, List)} to change part of a user.
     *
     * @param userId       Identifier of the user to replace.
     * @param classVersion Version of the class the payload conforms to.
     */
    SetUser setUser(String userId, int classVersion);

    /**
     * Get a DataSync channel by its id.
     *
     * <p>A Channel is a specialized DataSync entity of class {@code Channel}.
     *
     * @param channelId Identifier of the channel to fetch.
     */
    GetChannel getChannel(String channelId);

    /**
     * Create a DataSync channel. Optional fields are set via the returned builder.
     *
     * <p>The optional {@code classLevel} builder setter is create-only. The built-in {@code Channel} class is
     * defined at the {@code GLOBAL} class level.
     *
     * @param classVersion Version of the entity class.
     */
    CreateChannel createChannel(int classVersion);

    /**
     * Remove a DataSync channel by its id.
     *
     * @param channelId Identifier of the channel to remove.
     */
    RemoveChannel removeChannel(String channelId);

    /**
     * List DataSync channels. Optional filters/paging are set via the returned builder.
     *
     * <p>Filtering and sorting are only allowed on the entity class's properties whose filtering mode is not
     * disabled (i.e. those the class marks as filterable); using any other property in a {@code filter}/
     * {@code sort} returns a server error. For the default {@code Channel} class this set is {@code name} and
     * {@code type}, but a custom class or subclass may declare additional filterable properties. These are
     * filterable indexes over {@code /payload/name} and {@code /payload/type} — both nullable, not a required
     * or exclusive payload schema; the {@code payload} stays arbitrary JSON. The default {@code Channel} class
     * is defined at the {@code GLOBAL} class level.
     */
    GetChannels getChannels();

    /**
     * Partially update a DataSync channel via JSON Patch (RFC-6902). Optional {@code ifMatch} is set via the returned builder.
     *
     * @param channelId  Identifier of the channel to patch.
     * @param operations Non-empty list of JSON Patch operations to apply.
     */
    UpdateChannel updateChannel(String channelId, List<PNJsonPatchOperation> operations);

    /**
     * Replaces a channel in full. Optional fields are set via the returned builder.
     * <p>
     * Every mutable field is overwritten. Omitting {@code status} or {@code payload} clears the stored value rather
     * than preserving it, so a read-modify-write must send back every field it wants to keep. Use
     * {@link #updateChannel(String, List)} to change part of a channel.
     *
     * @param channelId    Identifier of the channel to replace.
     * @param classVersion Version of the class the payload conforms to.
     */
    SetChannel setChannel(String channelId, int classVersion);
}