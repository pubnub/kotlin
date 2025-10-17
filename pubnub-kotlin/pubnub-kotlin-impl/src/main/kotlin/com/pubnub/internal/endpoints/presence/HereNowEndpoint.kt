package com.pubnub.internal.endpoints.presence

import com.google.gson.JsonElement
import com.pubnub.api.endpoints.presence.HereNow
import com.pubnub.api.enums.PNOperationType
import com.pubnub.api.logging.LogMessage
import com.pubnub.api.logging.LogMessageContent
import com.pubnub.api.models.consumer.presence.PNHereNowChannelData
import com.pubnub.api.models.consumer.presence.PNHereNowOccupantData
import com.pubnub.api.models.consumer.presence.PNHereNowResult
import com.pubnub.api.retry.RetryableEndpointGroup
import com.pubnub.internal.EndpointCore
import com.pubnub.internal.PubNubImpl
import com.pubnub.internal.logging.LoggerManager
import com.pubnub.internal.logging.PNLogger
import com.pubnub.internal.models.server.Envelope
import com.pubnub.internal.toCsv
import retrofit2.Call
import retrofit2.Response

private const val MAX_CHANNEL_OCCUPANTS_LIMIT = 1000

/**
 * @see [PubNubImpl.hereNow]
 */
class HereNowEndpoint internal constructor(
    pubnub: PubNubImpl,
    override val channels: List<String> = emptyList(),
    override val channelGroups: List<String> = emptyList(),
    override val includeState: Boolean = false,
    override val includeUUIDs: Boolean = true,
    override val limit: Int = MAX_CHANNEL_OCCUPANTS_LIMIT,
    override val offset: Int? = null,
) : EndpointCore<Envelope<JsonElement>, PNHereNowResult>(pubnub), HereNow {
    private val log: PNLogger = LoggerManager.instance.getLogger(pubnub.logConfig, this::class.java)

    private fun isGlobalHereNow() = channels.isEmpty() && channelGroups.isEmpty()

    override fun getAffectedChannels() = channels

    override fun getAffectedChannelGroups() = channelGroups

    override fun doWork(queryParams: HashMap<String, String>): Call<Envelope<JsonElement>> {
        log.debug(
            LogMessage(
                message = LogMessageContent.Object(
                    arguments = mapOf(
                        "channels" to channels,
                        "channelGroups" to channelGroups,
                        "includeState" to includeState,
                        "includeUUIDs" to includeUUIDs,
                        "limit" to limit,
                        "offset" to (offset?.toString() ?: "null"),
                        "isGlobalHereNow" to isGlobalHereNow(),
                    ),
                    operation = this::class.simpleName
                ),
                details = "HereNow API call",
            )
        )

        addQueryParams(queryParams)

        return if (!isGlobalHereNow()) {
            retrofitManager.presenceService.hereNow(
                configuration.subscribeKey,
                channels.toCsv(),
                queryParams,
            )
        } else {
            retrofitManager.presenceService.globalHereNow(
                configuration.subscribeKey,
                queryParams,
            )
        }
    }

    override fun createResponse(input: Response<Envelope<JsonElement>>): PNHereNowResult {
        return if (isGlobalHereNow() || (channels.size > 1 || channelGroups.isNotEmpty())) {
            parseMultipleChannelResponse(input.body()!!.payload!!)
        } else {
            parseSingleChannelResponse(input.body()!!)
        }
    }

    override fun operationType() = PNOperationType.PNHereNowOperation

    override fun getEndpointGroupName(): RetryableEndpointGroup = RetryableEndpointGroup.PRESENCE

    private fun parseSingleChannelResponse(input: Envelope<JsonElement>): PNHereNowResult {
        val occupants = if (includeUUIDs) {
            prepareOccupantData(input.uuids!!)
        } else {
            emptyList()
        }

        val pnHereNowResult = PNHereNowResult(
            totalChannels = 1,
            totalOccupancy = input.occupancy,
        )

        // When includeUUIDs = false, occupants list will be empty but channel data is still present
        val pnHereNowChannelData = PNHereNowChannelData(
            channelName = channels[0],
            occupancy = input.occupancy,
            occupants = occupants
        )
        pnHereNowResult.channels[channels[0]] = pnHereNowChannelData

        return pnHereNowResult
    }

    private fun parseMultipleChannelResponse(input: JsonElement): PNHereNowResult {
        val channels = pubnub.mapper.getObjectIterator(input, "channels")

        val channelsMap = mutableMapOf<String, PNHereNowChannelData>()
        while (channels.hasNext()) {
            val entry = channels.next()
            val occupants = if (includeUUIDs) {
                prepareOccupantData(pubnub.mapper.getField(entry.value, "uuids")!!)
            } else {
                emptyList()
            }

            val pnHereNowChannelData = PNHereNowChannelData(
                channelName = entry.key,
                occupancy = pubnub.mapper.elementToInt(entry.value, "occupancy"),
                occupants = occupants
            )
            channelsMap[entry.key] = pnHereNowChannelData
        }

        val pnHereNowResult = PNHereNowResult(
            totalChannels = pubnub.mapper.elementToInt(input, "total_channels"),
            totalOccupancy = pubnub.mapper.elementToInt(input, "total_occupancy"),
            channels = channelsMap,
        )

        return pnHereNowResult
    }

    private fun prepareOccupantData(input: JsonElement): MutableList<PNHereNowOccupantData> {
        val occupantsResults = mutableListOf<PNHereNowOccupantData>()

        val it = pubnub.mapper.getArrayIterator(input)
        while (it?.hasNext()!!) {
            val occupant = it.next()
            occupantsResults.add(
                if (includeState) {
                    PNHereNowOccupantData(
                        uuid = pubnub.mapper.elementToString(occupant, "uuid")!!,
                        state = pubnub.mapper.getField(occupant, "state"),
                    )
                } else {
                    PNHereNowOccupantData(
                        uuid = pubnub.mapper.elementToString(occupant)!!,
                    )
                },
            )
        }

        return occupantsResults
    }

    private fun addQueryParams(queryParams: MutableMap<String, String>) {
        if (includeState) {
            queryParams["state"] = "1"
        }
        if (!includeUUIDs) {
            queryParams["disable_uuids"] = "1"
        }
        if (channelGroups.isNotEmpty()) {
            queryParams["channel-group"] = channelGroups.toCsv()
        }
        queryParams["limit"] = limit.toString()
        offset?.let { queryParams["offset"] = it.toString() }
    }
}
