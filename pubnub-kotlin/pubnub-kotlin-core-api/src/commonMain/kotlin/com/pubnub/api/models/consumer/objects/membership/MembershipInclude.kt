package com.pubnub.api.models.consumer.objects.membership

import com.pubnub.api.models.consumer.objects.member.Include

/**
 * Interface representing options to include additional data when using Memberships API.
 */
interface MembershipInclude : Include {
    /**
     * Whether to include channel information in the result.
     */
    val includeChannel: Boolean

    /**
     * Whether to include custom properties of the channel in the result.
     */
    val includeChannelCustom: Boolean

    /**
     * Whether to include the type of the channel in the result.
     */
    val includeChannelType: Boolean

    /**
     * Whether to include the status of the channel in the result.
     */
    val includeChannelStatus: Boolean
}
