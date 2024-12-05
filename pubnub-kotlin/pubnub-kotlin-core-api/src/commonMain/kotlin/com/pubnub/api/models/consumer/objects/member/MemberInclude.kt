package com.pubnub.api.models.consumer.objects.member

/**
 * Base interface defining options to include additional data when using Memberships API and ChannelMembers.
 */
interface Include {
    /**
     * Whether to include custom data of the Membership in the result.
     */
    val includeCustom: Boolean

    /**
     * Whether to include the status of the Membership in the result.
     */
    val includeStatus: Boolean

    /**
     * Whether to include the type of the Membership in the result.
     */
    val includeType: Boolean

    /**
     * Whether to include the total count of Memberships in the result.
     */
    val includeTotalCount: Boolean
}

/**
 * Interface representing options to include additional data when using Channel Members API.
 */
interface MemberInclude : Include {
    /**
     * Whether to include user information in the result.
     */
    val includeUser: Boolean

    /**
     * Whether to include custom properties of the user in the result.
     */
    val includeUserCustom: Boolean

    /**
     * Whether to include the type of the user in the result.
     */
    val includeUserType: Boolean

    /**
     * Whether to include the status of the user in the result.
     */
    val includeUserStatus: Boolean
}
