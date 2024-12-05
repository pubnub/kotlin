package com.pubnub.api.java.models.consumer.objects_api.membership

/**
 * Represents the options for including additional details in membership-related operations.
 *
 * This interface extends [com.pubnub.api.models.consumer.objects.membership.MembershipInclude]
 * and provides a flexible builder pattern to configure the desired inclusion options.
 */
interface MembershipInclude : com.pubnub.api.models.consumer.objects.membership.MembershipInclude {
    companion object {
        /**
         * Creates a new [Builder] for constructing a [MembershipInclude] instance.
         *
         * @return a new [Builder] instance.
         */
        @JvmStatic
        fun builder() = Builder()
    }

    /**
     * Builder class for configuring and creating instances of [MembershipInclude].
     */
    class Builder internal constructor() {
        var includeCustom: Boolean = false
        var includeStatus: Boolean = false
        var includeType: Boolean = false
        var includeTotalCount: Boolean = false
        var includeChannel: Boolean = false
        var includeChannelCustom: Boolean = false
        var includeChannelType: Boolean = false
        var includeChannelStatus: Boolean = false

        /**
         * Specifies whether to include custom fields in the membership data.
         *
         * @param includeCustom `true` to include custom data, `false` otherwise.
         * @return the current [Builder] instance.
         */
        fun includeCustom(includeCustom: Boolean): Builder {
            this.includeCustom = includeCustom
            return this
        }

        /**
         * Specifies whether to include the status of the membership.
         *
         * @param includeStatus `true` to include status, `false` otherwise.
         * @return the current [Builder] instance.
         */
        fun includeStatus(includeStatus: Boolean): Builder {
            this.includeStatus = includeStatus
            return this
        }

        /**
         * Specifies whether to include the type of the membership.
         *
         * @param includeType `true` to include type, `false` otherwise.
         * @return the current [Builder] instance.
         */
        fun includeType(includeType: Boolean): Builder {
            this.includeType = includeType
            return this
        }

        /**
         * Specifies whether to include the total count of memberships.
         *
         * @param includeTotalCount `true` to include the total count, `false` otherwise.
         * @return the current [Builder] instance.
         */
        fun includeTotalCount(includeTotalCount: Boolean): Builder {
            this.includeTotalCount = includeTotalCount
            return this
        }

        /**
         * Specifies whether to include channel information in the membership data.
         *
         * @param includeChannel `true` to include channel information, `false` otherwise.
         * @return the current [Builder] instance.
         */
        fun includeChannel(includeChannel: Boolean): Builder {
            this.includeChannel = includeChannel
            return this
        }

        /**
         * Specifies whether to include custom data for the channel in the membership data.
         *
         * @param includeChannelCustom `true` to include channel custom fields, `false` otherwise.
         * @return the current [Builder] instance.
         */
        fun includeChannelCustom(includeChannelCustom: Boolean): Builder {
            this.includeChannelCustom = includeChannelCustom
            return this
        }

        /**
         * Specifies whether to include the type of the channel in the membership data.
         *
         * @param includeChannelType `true` to include channel type, `false` otherwise.
         * @return the current [Builder] instance.
         */
        fun includeChannelType(includeChannelType: Boolean): Builder {
            this.includeChannelType = includeChannelType
            return this
        }

        /**
         * Specifies whether to include the status of the channel in the membership data.
         *
         * @param includeChannelStatus `true` to include channel status, `false` otherwise.
         * @return the current [Builder] instance.
         */
        fun includeChannelStatus(includeChannelStatus: Boolean): Builder {
            this.includeChannelStatus = includeChannelStatus
            return this
        }

        /**
         * Builds and returns a new [MembershipInclude] instance with the configured options.
         *
         * @return a new [MembershipInclude] instance.
         */
        fun build(): MembershipInclude {
            return object : MembershipInclude {
                override val includeCustom: Boolean = this@Builder.includeCustom
                override val includeTotalCount: Boolean = this@Builder.includeTotalCount
                override val includeChannel: Boolean = this@Builder.includeChannel
                override val includeChannelCustom: Boolean = this@Builder.includeChannelCustom
                override val includeChannelType: Boolean = this@Builder.includeChannelType
                override val includeChannelStatus: Boolean = this@Builder.includeChannelStatus
                override val includeStatus: Boolean = this@Builder.includeStatus
                override val includeType: Boolean = this@Builder.includeType
            }
        }
    }
}
