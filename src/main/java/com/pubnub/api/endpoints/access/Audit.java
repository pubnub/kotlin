package com.pubnub.api.endpoints.access;

import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.PubnubException;
import com.pubnub.api.core.PubnubUtil;
import com.pubnub.api.core.enums.PNOperationType;
import com.pubnub.api.core.models.Envelope;
import com.pubnub.api.core.models.consumer_facing.PNAccessManagerAuditData;
import com.pubnub.api.core.models.consumer_facing.PNAccessManagerAuditResult;
import com.pubnub.api.endpoints.Endpoint;
import lombok.Builder;
import lombok.Singular;
import retrofit2.Call;
import retrofit2.Response;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder
public class Audit extends Endpoint<Envelope<PNAccessManagerAuditData>, PNAccessManagerAuditResult> {

    private Pubnub pubnub;
    @Singular private List<String> authKeys;
    private String channel;
    private String channelGroup;


    @Override
    protected boolean validateParams() {
        if (pubnub.getConfiguration().getSecretKey() != null && pubnub.getConfiguration().getSecretKey().length() == 0) {
            return false;
        }

        return true;
    }

    @Override
    protected Call<Envelope<PNAccessManagerAuditData>> doWork() throws PubnubException {
        String signature;
        Map<String, Object> queryParams = new HashMap<>();
        Map<String, String> signParams = new HashMap<>();
        int timestamp = (int) ((new Date().getTime()) / 1000);

        String signInput = this.pubnub.getConfiguration().getSubscribeKey() + "\n"
                + this.pubnub.getConfiguration().getPublishKey() + "\n"
                + "audit" + "\n";

        signParams.put("timestamp", String.valueOf(timestamp));

        if (channel != null) {
            signParams.put("channel", channel);
            queryParams.put("channel", channel);
        }

        if (channelGroup != null) {
            signParams.put("channel-group", channelGroup);
            queryParams.put("channel-group", channelGroup);
        }

        if (authKeys.size() > 0) {
            signParams.put("auth", PubnubUtil.joinString(authKeys, ","));
            queryParams.put("auth", PubnubUtil.joinString(authKeys, ","));
        }

        signInput += PubnubUtil.preparePamArguments(signParams);

        signature = signSHA256(this.pubnub.getConfiguration().getSecretKey(), signInput);

        queryParams.put("timestamp", String.valueOf(timestamp));
        queryParams.put("signature", signature);

        AccessManagerService service = this.createRetrofit(pubnub).create(AccessManagerService.class);
        return service.audit(pubnub.getConfiguration().getSubscribeKey(), queryParams);
    }

    @Override
    protected PNAccessManagerAuditResult createResponse(final Response<Envelope<PNAccessManagerAuditData>> input) throws PubnubException {
        PNAccessManagerAuditResult pnAccessManagerAuditResult = new PNAccessManagerAuditResult();

        pnAccessManagerAuditResult.setData(input.body().getPayload());

        return pnAccessManagerAuditResult;
    }

    protected int getConnectTimeout() {
        return pubnub.getConfiguration().getConnectTimeout();
    }

    protected int getRequestTimeout() {
        return pubnub.getConfiguration().getNonSubscribeRequestTimeout();
    }

    @Override
    protected PNOperationType getOperationType() {
        return PNOperationType.PNAccessManagerAudit;
    }

}
