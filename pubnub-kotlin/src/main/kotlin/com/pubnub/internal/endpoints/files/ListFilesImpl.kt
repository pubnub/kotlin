package com.pubnub.internal.endpoints.files

import com.pubnub.api.endpoints.files.ListFiles
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.listFiles]
 */
class ListFilesImpl internal constructor(listFiles: IListFiles) : IListFiles by listFiles, ListFiles
