package com.pubnub.api.enums

enum class PNStatusCategory {
    PNUnknownCategory,
    PNAcknowledgmentCategory,
    PNAccessDeniedCategory,
    PNTimeoutCategory,
    PNNetworkIssuesCategory,
    PNConnectedCategory,
    PNReconnectedCategory,
    PNDisconnectedCategory,
    PNUnexpectedDisconnectCategory,
    PNCancelledCategory,
    PNBadRequestCategory,
    PNMalformedFilterExpressionCategory,
    PNMalformedResponseCategory,
    PNDecryptionErrorCategory,
    PNTLSConnectionFailedCategory,
    PNTLSUntrustedCertificateCategory,

    PNRequestMessageCountExceededCategory,
    PNReconnectionAttemptsExhausted,
    PNNotFoundCategory
}
