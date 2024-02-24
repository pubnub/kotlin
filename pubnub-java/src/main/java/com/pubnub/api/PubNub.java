package com.pubnub.api;

import com.pubnub.api.builder.PresenceBuilder;
import com.pubnub.api.builder.SubscribeBuilder;
import com.pubnub.api.builder.UnsubscribeBuilder;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.endpoints.DeleteMessages;
import com.pubnub.api.endpoints.FetchMessages;
import com.pubnub.api.endpoints.History;
import com.pubnub.api.endpoints.MessageCounts;
import com.pubnub.api.endpoints.Time;
import com.pubnub.api.endpoints.access.Grant;
import com.pubnub.api.endpoints.access.RevokeToken;
import com.pubnub.api.endpoints.access.builder.GrantTokenBuilder;
import com.pubnub.api.endpoints.channel_groups.AddChannelChannelGroup;
import com.pubnub.api.endpoints.channel_groups.AllChannelsChannelGroup;
import com.pubnub.api.endpoints.channel_groups.DeleteChannelGroup;
import com.pubnub.api.endpoints.channel_groups.ListAllChannelGroup;
import com.pubnub.api.endpoints.channel_groups.RemoveChannelChannelGroup;
import com.pubnub.api.endpoints.files.DeleteFile;
import com.pubnub.api.endpoints.files.DownloadFile;
import com.pubnub.api.endpoints.files.GetFileUrl;
import com.pubnub.api.endpoints.files.ListFiles;
import com.pubnub.api.endpoints.files.PublishFileMessage;
import com.pubnub.api.endpoints.files.SendFile;
import com.pubnub.api.endpoints.message_actions.AddMessageAction;
import com.pubnub.api.endpoints.message_actions.GetMessageActions;
import com.pubnub.api.endpoints.message_actions.RemoveMessageAction;
import com.pubnub.api.endpoints.objects_api.channel.GetAllChannelsMetadata;
import com.pubnub.api.endpoints.objects_api.channel.GetChannelMetadata;
import com.pubnub.api.endpoints.objects_api.channel.RemoveChannelMetadata;
import com.pubnub.api.endpoints.objects_api.channel.SetChannelMetadata;
import com.pubnub.api.endpoints.objects_api.members.GetChannelMembers;
import com.pubnub.api.endpoints.objects_api.members.ManageChannelMembers;
import com.pubnub.api.endpoints.objects_api.members.RemoveChannelMembers;
import com.pubnub.api.endpoints.objects_api.members.SetChannelMembers;
import com.pubnub.api.endpoints.objects_api.memberships.GetMemberships;
import com.pubnub.api.endpoints.objects_api.memberships.ManageMemberships;
import com.pubnub.api.endpoints.objects_api.memberships.RemoveMemberships;
import com.pubnub.api.endpoints.objects_api.memberships.SetMemberships;
import com.pubnub.api.endpoints.objects_api.uuid.GetAllUUIDMetadata;
import com.pubnub.api.endpoints.objects_api.uuid.GetUUIDMetadata;
import com.pubnub.api.endpoints.objects_api.uuid.RemoveUUIDMetadata;
import com.pubnub.api.endpoints.objects_api.uuid.SetUUIDMetadata;
import com.pubnub.api.endpoints.presence.GetState;
import com.pubnub.api.endpoints.presence.HereNow;
import com.pubnub.api.endpoints.presence.SetState;
import com.pubnub.api.endpoints.presence.WhereNow;
import com.pubnub.api.endpoints.pubsub.Publish;
import com.pubnub.api.endpoints.pubsub.Signal;
import com.pubnub.api.endpoints.push.AddChannelsToPush;
import com.pubnub.api.endpoints.push.ListPushProvisions;
import com.pubnub.api.endpoints.push.RemoveAllPushChannelsForDevice;
import com.pubnub.api.endpoints.push.RemoveChannelsFromPush;
import com.pubnub.api.models.consumer.access_manager.v3.PNToken;
import com.pubnub.api.v2.callbacks.EventListener;
import com.pubnub.api.v2.callbacks.StatusListener;
import com.pubnub.api.v2.entities.Channel;
import com.pubnub.api.v2.entities.ChannelGroup;
import com.pubnub.api.v2.entities.ChannelMetadata;
import com.pubnub.api.v2.entities.UserMetadata;
import com.pubnub.api.v2.subscriptions.Subscription;
import com.pubnub.api.v2.subscriptions.SubscriptionSet;
import com.pubnub.internal.PubNubImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

