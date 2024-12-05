package com.pubnub.api.java.models.consumer.objects_api.member

/**
 * Represents the options for including additional details in member-related operations.
 *
 * This interface extends [com.pubnub.api.models.consumer.objects.member.MemberInclude]
 * and provides a flexible builder pattern to configure the desired inclusion options.
 */
interface MemberInclude : com.pubnub.api.models.consumer.objects.member.MemberInclude {
    companion object {
        /**
         * Creates a new [Builder] for constructing a [MemberInclude] instance.
         *
         * @return a new [Builder] instance.
         */
        @JvmStatic
        fun builder() = Builder()
    }

    /**
     * Builder class for configuring and creating instances of [MemberInclude].
     */
    class Builder internal constructor() {
        var includeCustom: Boolean = false
        var includeStatus: Boolean = false
        var includeType: Boolean = false
        var includeTotalCount: Boolean = false
        var includeUser: Boolean = false
        var includeUserCustom: Boolean = false
        var includeUserType: Boolean = false
        var includeUserStatus: Boolean = false

        /**
         * Specifies whether to include custom data in the member data.
         *
         * @param includeCustom `true` to include custom fields, `false` otherwise.
         * @return the current [Builder] instance.
         */
        fun includeCustom(includeCustom: Boolean): Builder {
            this.includeCustom = includeCustom
            return this
        }

        /**
         * Specifies whether to include the status of the member.
         *
         * @param includeStatus `true` to include status, `false` otherwise.
         * @return the current [Builder] instance.
         */
        fun includeStatus(includeStatus: Boolean): Builder {
            this.includeStatus = includeStatus
            return this
        }

        /**
         * Specifies whether to include the type of the member.
         *
         * @param includeType `true` to include type, `false` otherwise.
         * @return the current [Builder] instance.
         */
        fun includeType(includeType: Boolean): Builder {
            this.includeType = includeType
            return this
        }

        /**
         * Specifies whether to include the total count of members.
         *
         * @param includeTotalCount `true` to include the total count, `false` otherwise.
         * @return the current [Builder] instance.
         */
        fun includeTotalCount(includeTotalCount: Boolean): Builder {
            this.includeTotalCount = includeTotalCount
            return this
        }

        /**
         * Specifies whether to include user information in the member data.
         *
         * @param includeUser `true` to include user information, `false` otherwise.
         * @return the current [Builder] instance.
         */
        fun includeUser(includeUser: Boolean): Builder {
            this.includeUser = includeUser
            return this
        }

        /**
         * Specifies whether to include custom fields for the user in the member data.
         *
         * @param includeUserCustom `true` to include user custom fields, `false` otherwise.
         * @return the current [Builder] instance.
         */
        fun includeUserCustom(includeUserCustom: Boolean): Builder {
            this.includeUserCustom = includeUserCustom
            return this
        }

        /**
         * Specifies whether to include the type of the user in the member data.
         *
         * @param includeUserType `true` to include user type, `false` otherwise.
         * @return the current [Builder] instance.
         */
        fun includeUserType(includeUserType: Boolean): Builder {
            this.includeUserType = includeUserType
            return this
        }

        /**
         * Specifies whether to include the status of the user in the member data.
         *
         * @param includeUserStatus `true` to include user status, `false` otherwise.
         * @return the current [Builder] instance.
         */
        fun includeUserStatus(includeUserStatus: Boolean): Builder {
            this.includeUserStatus = includeUserStatus
            return this
        }

        /**
         * Builds and returns a new [MemberInclude] instance with the configured options.
         *
         * @return a new [MemberInclude] instance.
         */
        fun build(): MemberInclude {
            return object : MemberInclude {
                override val includeCustom: Boolean = this@Builder.includeCustom
                override val includeUserStatus: Boolean = this@Builder.includeUserStatus
                override val includeUserType: Boolean = this@Builder.includeUserType
                override val includeTotalCount: Boolean = this@Builder.includeTotalCount
                override val includeUser: Boolean = this@Builder.includeUser
                override val includeUserCustom: Boolean = this@Builder.includeUserCustom
                override val includeStatus: Boolean = this@Builder.includeStatus
                override val includeType: Boolean = this@Builder.includeType
            }
        }
    }
}
