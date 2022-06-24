package com.pubnub.api.endpoints.access;

import com.google.gson.JsonElement;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.PubNubUtil;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.managers.MapperManager;
import com.pubnub.api.managers.RetrofitManager;
import com.pubnub.api.managers.TelemetryManager;
import com.pubnub.api.managers.token_manager.TokenManager;
import com.pubnub.api.models.consumer.access_manager.PNAccessManagerGrantResult;
import com.pubnub.api.models.consumer.access_manager.PNAccessManagerKeyData;
import com.pubnub.api.models.server.Envelope;
import com.pubnub.api.models.server.access_manager.AccessManagerGrantPayload;
import lombok.Setter;
import lombok.experimental.Accessors;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


@Accessors(chain = true, fluent = true)
public class Grant extends Endpoint<Envelope<AccessManagerGrantPayload>, PNAccessManagerGrantResult> {

    @Setter
    private boolean read;
    @Setter
    private boolean write;
    @Setter
    private boolean manage;
    @Setter
    private boolean delete;
    @Setter
    private boolean get;
    @Setter
    private boolean update;
    @Setter
    private boolean join;
    @Setter
    private Integer ttl;

    @Setter
    private List<String> authKeys;
    @Setter
    private List<String> channels;
    @Setter
    private List<String> channelGroups;
    @Setter
    private List<String> uuids = Collections.emptyList();

    public Grant(PubNub pubnub,
                 TelemetryManager telemetryManager,
                 RetrofitManager retrofit,
                 TokenManager tokenManager) {
        super(pubnub, telemetryManager, retrofit, tokenManager);
        authKeys = new ArrayList<>();
        channels = new ArrayList<>();
        channelGroups = new ArrayList<>();
    }

    @Override
    protected List<String> getAffectedChannels() {
        return channels;
    }

    @Override
    protected List<String> getAffectedChannelGroups() {
        return channelGroups;
    }

