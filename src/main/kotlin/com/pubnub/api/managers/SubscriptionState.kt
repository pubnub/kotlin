package com.pubnub.api.managers

import com.pubnub.api.models.SubscriptionItem

class SubscriptionState {

    internal var channels: HashMap<String, SubscriptionItem> = hashMapOf()
        @Synchronized internal set
    internal var channelGroups: HashMap<String, SubscriptionItem> = hashMapOf()
        @Synchronized internal set
    private val presenceChannels: HashMap<String, SubscriptionItem> = hashMapOf()
    private val presenceGroups: HashMap<String, SubscriptionItem> = hashMapOf()
    private val heartbeatChannels: HashMap<String, SubscriptionItem> = hashMapOf()
    private val heartbeatGroups: HashMap<String, SubscriptionItem> = hashMapOf()
    private var region: String? = null
    private var timetoken = 0L
    private var storedTimetoken: Long? = null
}
