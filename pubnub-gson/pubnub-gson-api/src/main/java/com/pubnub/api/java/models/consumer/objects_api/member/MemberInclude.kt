package com.pubnub.api.java.models.consumer.objects_api.member

interface MemberInclude : com.pubnub.api.models.consumer.objects.member.MemberInclude {
    companion object {
        @JvmStatic
        fun builder() = Builder()
    }

    class Builder internal constructor() {
        var includeCustom: Boolean = false
        var includeStatus: Boolean = false
        var includeType: Boolean = false
        var includeTotalCount: Boolean = false
        var includeUser: Boolean = false
        var includeUserCustom: Boolean = false
        var includeUserType: Boolean = false
        var includeUserStatus: Boolean = false

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

        fun includeUser(includeUser: Boolean): Builder {
            this.includeUser = includeUser
            return this
        }

        fun includeUserCustom(includeUserCustom: Boolean): Builder {
            this.includeUserCustom = includeUserCustom
            return this
        }

        fun includeUserType(includeUserType: Boolean): Builder {
            this.includeUserType = includeUserType
            return this
        }

        fun includeUserStatus(includeUserStatus: Boolean): Builder {
            this.includeUserStatus = includeUserStatus
            return this
        }

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
