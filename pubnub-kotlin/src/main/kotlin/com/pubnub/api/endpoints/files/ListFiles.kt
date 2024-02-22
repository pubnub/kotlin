package com.pubnub.api.endpoints.files

import com.pubnub.api.Endpoint
import com.pubnub.internal.PubNubImpl
import com.pubnub.api.models.consumer.files.PNListFilesResult
import com.pubnub.internal.endpoints.files.IListFiles

/**
 * @see [PubNubImpl.listFiles]
 */
class ListFiles internal constructor(listFiles: IListFiles) : Endpoint<PNListFilesResult>(), IListFiles by listFiles
