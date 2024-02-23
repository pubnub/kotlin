package com.pubnub.internal.kotlin.endpoints.files

import com.pubnub.api.endpoints.files.ListFiles
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.files.IListFiles

/**
 * @see [PubNubImpl.listFiles]
 */
class ListFilesImpl internal constructor(listFiles: IListFiles) : IListFiles by listFiles, ListFiles
