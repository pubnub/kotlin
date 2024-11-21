package com.pubnub.api.models.consumer.objects.membership

import com.pubnub.api.models.consumer.objects.member.Include
import kotlin.jvm.JvmStatic

class MembershipInclude(
    override val includeCustom: Boolean = false,
    override val includeStatus: Boolean = false,
    override val includeType: Boolean = false,
    override val includeTotalCount: Boolean = false,
    val includeChannel: Boolean = false,
    val includeChannelCustom: Boolean = false,
    val includeChannelType: Boolean = false,
    val includeChannelStatus: Boolean = false,
) : Include() {
    companion object {
        @JvmStatic
        fun includeCustom(includeCustom: Boolean): Builder {
            return Builder().includeCustom(includeCustom)
        }

        @JvmStatic
        fun includeStatus(includeStatus: Boolean): Builder {
            return Builder().includeStatus(includeStatus)
        }

        @JvmStatic
        fun includeType(includeType: Boolean): Builder {
            return Builder().includeType(includeType)
        }

        @JvmStatic
        fun includeTotalCount(includeTotalCount: Boolean): Builder {
            return Builder().includeTotalCount(includeTotalCount)
        }

        @JvmStatic
        fun includeChannel(includeChannel: Boolean): Builder {
            return Builder().includeChannel(includeChannel)
        }

        @JvmStatic
        fun includeChannelCustom(includeChannelCustom: Boolean): Builder {
            return Builder().includeChannelCustom(includeChannelCustom)
        }

        @JvmStatic
        fun includeChannelType(includeChannelType: Boolean): Builder {
            return Builder().includeChannelType(includeChannelType)
        }

        @JvmStatic
        fun includeChannelStatus(includeChannelStatus: Boolean): Builder {
            return Builder().includeChannelStatus(includeChannelStatus)
        }
    }

    class Builder {
        private var includeCustom: Boolean = false
        private var includeStatus: Boolean = false
        private var includeType: Boolean = false
        private var includeTotalCount: Boolean = false

        private var includeChannel: Boolean = false
        private var includeChannelCustom: Boolean = false
        private var includeChannelType: Boolean = false
        private var includeChannelStatus: Boolean = false

        fun includeCustom(includeCustom: Boolean): Builder = apply { this.includeCustom = includeCustom }

        fun includeStatus(includeStatus: Boolean) = apply { this.includeStatus = includeStatus }

        fun includeType(includeType: Boolean) = apply { this.includeType = includeType }

        fun includeTotalCount(includeTotalCount: Boolean) = apply { this.includeTotalCount = includeTotalCount }

        fun includeChannel(includeChannel: Boolean) = apply { this.includeChannel = includeChannel }

        fun includeChannelCustom(includeChannelCustom: Boolean) =
            apply { this.includeChannelCustom = includeChannelCustom }

        fun includeChannelType(includeChannelType: Boolean) = apply { this.includeChannelType = includeChannelType }

        fun includeChannelStatus(includeChannelStatus: Boolean) = apply { this.includeChannelStatus = includeChannelStatus }

        fun build(): MembershipInclude {
            return MembershipInclude(
                includeCustom,
                includeStatus,
                includeType,
                includeTotalCount,
                includeChannel,
                includeChannelCustom,
                includeChannelType,
                includeChannelStatus
            )
        }
    }
}
