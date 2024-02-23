package com.pubnub.internal.kotlin.endpoints.files

import com.pubnub.api.endpoints.files.DeleteFile
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.endpoints.files.IDeleteFile

/**
 * @see [PubNubImpl.deleteFile]
 */
class DeleteFileImpl internal constructor(deleteFile: IDeleteFile) :
    IDeleteFile by deleteFile,
    DeleteFile