    @Override
    protected void validateParams() throws PubNubException {
        if (this.getPubnub().getConfiguration().getSecretKey() == null || this.getPubnub()
                .getConfiguration()
                .getSecretKey()
                .isEmpty()) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_SECRET_KEY_MISSING).build();
        }
        if (this.getPubnub().getConfiguration().getSubscribeKey() == null || this.getPubnub()
                .getConfiguration()
                .getSubscribeKey()
                .isEmpty()) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_SUBSCRIBE_KEY_MISSING).build();
        }
        if (this.getPubnub().getConfiguration().getPublishKey() == null || this.getPubnub()
                .getConfiguration()
                .getPublishKey()
                .isEmpty()) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_PUBLISH_KEY_MISSING).build();
        }
        if ((!channels.isEmpty() || !channelGroups.isEmpty()) && !uuids.isEmpty()) {
            throw PubNubException.builder()
                    .pubnubError(PubNubErrorBuilder.PNERROBJ_INVALID_ARGUMENTS)
                    .errormsg("Grants for channels or channelGroups can't be changed together with grants for UUIDs")
                    .build();
        }
        if (!uuids.isEmpty() && authKeys.isEmpty()) {
            throw PubNubException.builder()
                    .pubnubError(PubNubErrorBuilder.PNERROBJ_INVALID_ARGUMENTS)
                    .errormsg("UUIDs grant management require providing non empty authKeys")
                    .build();
        }
    }

    @Override
    protected Call<Envelope<AccessManagerGrantPayload>> doWork(Map<String, String> queryParams) throws PubNubException {

        if (channels.size() > 0) {
            queryParams.put("channel", PubNubUtil.joinString(channels, ","));
        }

        if (channelGroups.size() > 0) {
            queryParams.put("channel-group", PubNubUtil.joinString(channelGroups, ","));
        }

        if (uuids.size() > 0) {
            queryParams.put("target-uuid", PubNubUtil.joinString(uuids, ","));
        }

        if (authKeys.size() > 0) {
            queryParams.put("auth", PubNubUtil.joinString(authKeys, ","));
        }

        if (ttl != null && ttl >= -1) {
            queryParams.put("ttl", String.valueOf(ttl));
        }

        queryParams.put("r", (read) ? "1" : "0");
        queryParams.put("w", (write) ? "1" : "0");
        queryParams.put("m", (manage) ? "1" : "0");
        queryParams.put("d", (delete) ? "1" : "0");
        queryParams.put("g", get ? "1" : "0");
        queryParams.put("u", update ? "1" : "0");
        queryParams.put("j", join ? "1" : "0");

        return this.getRetrofit()
                .getAccessManagerService()
                .grant(this.getPubnub().getConfiguration().getSubscribeKey(), queryParams);
    }

    @Override
    protected PNAccessManagerGrantResult createResponse(Response<Envelope<AccessManagerGrantPayload>> input) throws
            PubNubException {
        MapperManager mapperManager = getPubnub().getMapper();
        PNAccessManagerGrantResult.PNAccessManagerGrantResultBuilder pnAccessManagerGrantResult =
                PNAccessManagerGrantResult.builder();

        if (input.body() == null || input.body().getPayload() == null) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_PARSING_ERROR).build();
        }

        AccessManagerGrantPayload data = input.body().getPayload();
        Map<String, Map<String, PNAccessManagerKeyData>> constructedChannels = new HashMap<>();
        Map<String, Map<String, PNAccessManagerKeyData>> constructedGroups = new HashMap<>();

        // we have a case of a singular channel.
        if (data.getChannel() != null) {
            constructedChannels.put(data.getChannel(), data.getAuthKeys());
        }

        if (channelGroups != null) {
            if (channelGroups.size() == 1) {
                constructedGroups.put(mapperManager.elementToString(data.getChannelGroups()), data.getAuthKeys());
            } else if (channelGroups.size() > 1) {
                Iterator<Map.Entry<String, JsonElement>> it = mapperManager.getObjectIterator(data.getChannelGroups());
                while (it.hasNext()) {
                    Map.Entry<String, JsonElement> channelGroup = it.next();
                    constructedGroups.put(channelGroup.getKey(), createKeyMap(channelGroup.getValue()));
                }
            }
        }

        if (data.getChannels() != null) {
            for (String fetchedChannel : data.getChannels().keySet()) {
                constructedChannels.put(fetchedChannel, data.getChannels().get(fetchedChannel).getAuthKeys());
            }
        }

        Map<String, Map<String, PNAccessManagerKeyData>> constructedUuids = new HashMap<>();

        if (data.getUuids() != null) {
            for (String fetchedUuid : data.getUuids().keySet()) {
                constructedUuids.put(fetchedUuid, data.getUuids().get(fetchedUuid).getAuthKeys());
            }
        }

        return pnAccessManagerGrantResult
                .subscribeKey(data.getSubscribeKey())
                .level(data.getLevel())
                .ttl(data.getTtl())
                .channels(constructedChannels)
                .channelGroups(constructedGroups)
                .uuids(constructedUuids)
                .build();
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNAccessManagerGrant;
    }

    @Override
    protected boolean isAuthRequired() {
        return false;
    }

    private Map<String, PNAccessManagerKeyData> createKeyMap(JsonElement input) {
        Map<String, PNAccessManagerKeyData> result = new HashMap<>();
        MapperManager mapper = getPubnub().getMapper();

        Iterator<Map.Entry<String, JsonElement>> it = mapper.getObjectIterator(input, "auths");
        while (it.hasNext()) {
            Map.Entry<String, JsonElement> keyMap = it.next();
            PNAccessManagerKeyData pnAccessManagerKeyData = PNAccessManagerKeyData.builder()
                    .manageEnabled(mapper.getAsBoolean(keyMap.getValue(), "m"))
                    .writeEnabled(mapper.getAsBoolean(keyMap.getValue(), "w"))
                    .readEnabled(mapper.getAsBoolean(keyMap.getValue(), "r"))
                    .deleteEnabled(mapper.getAsBoolean(keyMap.getValue(), "d"))
                    .getEnabled(mapper.getAsBoolean(keyMap.getValue(), "g"))
                    .updateEnabled(mapper.getAsBoolean(keyMap.getValue(), "u"))
                    .joinEnabled(mapper.getAsBoolean(keyMap.getValue(), "j")).build();

            result.put(keyMap.getKey(), pnAccessManagerKeyData);
        }

        return result;
    }

}
