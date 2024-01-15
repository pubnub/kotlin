package com.pubnub.api.enums;

public enum PNStatusCategory {

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
    PNURITooLongCategory,
    PNMalformedFilterExpressionCategory,
    PNMalformedResponseCategory,
    PNDecryptionErrorCategory,
    PNTLSConnectionFailedCategory,
    PNTLSUntrustedCertificateCategory,

    PNRequestMessageCountExceededCategory,
    PNReconnectionAttemptsExhaustedCategory,
    PNRateLimitExceededCategory;
}
