package com.pubnub.api;

/**
 * Created by work1 on 06/08/15.
 */
public interface PubnubSyncInterfacePush {

    Object	enablePushNotificationsOnChannel(String channel, String gcmRegistrationId);

    Object	enablePushNotificationsOnChannels(String[] channels, String gcmRegistrationId);

    Object	disablePushNotificationsOnChannel(String channel, String gcmRegistrationId);

    Object	disablePushNotificationsOnChannels(String[] channels, String gcmRegistrationId);

}
