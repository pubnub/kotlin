// package com.pubnub.internal
//
// import com.pubnub.api.PubNubException
// import com.pubnub.api.UserId
// import com.pubnub.api.crypto.CryptoModule
// import com.pubnub.api.enums.PNPushEnvironment
// import com.pubnub.api.enums.PNPushType
// import com.pubnub.api.models.consumer.PNBoundedPage
// import com.pubnub.api.models.consumer.access_manager.v3.PNToken
// import com.pubnub.api.models.consumer.history.PNHistoryResult
// import com.pubnub.api.models.consumer.message_actions.PNMessageAction
// import com.pubnub.api.models.consumer.objects.PNPage
// import com.pubnub.api.v2.PNConfiguration
// import com.pubnub.api.v2.subscriptions.EmptyOptions
// import com.pubnub.api.v2.subscriptions.Subscription
// import com.pubnub.api.v2.subscriptions.SubscriptionCursor
// import com.pubnub.api.v2.subscriptions.SubscriptionOptions
// import com.pubnub.internal.crypto.decryptString
// import com.pubnub.internal.crypto.encryptString
// import com.pubnub.internal.endpoints.DeleteMessagesEndpoint
// import com.pubnub.internal.endpoints.FetchMessagesEndpoint
// import com.pubnub.internal.endpoints.HistoryEndpoint
// import com.pubnub.internal.endpoints.MessageCountsEndpoint
// import com.pubnub.internal.endpoints.TimeEndpoint
// import com.pubnub.internal.endpoints.access.GrantEndpoint
// import com.pubnub.internal.endpoints.access.GrantTokenEndpoint
// import com.pubnub.internal.endpoints.access.RevokeTokenEndpoint
// import com.pubnub.internal.endpoints.channel_groups.AddChannelChannelGroupEndpoint
// import com.pubnub.internal.endpoints.channel_groups.AllChannelsChannelGroupEndpoint
// import com.pubnub.internal.endpoints.channel_groups.DeleteChannelGroupEndpoint
// import com.pubnub.internal.endpoints.channel_groups.ListAllChannelGroupEndpoint
// import com.pubnub.internal.endpoints.channel_groups.RemoveChannelChannelGroupEndpoint
// import com.pubnub.internal.endpoints.files.DeleteFileEndpoint
// import com.pubnub.internal.endpoints.files.DownloadFileEndpoint
// import com.pubnub.internal.endpoints.files.GenerateUploadUrlEndpoint
// import com.pubnub.internal.endpoints.files.GetFileUrlEndpoint
// import com.pubnub.internal.endpoints.files.ListFilesEndpoint
// import com.pubnub.internal.endpoints.files.PublishFileMessageEndpoint
// import com.pubnub.internal.endpoints.files.SendFileEndpoint
// import com.pubnub.internal.endpoints.files.UploadFileEndpoint
// import com.pubnub.internal.endpoints.message_actions.AddMessageActionEndpoint
// import com.pubnub.internal.endpoints.message_actions.GetMessageActionsEndpoint
// import com.pubnub.internal.endpoints.message_actions.RemoveMessageActionEndpoint
// import com.pubnub.internal.endpoints.objects.channel.GetAllChannelMetadataEndpoint
// import com.pubnub.internal.endpoints.objects.channel.GetChannelMetadataEndpoint
// import com.pubnub.internal.endpoints.objects.channel.RemoveChannelMetadataEndpoint
// import com.pubnub.internal.endpoints.objects.channel.SetChannelMetadataEndpoint
// import com.pubnub.internal.endpoints.objects.internal.CollectionQueryParameters
// import com.pubnub.internal.endpoints.objects.internal.IncludeQueryParam
// import com.pubnub.internal.endpoints.objects.member.GetChannelMembersEndpoint
// import com.pubnub.internal.endpoints.objects.member.ManageChannelMembersEndpoint
// import com.pubnub.internal.endpoints.objects.membership.GetMembershipsEndpoint
// import com.pubnub.internal.endpoints.objects.membership.ManageMembershipsEndpoint
// import com.pubnub.internal.endpoints.objects.uuid.GetAllUUIDMetadataEndpoint
// import com.pubnub.internal.endpoints.objects.uuid.GetUUIDMetadataEndpoint
// import com.pubnub.internal.endpoints.objects.uuid.RemoveUUIDMetadataEndpoint
// import com.pubnub.internal.endpoints.objects.uuid.SetUUIDMetadataEndpoint
// import com.pubnub.internal.endpoints.presence.GetStateEndpoint
// import com.pubnub.internal.endpoints.presence.HereNowEndpoint
// import com.pubnub.internal.endpoints.presence.SetStateEndpoint
// import com.pubnub.internal.endpoints.presence.WhereNowEndpoint
// import com.pubnub.internal.endpoints.pubsub.PublishEndpoint
// import com.pubnub.internal.endpoints.pubsub.SignalEndpoint
// import com.pubnub.internal.endpoints.push.AddChannelsToPushEndpoint
// import com.pubnub.internal.endpoints.push.ListPushProvisionsEndpoint
// import com.pubnub.internal.endpoints.push.RemoveAllPushChannelsForDeviceEndpoint
// import com.pubnub.internal.endpoints.push.RemoveChannelsFromPushEndpoint
// import com.pubnub.internal.managers.BasePathManager
// import com.pubnub.internal.managers.DuplicationManager
// import com.pubnub.internal.managers.ListenerManager
// import com.pubnub.internal.managers.MapperManager
// import com.pubnub.internal.managers.PublishSequenceManager
// import com.pubnub.internal.managers.RetrofitManager
// import com.pubnub.internal.managers.TokenManager
// import com.pubnub.internal.managers.TokenParser
// import com.pubnub.api.models.consumer.access_manager.sum.SpacePermissions
// import com.pubnub.api.models.consumer.access_manager.sum.UserPermissions
// import com.pubnub.api.models.consumer.access_manager.sum.toChannelGrant
// import com.pubnub.api.models.consumer.access_manager.sum.toUuidGrant
// import com.pubnub.api.models.consumer.access_manager.v3.ChannelGrant
// import com.pubnub.api.models.consumer.access_manager.v3.ChannelGroupGrant
// import com.pubnub.api.models.consumer.access_manager.v3.UUIDGrant
// import com.pubnub.api.models.consumer.objects.PNKey
// import com.pubnub.api.models.consumer.objects.PNMemberKey
// import com.pubnub.api.models.consumer.objects.PNMembershipKey
// import com.pubnub.api.models.consumer.objects.PNSortKey
// import com.pubnub.api.models.consumer.objects.member.MemberInput
// import com.pubnub.api.models.consumer.objects.member.PNUUIDDetailsLevel
// import com.pubnub.api.models.consumer.objects.membership.ChannelMembershipInput
// import com.pubnub.api.models.consumer.objects.membership.PNChannelDetailsLevel
// import com.pubnub.internal.presence.Presence
// import com.pubnub.internal.presence.eventengine.data.PresenceData
// import com.pubnub.internal.presence.eventengine.effect.effectprovider.HeartbeatProviderImpl
// import com.pubnub.internal.presence.eventengine.effect.effectprovider.LeaveProviderImpl
// import com.pubnub.internal.subscribe.PRESENCE_CHANNEL_SUFFIX
// import com.pubnub.internal.subscribe.Subscribe
// import com.pubnub.internal.subscribe.eventengine.configuration.EventEnginesConf
// import com.pubnub.internal.v2.entities.ChannelGroupImpl
// import com.pubnub.internal.v2.entities.ChannelGroupName
// import com.pubnub.internal.v2.entities.ChannelImpl
// import com.pubnub.internal.v2.entities.ChannelName
// import com.pubnub.internal.v2.subscription.SubscriptionImpl
// import com.pubnub.internal.workers.SubscribeMessageProcessor
// import java.io.InputStream
// import java.util.Date
// import java.util.UUID
// import java.util.concurrent.Executors
// import java.util.concurrent.ScheduledExecutorService
// import kotlin.time.Duration.Companion.seconds
//
// class PubNubCore internal constructor(
//    val configuration: PNConfiguration,
//    val listenerManager: ListenerManager,
//    eventEnginesConf: EventEnginesConf = EventEnginesConf(),
//    private val pnsdkName: String,
// ) {
//
//
//    //region Presence
//
//    //endregion
//
//    //region MessageActions
//
//    //region PAM
//
//
//
//    //endregion
//
//    //region Miscellaneous
//
//    //endregion
//
//    //region ObjectsAPI
//
//
//
//    /**
//     * @see [PubNub.getChannelMembers]
//     */
//    @Deprecated(
//        replaceWith =
//            ReplaceWith(
//                "getChannelMembers(channel = channel, limit = limit, " +
//                    "page = page, filter = filter, sort = sort, includeCount = includeCount, includeCustom = includeCustom," +
//                    "includeUUIDDetails = includeUUIDDetails)",
//            ),
//        level = DeprecationLevel.WARNING,
//        message = "Use getChannelMembers instead",
//    )
//    fun getMembers(
//        channel: String,
//        limit: Int? = null,
//        page: PNPage? = null,
//        filter: String? = null,
//        sort: Collection<PNSortKey<PNMemberKey>> = listOf(),
//        includeCount: Boolean = false,
//        includeCustom: Boolean = false,
//        includeUUIDDetails: PNUUIDDetailsLevel? = null,
//    ) = getChannelMembers(
//        channel = channel,
//        limit = limit,
//        page = page,
//        filter = filter,
//        sort = sort,
//        includeCount = includeCount,
//        includeCustom = includeCustom,
//        includeUUIDDetails = includeUUIDDetails,
//    )
//
//    /**
//     * @see [PubNub.setChannelMembers]
//     */
//    @Deprecated(
//        replaceWith =
//            ReplaceWith(
//                "setChannelMembers(channel = channel, uuids = uuids, limit = limit, " +
//                    "page = page, filter = filter, sort = sort, includeCount = includeCount, includeCustom = includeCustom," +
//                    "includeUUIDDetails = includeUUIDDetails)",
//            ),
//        level = DeprecationLevel.WARNING,
//        message = "Use setChannelMembers instead",
//    )
//    fun addMembers(
//        channel: String,
//        uuids: List<MemberInput>,
//        limit: Int? = null,
//        page: PNPage? = null,
//        filter: String? = null,
//        sort: Collection<PNSortKey<PNMemberKey>> = listOf(),
//        includeCount: Boolean = false,
//        includeCustom: Boolean = false,
//        includeUUIDDetails: PNUUIDDetailsLevel? = null,
//    ) = setChannelMembers(
//        channel = channel,
//        uuids = uuids,
//        limit = limit,
//        page = page,
//        filter = filter,
//        sort = sort,
//        includeCount = includeCount,
//        includeCustom = includeCustom,
//        includeUUIDDetails = includeUUIDDetails,
//    )
//
//    /**
//     * This method sets members in a channel.
//     *
//     * @param channel Channel name
//     * @param uuids List of members to add to the channel. List can contain strings (uuid only)
//     *              or objects (which can include custom data). @see [PNMember.Partial]
//     * @param limit Number of objects to return in the response.
//     *              Default is 100, which is also the maximum value.
//     *              Set limit to 0 (zero) and includeCount to true if you want to retrieve only a result count.
//     * @param page Use for pagination.
//     *              - [PNNext] : Previously-returned cursor bookmark for fetching the next page.
//     *              - [PNPrev] : Previously-returned cursor bookmark for fetching the previous page.
//     *                           Ignored if you also supply the start parameter.
//     * @param filter Expression used to filter the results. Only objects whose properties satisfy the given
//     *               expression are returned.
//     * @param sort List of properties to sort by. Available options are id, name, and updated.
//     *             @see [PNAsc], [PNDesc]
//     * @param includeCount Request totalCount to be included in paginated response. By default, totalCount is omitted.
//     *                     Default is `false`.
//     * @param includeCustom Include respective additional fields in the response.
//     * @param includeUUIDDetails Include custom fields for UUIDs metadata.
//     */
//    fun setChannelMembers(
//        channel: String,
//        uuids: List<MemberInput>,
//        limit: Int? = null,
//        page: PNPage? = null,
//        filter: String? = null,
//        sort: Collection<PNSortKey<PNMemberKey>> = listOf(),
//        includeCount: Boolean = false,
//        includeCustom: Boolean = false,
//        includeUUIDDetails: PNUUIDDetailsLevel? = null,
//        includeType: Boolean = false,
//    ) = manageChannelMembers(
//        channel = channel,
//        uuidsToSet = uuids,
//        uuidsToRemove = listOf(),
//        limit = limit,
//        page = page,
//        filter = filter,
//        sort = sort,
//        includeCount = includeCount,
//        includeCustom = includeCustom,
//        includeUUIDDetails = includeUUIDDetails,
//        includeType = includeType
//    )
//
//    /**
//     * @see [PubNub.removeChannelMembers]
//     */
//    @Deprecated(
//        replaceWith =
//            ReplaceWith(
//                "removeChannelMembers(channel = channel, uuids = uuids, limit = limit, " +
//                    "page = page, filter = filter, sort = sort, includeCount = includeCount, includeCustom = includeCustom," +
//                    "includeUUIDDetails = includeUUIDDetails)",
//            ),
//        level = DeprecationLevel.WARNING,
//        message = "Use removeChannelMembers instead",
//    )
//    fun removeMembers(
//        channel: String,
//        uuids: List<String>,
//        limit: Int? = null,
//        page: PNPage? = null,
//        filter: String? = null,
//        sort: Collection<PNSortKey<PNMemberKey>> = listOf(),
//        includeCount: Boolean = false,
//        includeCustom: Boolean = false,
//        includeUUIDDetails: PNUUIDDetailsLevel? = null,
//    ) = removeChannelMembers(
//        channel = channel,
//        uuids = uuids,
//        limit = limit,
//        page = page,
//        filter = filter,
//        sort = sort,
//        includeCount = includeCount,
//        includeCustom = includeCustom,
//        includeUUIDDetails = includeUUIDDetails,
//    )
//
//    /**
//     * Remove members from a Channel.
//     *
//     * @param channel Channel name
//     * @param uuids Members to remove from channel.
//     * @param limit Number of objects to return in the response.
//     *              Default is 100, which is also the maximum value.
//     *              Set limit to 0 (zero) and includeCount to true if you want to retrieve only a result count.
//     * @param page Use for pagination.
//     *              - [PNNext] : Previously-returned cursor bookmark for fetching the next page.
//     *              - [PNPrev] : Previously-returned cursor bookmark for fetching the previous page.
//     *                           Ignored if you also supply the start parameter.
//     * @param filter Expression used to filter the results. Only objects whose properties satisfy the given
//     *               expression are returned.
//     * @param sort List of properties to sort by. Available options are id, name, and updated.
//     *             @see [PNAsc], [PNDesc]
//     * @param includeCount Request totalCount to be included in paginated response. By default, totalCount is omitted.
//     *                     Default is `false`.
//     * @param includeCustom Include respective additional fields in the response.
//     * @param includeUUIDDetails Include custom fields for UUIDs metadata.
//     */
//    fun removeChannelMembers(
//        channel: String,
//        uuids: List<String>,
//        limit: Int? = null,
//        page: PNPage? = null,
//        filter: String? = null,
//        sort: Collection<PNSortKey<PNMemberKey>> = listOf(),
//        includeCount: Boolean = false,
//        includeCustom: Boolean = false,
//        includeUUIDDetails: PNUUIDDetailsLevel? = null,
//        includeType: Boolean = false,
//    ) = manageChannelMembers(
//        channel = channel,
//        uuidsToSet = listOf(),
//        uuidsToRemove = uuids,
//        limit = limit,
//        page = page,
//        filter = filter,
//        sort = sort,
//        includeCount = includeCount,
//        includeCustom = includeCustom,
//        includeUUIDDetails = includeUUIDDetails,
//        includeType = includeType,
//    )
//
//    /**
//     * Set or remove members in a channel.
//     *
//     * @param channel Channel name
//     * @param uuidsToSet Collection of members to add to the channel. @see [com.pubnub.api.models.consumer.objects.member.PNMember.Partial]
//     * @param uuidsToRemove Members to remove from channel.
//     * @param limit Number of objects to return in the response.
//     *              Default is 100, which is also the maximum value.
//     *              Set limit to 0 (zero) and includeCount to true if you want to retrieve only a result count.
//     * @param page Use for pagination.
//     *              - [PNNext] : Previously-returned cursor bookmark for fetching the next page.
//     *              - [PNPrev] : Previously-returned cursor bookmark for fetching the previous page.
//     *                           Ignored if you also supply the start parameter.
//     * @param filter Expression used to filter the results. Only objects whose properties satisfy the given
//     *               expression are returned.
//     * @param sort List of properties to sort by. Available options are id, name, and updated.
//     *             @see [PNAsc], [PNDesc]
//     * @param includeCount Request totalCount to be included in paginated response. By default, totalCount is omitted.
//     *                     Default is `false`.
//     * @param includeCustom Include respective additional fields in the response.
//     * @param includeUUIDDetails Include custom fields for UUIDs metadata.
//     */
//    fun manageChannelMembers(
//        channel: String,
//        uuidsToSet: Collection<MemberInput>,
//        uuidsToRemove: Collection<String>,
//        limit: Int? = null,
//        page: PNPage? = null,
//        filter: String? = null,
//        sort: Collection<PNSortKey<PNMemberKey>> = listOf(),
//        includeCount: Boolean = false,
//        includeCustom: Boolean = false,
//        includeUUIDDetails: PNUUIDDetailsLevel? = null,
//        includeType: Boolean = false,
//    ) = ManageChannelMembersEndpoint(
//        pubnub = this,
//        channel = channel,
//        uuidsToSet = uuidsToSet,
//        uuidsToRemove = uuidsToRemove,
//        collectionQueryParameters =
//            CollectionQueryParameters(
//                limit = limit,
//                page = page,
//                filter = filter,
//                sort = sort,
//                includeCount = includeCount,
//            ),
//        includeQueryParam =
//            IncludeQueryParam(
//                includeCustom = includeCustom,
//                includeUUIDDetails = includeUUIDDetails,
//                includeUuidType = includeType,
//            ),
//    )
//
//    //endregion ObjectsAPI
//
//    //region files
//
//    /**
//     * Upload file / data to specified Channel.
//     *
//     * @param channel Channel name
//     * @param fileName Name of the file to send.
//     * @param inputStream Input stream with file content. The inputStream will be depleted after the call.
//     * @param message The payload.
//     *                **Warning:** It is important to note that you should not serialize JSON
//     *                when sending signals/messages via PubNub.
//     *                Why? Because the serialization is done for you automatically.
//     *                Instead just pass the full object as the message payload.
//     *                PubNub takes care of everything for you.
//     * @param meta Metadata object which can be used with the filtering ability.
//     * @param ttl Set a per message time to live in storage.
//     *            - If `shouldStore = true`, and `ttl = 0`, the message is stored
//     *              with no expiry time.
//     *            - If `shouldStore = true` and `ttl = X` (`X` is an Integer value),
//     *              the message is stored with an expiry time of `X` hours.
//     *            - If `shouldStore = false`, the `ttl` parameter is ignored.
//     *            - If ttl isn't specified, then expiration of the message defaults
//     *              back to the expiry value for the key.
//     * @param shouldStore Store in history.
//     *                    If not specified, then the history configuration of the key is used.
//     * @param cipherKey Key to be used to encrypt uploaded data.
//     */
//    fun sendFile(
//        channel: String,
//        fileName: String,
//        inputStream: InputStream,
//        message: Any? = null,
//        meta: Any? = null,
//        ttl: Int? = null,
//        shouldStore: Boolean? = null,
//        cipherKey: String? = null,
//    ): SendFileEndpoint {
//        val cryptoModule =
//            if (cipherKey != null) {
//                CryptoModule.createLegacyCryptoModule(cipherKey)
//            } else {
//                configuration.cryptoModule
//            }
//
//        return SendFileEndpoint(
//            channel = channel,
//            fileName = fileName,
//            inputStream = inputStream,
//            message = message,
//            meta = meta,
//            ttl = ttl,
//            shouldStore = shouldStore,
//            executorService =
//                retrofitManager.getTransactionClientExecutorService()
//                    ?: Executors.newSingleThreadExecutor(),
//            fileMessagePublishRetryLimit = configuration.fileMessagePublishRetryLimit,
//            generateUploadUrlFactory = GenerateUploadUrlEndpoint.Factory(this),
//            publishFileMessageFactory = PublishFileMessageEndpoint.Factory(this),
//            sendFileToS3Factory = UploadFileEndpoint.Factory(this),
//            cryptoModule = cryptoModule,
//        )
//    }
//
//    /**
//     * Retrieve list of files uploaded to Channel.
//     *
//     * @param channel Channel name
//     * @param limit Number of files to return. Minimum value is 1, and maximum is 100. Default value is 100.
//     * @param next Previously-returned cursor bookmark for fetching the next page. @see [PNPage.PNNext]
//     */
//    fun listFiles(
//        channel: String,
//        limit: Int? = null,
//        next: PNPage.PNNext? = null,
//    ): ListFilesEndpoint {
//        return ListFilesEndpoint(
//            pubNub = this,
//            channel = channel,
//            limit = limit,
//            next = next,
//        )
//    }
//
//    /**
//     * Generate URL which can be used to download file from target Channel.
//     *
//     * @param channel Name of channel to which the file has been uploaded.
//     * @param fileName Name under which the uploaded file is stored.
//     * @param fileId Unique identifier for the file, assigned during upload.
//     */
//    fun getFileUrl(
//        channel: String,
//        fileName: String,
//        fileId: String,
//    ): GetFileUrlEndpoint {
//        return GetFileUrlEndpoint(
//            pubNub = this,
//            channel = channel,
//            fileName = fileName,
//            fileId = fileId,
//        )
//    }
//
//    /**
//     * Download file from specified Channel.
//     *
//     * @param channel Name of channel to which the file has been uploaded.
//     * @param fileName Name under which the uploaded file is stored.
//     * @param fileId Unique identifier for the file, assigned during upload.
//     * @param cipherKey Key to be used to decrypt downloaded data. If a key is not provided,
//     *                  the SDK uses the cipherKey from the @see [PNConfiguration].
//     */
//    fun downloadFile(
//        channel: String,
//        fileName: String,
//        fileId: String,
//        cipherKey: String? = null,
//    ): DownloadFileEndpoint {
//        val cryptoModule =
//            if (cipherKey != null) {
//                CryptoModule.createLegacyCryptoModule(cipherKey)
//            } else {
//                configuration.cryptoModule
//            }
//
//        return DownloadFileEndpoint(
//            pubNub = this,
//            channel = channel,
//            fileName = fileName,
//            fileId = fileId,
//            cryptoModule = cryptoModule,
//        )
//    }
//
//    /**
//     * Delete file from specified Channel.
//     *
//     * @param channel Name of channel to which the file has been uploaded.
//     * @param fileName Name under which the uploaded file is stored.
//     * @param fileId Unique identifier for the file, assigned during upload.
//     */
//    fun deleteFile(
//        channel: String,
//        fileName: String,
//        fileId: String,
//    ): DeleteFileEndpoint {
//        return DeleteFileEndpoint(
//            pubNub = this,
//            channel = channel,
//            fileName = fileName,
//            fileId = fileId,
//        )
//    }
//
//    /**
//     * Publish file message from specified Channel.
//     * @param channel Name of channel to which the file has been uploaded.
//     * @param fileName Name under which the uploaded file is stored.
//     * @param fileId Unique identifier for the file, assigned during upload.
//     * @param message The payload.
//     *                **Warning:** It is important to note that you should not serialize JSON
//     *                when sending signals/messages via PubNub.
//     *                Why? Because the serialization is done for you automatically.
//     *                Instead just pass the full object as the message payload.
//     *                PubNub takes care of everything for you.
//     * @param meta Metadata object which can be used with the filtering ability.
//     * @param ttl Set a per message time to live in storage.
//     *            - If `shouldStore = true`, and `ttl = 0`, the message is stored
//     *              with no expiry time.
//     *            - If `shouldStore = true` and `ttl = X` (`X` is an Integer value),
//     *              the message is stored with an expiry time of `X` hours.
//     *            - If `shouldStore = false`, the `ttl` parameter is ignored.
//     *            - If ttl isn't specified, then expiration of the message defaults
//     *              back to the expiry value for the key.
//     * @param shouldStore Store in history.
//     *                    If not specified, then the history configuration of the key is used.
//     *
//     */
//    fun publishFileMessage(
//        channel: String,
//        fileName: String,
//        fileId: String,
//        message: Any? = null,
//        meta: Any? = null,
//        ttl: Int? = null,
//        shouldStore: Boolean? = null,
//    ): PublishFileMessageEndpoint {
//        return PublishFileMessageEndpoint(
//            pubNub = this,
//            channel = channel,
//            fileName = fileName,
//            fileId = fileId,
//            message = message,
//            meta = meta,
//            ttl = ttl,
//            shouldStore = shouldStore,
//        )
//    }
//    //endregion
//
//    //region Encryption
//
//    /**
//     * Perform Cryptographic decryption of an input string using cipher key provided by [PNConfiguration.cipherKey].
//     *
//     * @param inputString String to be decrypted.
//     *
//     * @return String containing the decryption of `inputString` using `cipherKey`.
//     * @throws PubNubException throws exception in case of failed decryption.
//     */
//    @Throws(PubNubException::class)
//    fun decrypt(inputString: String): String = decrypt(inputString, null)
//
//    /**
//     * Perform Cryptographic decryption of an input string using a cipher key.
//     *
//     * @param inputString String to be decrypted.
//     * @param cipherKey cipher key to be used for decryption. Default is [PNConfiguration.cipherKey]
//     *
//     * @return String containing the decryption of `inputString` using `cipherKey`.
//     * @throws PubNubException throws exception in case of failed decryption.
//     */
//    @Throws(PubNubException::class)
//    fun decrypt(
//        inputString: String,
//        cryptoModule: CryptoModule? = null,
//    ): String = getCryptoModuleOrThrow(cryptoModule).decryptString(inputString)
//
//    /**
//     * Perform Cryptographic decryption of an input stream using provided cipher key.
//     *
//     * @param inputStream InputStream to be encrypted.
//     * @param cipherKey Cipher key to be used for decryption.
//     *
//     * @return InputStream containing the encryption of `inputStream` using `cipherKey`.
//     * @throws PubNubException Throws exception in case of failed decryption.
//     */
//    @Throws(PubNubException::class)
//    fun decryptInputStream(
//        inputStream: InputStream,
//        cryptoModule: CryptoModule? = null,
//    ): InputStream = getCryptoModuleOrThrow(cryptoModule).decryptStream(inputStream)
//
//    /**
//     * Perform Cryptographic encryption of an input string and a cipher key.
//     *
//     * @param inputString String to be encrypted.
//     * @param cipherKey Cipher key to be used for encryption. Default is [PNConfiguration.cipherKey]
//     *
//     * @return String containing the encryption of `inputString` using `cipherKey`.
//     * @throws PubNubException Throws exception in case of failed encryption.
//     */
//    @Throws(PubNubException::class)
//    fun encrypt(
//        inputString: String,
//        cryptoModule: CryptoModule? = null,
//    ): String = getCryptoModuleOrThrow(cryptoModule).encryptString(inputString)
//
//    /**
//     * Perform Cryptographic encryption of an input stream using provided cipher key.
//     *
//     * @param inputStream InputStream to be encrypted.
//     * @param cipherKey Cipher key to be used for encryption.
//     *
//     * @return InputStream containing the encryption of `inputStream` using `cipherKey`.
//     * @throws PubNubException Throws exception in case of failed encryption.
//     */
//    @Throws(PubNubException::class)
//    fun encryptInputStream(
//        inputStream: InputStream,
//        cryptoModule: CryptoModule? = null,
//    ): InputStream = getCryptoModuleOrThrow(cryptoModule).encryptStream(inputStream)
//
//    @Throws(PubNubException::class)
//    private fun getCryptoModuleOrThrow(cryptoModule: CryptoModule? = null): CryptoModule {
//        return cryptoModule ?: configuration.cryptoModule ?: throw PubNubException("Crypto module is not initialized")
//    }
//    //endregion
//
//
// }
