package com.pubnub.kmp

interface Codable {
    /**
     * This must return a type that is already considered Codable in Swift, such as
     * Map, Collection, Array, String, Boolean, Int/Long. //todo what about null?
     */
    fun toCodable(): Any
}