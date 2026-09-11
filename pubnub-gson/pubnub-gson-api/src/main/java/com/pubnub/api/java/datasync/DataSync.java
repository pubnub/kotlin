package com.pubnub.api.java.datasync;

import com.pubnub.api.java.endpoints.datasync.channel.CreateChannel;
import com.pubnub.api.java.endpoints.datasync.channel.GetChannel;
import com.pubnub.api.java.endpoints.datasync.channel.GetChannels;
import com.pubnub.api.java.endpoints.datasync.channel.RemoveChannel;
import com.pubnub.api.java.endpoints.datasync.channel.SetChannel;
import com.pubnub.api.java.endpoints.datasync.channel.UpdateChannel;
import com.pubnub.api.java.endpoints.datasync.membership.CreateMembership;
import com.pubnub.api.java.endpoints.datasync.membership.GetMembership;
import com.pubnub.api.java.endpoints.datasync.membership.GetMemberships;
import com.pubnub.api.java.endpoints.datasync.membership.RemoveMembership;
import com.pubnub.api.java.endpoints.datasync.membership.SetMembership;
import com.pubnub.api.java.endpoints.datasync.membership.UpdateMembership;
import com.pubnub.api.java.endpoints.datasync.relationship.CreateRelationship;
import com.pubnub.api.java.endpoints.datasync.relationship.GetRelationship;
import com.pubnub.api.java.endpoints.datasync.relationship.GetRelationships;
import com.pubnub.api.java.endpoints.datasync.relationship.RemoveRelationship;
import com.pubnub.api.java.endpoints.datasync.relationship.SetRelationship;
import com.pubnub.api.java.endpoints.datasync.relationship.UpdateRelationship;
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
     * <p>Filtering and sorting are only allowed on properties the entity class marks as filterable via their
     * {@code filtering} mode ({@code none} / {@code simple} / {@code full}), which also determines which of
     * {@code filterFast} / {@code filter} / {@code sort} a property may be used with — see
     * {@link GetEntities#filterFast(String)}, {@link GetEntities#filter(String)}, and
     * {@link GetEntities#sort(List)} for the exact per-parameter rules. An Entity has no built-in default class,
     * so the filterable set is whatever the supplied {@code className} declares, plus the built-in fields
     * {@code id}, {@code createdAt}, {@code updatedAt}, and {@code status}, which behave as {@code full} and are
     * always filterable and sortable on any class.
     *
     * <p>Results are scoped to the caller's access token: only entities the token is permitted to read
     * ({@code get}) are returned. Entities the token cannot read are silently omitted — the call does not error
     * and does not return a {@code 403} for the un-readable entities. This token scoping is applied before
     * {@code filterFast} / {@code filter} / {@code sort}. When the PubNub instance is configured with a
     * secretKey (a trusted server-side deployment, never a client — the secretKey must not be shipped to clients),
     * or when using a token whose grants cover the whole result set, no permission-based filtering is applied and
     * all matching entities are
     * returned, subject only to {@code filterFast} / {@code filter} / {@code sort} and {@code limit} paging.
     *
     * @param className Entity class identifier.
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
     * <p>Filtering and sorting are only allowed on properties the entity class marks as filterable via their
     * {@code filtering} mode ({@code none} / {@code simple} / {@code full}), which also determines which of
     * {@code filterFast} / {@code filter} / {@code sort} a property may be used with — see
     * {@link GetUsers#filterFast(String)}, {@link GetUsers#filter(String)}, and {@link GetUsers#sort(List)} for
     * the exact per-parameter rules. For the built-in {@code User} class the filterable set is {@code name} and
     * {@code type}, and a subclass that extends {@code User} may declare additional filterable properties. These
     * are filterable indexes over {@code /payload/name} and {@code /payload/type} — both nullable, not a required
     * or exclusive payload schema; the {@code payload} stays arbitrary JSON. In addition, the built-in fields
     * {@code id}, {@code createdAt}, {@code updatedAt}, and {@code status} behave as {@code full} and are always
     * filterable and sortable on any class. The built-in {@code User} class is defined at the {@code GLOBAL}
     * class level.
     *
     * <p>Results are scoped to the caller's access token: only users the token is permitted to read
     * ({@code get}) are returned. Users the token cannot read are silently omitted — the call does not error
     * and does not return a {@code 403} for the un-readable users. This token scoping is applied before
     * {@code filterFast} / {@code filter} / {@code sort}. When the PubNub instance is configured with a
     * secretKey (a trusted server-side deployment, never a client — the secretKey must not be shipped to clients),
     * or when using a token whose grants cover the whole result set, no permission-based filtering is applied and
     * all matching users are
     * returned, subject only to {@code filterFast} / {@code filter} / {@code sort} and {@code limit} paging.
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
     * <p>Filtering and sorting are only allowed on properties the entity class marks as filterable via their
     * {@code filtering} mode ({@code none} / {@code simple} / {@code full}), which also determines which of
     * {@code filterFast} / {@code filter} / {@code sort} a property may be used with — see
     * {@link GetChannels#filterFast(String)}, {@link GetChannels#filter(String)}, and
     * {@link GetChannels#sort(List)} for the exact per-parameter rules. For the default {@code Channel} class
     * the filterable set is {@code name} and {@code type}, but a custom class or subclass may declare additional
     * filterable properties. These are filterable indexes over {@code /payload/name} and {@code /payload/type} —
     * both nullable, not a required or exclusive payload schema; the {@code payload} stays arbitrary JSON. In
     * addition, the built-in fields {@code id}, {@code createdAt}, {@code updatedAt}, and {@code status} behave
     * as {@code full} and are always filterable and sortable on any class. The default {@code Channel} class is
     * defined at the {@code GLOBAL} class level.
     *
     * <p>Results are scoped to the caller's access token: only channels the token is permitted to read
     * ({@code get}) are returned. Channels the token cannot read are silently omitted — the call does not error
     * and does not return a {@code 403} for the un-readable channels. This token scoping is applied before
     * {@code filterFast} / {@code filter} / {@code sort}. When the PubNub instance is configured with a
     * secretKey (a trusted server-side deployment, never a client — the secretKey must not be shipped to clients),
     * or when using a token whose grants cover the whole result set, no permission-based filtering is applied and
     * all matching channels are
     * returned, subject only to {@code filterFast} / {@code filter} / {@code sort} and {@code limit} paging.
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

    /**
     * Get a DataSync membership by its id.
     *
     * <p>A Membership is a specialized DataSync relationship linking a Channel and a User.
     *
     * @param membershipId Identifier of the membership to fetch.
     */
    GetMembership getMembership(String membershipId);

    /**
     * Create a DataSync membership linking a Channel and a User. Optional fields are set via the returned builder.
     *
     * <p>This API always targets the built-in {@code Membership} relationship class — there is no
     * class-name parameter; the SDK fixes the class to {@code Membership}. Creating a Membership <i>subclass</i>
     * is not supported here; that is served by the general {@code /relationships} API.
     *
     * @param channelId    Identifier of the Channel side of the membership. Must reference an existing Channel
     *                     entity — a missing or wrong-class entity is rejected.
     * @param userId       Identifier of the User side of the membership. Must reference an existing User
     *                     entity — a missing or wrong-class entity is rejected.
     * @param classVersion Version of the {@code Membership} class the payload conforms to. Currently only
     *                     version {@code 1} exists — pass {@code 1}. New versions may be introduced in the
     *                     future; when one is, opt in by passing its number.
     */
    CreateMembership createMembership(String channelId, String userId, int classVersion);

    /**
     * Remove a DataSync membership by its id. Optional {@code ifMatch} is set via the returned builder.
     *
     * @param membershipId Identifier of the membership to remove.
     */
    RemoveMembership removeMembership(String membershipId);

    /**
     * List DataSync memberships, optionally filtered by channel and/or user. Optional filters/paging are set via
     * the returned builder.
     *
     * <p>This API always queries the built-in {@code Membership} relationship class — there is no class-name
     * query, only {@code classVersion}. Querying a Membership <i>subclass</i> is not supported here; that is
     * served by the general {@code /relationships} API. See {@link GetMemberships#filterFast(String)},
     * {@link GetMemberships#filter(String)}, and {@link GetMemberships#sort(List)} for the exact per-parameter
     * rules. The built-in fields {@code id}, {@code createdAt}, {@code updatedAt}, and {@code status} are
     * filterable and sortable, except that {@code status} may be excluded when the class declares it as a
     * projected field and the token cannot fully reach it.
     *
     * <p>Results are scoped to the caller's access token: only memberships the token is permitted to read
     * ({@code get}) are returned. Memberships the token cannot read are silently omitted — the call does not error
     * and does not return a {@code 403} for the un-readable memberships. Token scoping and the
     * {@code filterFast} / {@code filter} / {@code sort} criteria are applied together, so the returned page
     * never contains a membership the token cannot read regardless of the other criteria. When the PubNub
     * instance is configured with a secretKey
     * (a trusted server-side deployment, never a client — the secretKey must not be shipped to clients), or when
     * using a token whose grants cover the whole result set, no permission-based filtering is applied and all
     * matching memberships are returned, subject only to {@code filterFast} / {@code filter} / {@code sort} and
     * {@code limit} paging.
     */
    GetMemberships getMemberships();

    /**
     * Partially update a DataSync membership via JSON Patch (RFC-6902). Optional {@code ifMatch} is set via the returned builder.
     *
     * @param membershipId Identifier of the membership to patch.
     * @param operations   Non-empty list of JSON Patch operations to apply.
     */
    UpdateMembership updateMembership(String membershipId, List<PNJsonPatchOperation> operations);

    /**
     * Replaces a membership in full. Optional fields are set via the returned builder.
     * <p>
     * Every mutable field is overwritten. Omitting {@code status} or {@code payload} clears the stored value rather
     * than preserving it, so a read-modify-write must send back every field it wants to keep. This cannot re-point
     * or reclassify a membership — the linked channel/user and the relationship class are immutable. Use
     * {@link #updateMembership(String, List)} to change part of a membership.
     *
     * @param membershipId Identifier of the membership to replace.
     * @param classVersion Version of the {@code Membership} class the payload conforms to. Currently only
     *                     version {@code 1} exists — pass {@code 1}. New versions may be introduced in the
     *                     future; when one is, opt in by passing its number.
     */
    SetMembership setMembership(String membershipId, int classVersion);

    /**
     * Get a DataSync relationship by its id.
     *
     * <p>A Relationship links two entities under a relationship class. It is the general counterpart to
     * Membership, which is a fixed Channel↔User specialization.
     *
     * @param relationshipId Identifier of the relationship to fetch.
     */
    GetRelationship getRelationship(String relationshipId);

    /**
     * Create a DataSync relationship linking two entities. Optional fields are set via the returned builder.
     *
     * @param entityAId    Identifier of entity A. Must reference an existing entity — a missing or
     *                     wrong-class entity is rejected.
     * @param entityBId    Identifier of entity B. Must reference an existing entity — a missing or
     *                     wrong-class entity is rejected.
     * @param className    Relationship class identifier.
     * @param classVersion Version of the relationship class the payload conforms to. Relationship classes are
     *                     versioned (an integer {@code >= 1}); pass the version of the class you are targeting.
     */
    CreateRelationship createRelationship(String entityAId, String entityBId, String className, int classVersion);

    /**
     * Remove a DataSync relationship by its id. Optional {@code ifMatch} is set via the returned builder.
     *
     * @param relationshipId Identifier of the relationship to remove.
     */
    RemoveRelationship removeRelationship(String relationshipId);

    /**
     * List DataSync relationships of a class, optionally filtered by entity A and/or entity B. Optional
     * filters/paging are set via the returned builder.
     *
     * <p>Filtering and sorting are only allowed on properties the relationship class marks as filterable via
     * their {@code filtering} mode ({@code none} / {@code simple} / {@code full}), which also determines which of
     * {@code filterFast} / {@code filter} / {@code sort} a property may be used with — see
     * {@link GetRelationships#filterFast(String)}, {@link GetRelationships#filter(String)}, and
     * {@link GetRelationships#sort(List)} for the exact per-parameter rules. The filterable set has no fixed
     * default; it is whatever the supplied {@code className} declares as filterable payload properties (indexes
     * over the relationship's {@code /payload}; the {@code payload} otherwise stays arbitrary JSON). In addition,
     * the built-in fields {@code id}, {@code createdAt}, {@code updatedAt}, and {@code status} behave as
     * {@code full} and are always filterable and sortable on any class, except that {@code status} may be
     * excluded when the class declares it as a projected field and the token cannot fully reach it.
     *
     * <p>Results are scoped to the caller's access token: only relationships the token is permitted to read
     * ({@code get}) are returned. Relationships the token cannot read are silently omitted — the call does not
     * error and does not return a {@code 403} for the un-readable relationships. Token scoping and the
     * {@code filterFast} / {@code filter} / {@code sort} criteria are applied together, so the returned page
     * never contains a relationship the token cannot read regardless of the other criteria. When the PubNub
     * instance is configured with a secretKey (a trusted server-side deployment, never a client — the secretKey
     * must not be shipped to clients), or when using a token whose grants cover the whole result set, no
     * permission-based filtering is applied and all matching relationships are returned, subject only to
     * {@code filterFast} / {@code filter} / {@code sort} and {@code limit} paging.
     *
     * @param className Relationship class identifier.
     */
    GetRelationships getRelationships(String className);

    /**
     * Partially update a DataSync relationship via JSON Patch (RFC-6902). Optional {@code ifMatch} is set via the returned builder.
     *
     * @param relationshipId Identifier of the relationship to patch.
     * @param operations     Non-empty list of JSON Patch operations to apply.
     */
    UpdateRelationship updateRelationship(String relationshipId, List<PNJsonPatchOperation> operations);

    /**
     * Replaces a relationship in full. Optional fields are set via the returned builder.
     * <p>
     * Every mutable field is overwritten. Omitting {@code status} or {@code payload} clears the stored value rather
     * than preserving it, so a read-modify-write must send back every field it wants to keep. This cannot re-point
     * or reclassify a relationship — the linked entities and the relationship class are immutable. Use
     * {@link #updateRelationship(String, List)} to change part of a relationship.
     *
     * @param relationshipId Identifier of the relationship to replace.
     * @param classVersion   Version of the relationship class the payload conforms to. Relationship classes are
     *                       versioned (an integer {@code >= 1}); pass the version of the class you are targeting.
     */
    SetRelationship setRelationship(String relationshipId, int classVersion);
}