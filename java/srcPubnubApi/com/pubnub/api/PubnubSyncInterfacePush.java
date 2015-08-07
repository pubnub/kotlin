package com.pubnub.api;

/**
 * Created by work1 on 06/08/15.
 */
interface PubnubSyncInterfacePush {
    /**
     *
     * @param channel
     * @param gcmRegistrationId
     * @return
     */
    Object	enablePushNotificationsOnChannel(String channel, String gcmRegistrationId);

    /**
     *
     * @param channels
     * @param gcmRegistrationId
     * @return
     */
    Object	enablePushNotificationsOnChannels(String[] channels, String gcmRegistrationId);

    /**
     *
     * @param channel
     * @param gcmRegistrationId
     * @return
     */
    Object	disablePushNotificationsOnChannel(String channel, String gcmRegistrationId);

    /**
     *
     * @param channels
     * @param gcmRegistrationId
     * @return
     */
    Object	disablePushNotificationsOnChannels(String[] channels, String gcmRegistrationId);

}
