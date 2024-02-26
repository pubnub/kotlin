package com.pubnub.internal.endpoints.files

import com.pubnub.api.endpoints.files.DeleteFile
import com.pubnub.internal.PubNubImpl

/**
 * @see [PubNubImpl.deleteFile]
 */
class DeleteFileImpl internal constructor(deleteFile: IDeleteFile) :
    IDeleteFile by deleteFile,
    DeleteFile