public interface PubNub extends BasePubNub<
        EventListener,
        Subscription,
        Channel,
        ChannelGroup,
        ChannelMetadata,
        UserMetadata,
        SubscriptionSet,
        StatusListener> {

    static PubNub create(PNConfiguration configuration) {
        return new PubNubImpl(configuration);
    }
    @NotNull PNConfiguration getConfiguration();

    @NotNull SubscribeBuilder subscribe();

    @NotNull UnsubscribeBuilder unsubscribe();

    @NotNull PresenceBuilder presence();

    @NotNull AddChannelsToPush addPushNotificationsOnChannels();

    @NotNull RemoveChannelsFromPush removePushNotificationsFromChannels();

    @NotNull RemoveAllPushChannelsForDevice removeAllPushNotificationsFromDeviceWithPushToken();

    @NotNull ListPushProvisions auditPushChannelProvisions();

    @NotNull WhereNow whereNow();

    @NotNull HereNow hereNow();

    @NotNull Time time();

    @NotNull History history();

    @NotNull FetchMessages fetchMessages();

    @NotNull DeleteMessages deleteMessages();

    @NotNull MessageCounts messageCounts();

    @NotNull Grant grant();

    @NotNull GrantTokenBuilder grantToken();

    @NotNull
    @SuppressWarnings("deprecation")
    GrantTokenBuilder grantToken(Integer ttl);

    @NotNull RevokeToken revokeToken();

    @NotNull GetState getPresenceState();

    @NotNull SetState setPresenceState();

    @NotNull Publish publish();

    @NotNull Signal signal();

    @NotNull ListAllChannelGroup listAllChannelGroups();

    @NotNull AllChannelsChannelGroup listChannelsForChannelGroup();

    @NotNull AddChannelChannelGroup addChannelsToChannelGroup();

    @NotNull RemoveChannelChannelGroup removeChannelsFromChannelGroup();

    @NotNull DeleteChannelGroup deleteChannelGroup();

    SetUUIDMetadata setUUIDMetadata();

    @NotNull GetAllUUIDMetadata getAllUUIDMetadata();

    @NotNull GetUUIDMetadata getUUIDMetadata();

    @NotNull RemoveUUIDMetadata removeUUIDMetadata();

    SetChannelMetadata.Builder setChannelMetadata();

    @NotNull GetAllChannelsMetadata getAllChannelsMetadata();

    @NotNull GetChannelMetadata.Builder getChannelMetadata();

    RemoveChannelMetadata.Builder removeChannelMetadata();

    @NotNull GetMemberships getMemberships();

    @NotNull SetMemberships.Builder setMemberships();

    RemoveMemberships.Builder removeMemberships();

    ManageMemberships.Builder manageMemberships();

    GetChannelMembers.Builder getChannelMembers();

    SetChannelMembers.Builder setChannelMembers();

    RemoveChannelMembers.Builder removeChannelMembers();

    ManageChannelMembers.Builder manageChannelMembers();

    @NotNull AddMessageAction addMessageAction();

    @NotNull GetMessageActions getMessageActions();

    @NotNull RemoveMessageAction removeMessageAction();

    SendFile.Builder sendFile();

    ListFiles.Builder listFiles();

    GetFileUrl.Builder getFileUrl();

    DownloadFile.Builder downloadFile();

    DeleteFile.Builder deleteFile();

    PublishFileMessage.Builder publishFileMessage();

    @Nullable String decrypt(String inputString) throws PubNubException;

    @Nullable String decrypt(String inputString, String cipherKey) throws PubNubException;

    InputStream decryptInputStream(InputStream inputStream) throws PubNubException;

    InputStream decryptInputStream(InputStream inputStream, String cipherKey) throws PubNubException;

    @Nullable String encrypt(String inputString) throws PubNubException;

    @Nullable String encrypt(String inputString, String cipherKey) throws PubNubException;

    InputStream encryptInputStream(InputStream inputStream) throws PubNubException;

    InputStream encryptInputStream(InputStream inputStream, String cipherKey) throws PubNubException;

    @NotNull String getRequestId();

    void reconnect();

    void disconnect();

    @NotNull Publish fire();

    @NotNull List<String> getSubscribedChannels();

    @NotNull List<String> getSubscribedChannelGroups();

    void unsubscribeAll();

    PNToken parseToken(String token) throws PubNubException;

    void setToken(String token);

    @NotNull Channel channel(@NotNull String name);

    @NotNull ChannelGroup channelGroup(@NotNull String name);

    @NotNull ChannelMetadata channelMetadata(@NotNull String id);

    @NotNull UserMetadata userMetadata(@NotNull String id);

    @NotNull SubscriptionSet subscriptionSetOf(@NotNull Set<? extends Subscription> subscriptions);

    void addListener(@NotNull EventListener listener);

    void addListener(@NotNull StatusListener listener);

    void addListener(@NotNull SubscribeCallback listener);
}
