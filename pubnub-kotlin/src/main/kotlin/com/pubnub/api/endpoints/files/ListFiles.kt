package com.pubnub.api.endpoints.files

import com.pubnub.api.Endpoint
import com.pubnub.api.PubNub
import com.pubnub.api.models.consumer.files.PNListFilesResult
import com.pubnub.internal.endpoints.files.IListFiles

/**
 * @see [PubNub.listFiles]
 */
class ListFiles internal constructor(listFiles: IListFiles) : Endpoint<PNListFilesResult>(), IListFiles by listFiles