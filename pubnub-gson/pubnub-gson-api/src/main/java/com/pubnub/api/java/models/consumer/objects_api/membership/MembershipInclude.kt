package com.pubnub.api.java.models.consumer.objects_api.membership

interface MembershipInclude : com.pubnub.api.models.consumer.objects.membership.MembershipInclude {
    companion object {
        @JvmStatic
        fun builder() = Builder()
    }

    class Builder internal constructor() {
        var includeCustom: Boolean = false
        var includeStatus: Boolean = false
        var includeType: Boolean = false
        var includeTotalCount: Boolean = false
        var includeChannel: Boolean = false
        var includeChannelCustom: Boolean = false
        var includeChannelType: Boolean = false
        var includeChannelStatus: Boolean = false

        fun includeCustom(includeCustom: Boolean): Builder {
            this.includeCustom = includeCustom
            return this
        }

        fun includeStatus(includeStatus: Boolean): Builder {
            this.includeStatus = includeStatus
            return this
        }

        fun includeType(includeType: Boolean): Builder {
            this.includeType = includeType
            return this
        }

        fun includeTotalCount(includeTotalCount: Boolean): Builder {
            this.includeTotalCount = includeTotalCount
            return this
        }

        fun includeChannel(includeChannel: Boolean): Builder {
            this.includeChannel = includeChannel
            return this
        }

        fun includeChannelCustom(includeChannelCustom: Boolean): Builder {
            this.includeChannelCustom = includeChannelCustom
            return this
        }

        fun includeChannelType(includeChannelType: Boolean): Builder {
            this.includeChannelType = includeChannelType
            return this
        }

        fun includeChannelStatus(includeChannelStatus: Boolean): Builder {
            this.includeChannelStatus = includeChannelStatus
            return this
        }

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
