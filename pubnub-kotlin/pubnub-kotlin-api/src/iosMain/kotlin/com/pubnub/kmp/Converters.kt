package com.pubnub.kmp

import platform.Foundation.NSData
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.dataUsingEncoding

fun String.toNSData(): NSData? {
    return (this as NSString).dataUsingEncoding(NSUTF8StringEncoding)
}