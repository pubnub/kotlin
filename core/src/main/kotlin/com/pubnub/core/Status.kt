package com.pubnub.core

import com.pubnub.api.models.consumer.PNErrorData
import java.util.ServiceLoader

interface Status {
    // https://youtrack.jetbrains.com/issue/KT-31420
    @Suppress("INAPPLICABLE_JVM_NAME")
    @get:JvmName("isError")
    val error: Boolean
    val category: StatusCategory
    val operation: OperationType
    val affectedChannels: List<String>
    val affectedChannelGroups: List<String>
    val exception: Exception?
    val statusCode: Int?

    // https://youtrack.jetbrains.com/issue/KT-31420
    @Suppress("INAPPLICABLE_JVM_NAME")
    @get:JvmName("isTlsEnabled")
    val tlsEnabled: Boolean?
    val origin: String?
    val uuid: String?
    val authKey: String?

    // https://youtrack.jetbrains.com/issue/KT-31420
    @Suppress("INAPPLICABLE_JVM_NAME")
    @get:JvmName("getErrorData")
    val __errorData: PNErrorData?
}


interface PNStatusFactory {
    fun createPNStatus(
        category: StatusCategory,
        error: Boolean,
        operation: OperationType,
        affectedChannels: List<String>,
        affectedChannelGroups: List<String>,
        exception: CoreException? = null,
        statusCode: Int? = null,
        tlsEnabled: Boolean? = null,
        origin: String? = null,
        uuid: String? = null,
        authKey: String? = null,
        errorData: PNErrorData? = null
    ): Status
}

val pnStatusFactory: PNStatusFactory by lazy {
    ServiceLoader
        .load(PNStatusFactory::class.java).first()
}

fun PNStatus(
    category: StatusCategory,
    error: Boolean,
    operation: OperationType,
    affectedChannels: List<String>,
    affectedChannelGroups: List<String>,
    exception: CoreException? = null,
    statusCode: Int? = null,
    tlsEnabled: Boolean? = null,
    origin: String? = null,
    uuid: String? = null,
    authKey: String? = null
): Status {
    return pnStatusFactory.createPNStatus(
        category = category,
        error = error,
        operation = operation,
        affectedChannels = affectedChannels.toList(),
        affectedChannelGroups = affectedChannelGroups.toList(),
        exception = exception,
        statusCode = statusCode,
        tlsEnabled = tlsEnabled,
        origin = origin,
        uuid = uuid,
        authKey = authKey,
        errorData = exception?.let { PNErrorData(it.message, it) }
    )
}
