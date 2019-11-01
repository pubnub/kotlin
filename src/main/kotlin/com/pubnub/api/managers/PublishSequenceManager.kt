package com.pubnub.api.managers

internal class PublishSequenceManager {

    private val maxSequence = 65535
    private var nextSequence = 0

    @Synchronized
    internal fun nextSequence(): Int {
        if (maxSequence == nextSequence) {
            nextSequence = 1
        } else {
            nextSequence++
        }
        return nextSequence
    }
}