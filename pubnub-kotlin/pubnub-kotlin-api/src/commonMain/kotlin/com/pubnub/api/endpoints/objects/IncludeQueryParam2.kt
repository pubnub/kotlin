package com.pubnub.api.endpoints.objects

sealed class IncludeQueryParam2 {
    internal abstract val queryParamValue: String

    fun createIncludeQueryParams(): Pair<String, String> {
        return "include" to queryParamValue
    }

    open operator fun plus(other: IncludeQueryParam2): IncludeQueryParam2 {
        return if (other is Empty) {
            return this
        } else if (other is Set) {
            other + this
        } else {
            Set(setOf(this, other))
        }
    }

    internal class Set(val params: kotlin.collections.Set<IncludeQueryParam2> = setOf<IncludeQueryParam2>()) :
        IncludeQueryParam2() {
        override val queryParamValue: String = params.joinToString(",") { it.queryParamValue }

        override fun plus(other: IncludeQueryParam2): IncludeQueryParam2 {
            return if (other is Empty) {
                return this
            } else if (other is Set) {
                Set(params + other.params)
            } else {
                Set(params + other)
            }
        }
    }

    object Empty : IncludeQueryParam2() {
        override val queryParamValue: String = ""

        override fun plus(other: IncludeQueryParam2): IncludeQueryParam2 {
            return other
        }
    }

    object Channel : IncludeQueryParam2() {
        override val queryParamValue: String = "channel"

        object Custom : IncludeQueryParam2() {
            override val queryParamValue: String = "channel.custom"
        }

        object Type : IncludeQueryParam2() {
            override val queryParamValue: String = "channel.type"
        }
    }

    object User : IncludeQueryParam2() {
        override val queryParamValue: String = "uuid"

        object Custom : IncludeQueryParam2() {
            override val queryParamValue: String = "uuid.custom"
        }

        object Type : IncludeQueryParam2() {
            override val queryParamValue: String = "uuid.type"
        }
    }

    object Custom : IncludeQueryParam2() {
        override val queryParamValue: String = "custom"
    }

    object Type : IncludeQueryParam2() {
        override val queryParamValue: String = "type"
    }

    object Status : IncludeQueryParam2() {
        override val queryParamValue: String = "status"
    }
}
