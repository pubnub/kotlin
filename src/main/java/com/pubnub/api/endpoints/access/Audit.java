package com.pubnub.api.endpoints.access;

import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.PubnubException;
import com.pubnub.api.core.PubnubUtil;
import com.pubnub.api.core.enums.PNOperationType;
import com.pubnub.api.core.models.Envelope;
import com.pubnub.api.core.models.consumer_facing.PNAccessManagerAuditData;
import com.pubnub.api.core.models.consumer_facing.PNAccessManagerAuditResult;
import com.pubnub.api.endpoints.Endpoint;
import lombok.Setter;
import lombok.experimental.Accessors;
import retrofit2.Call;
import retrofit2.Response;

import java.util.List;
import java.util.Map;

@Accessors(chain = true, fluent = true)
public class Audit extends Endpoint<Envelope<PNAccessManagerAuditData>, PNAccessManagerAuditResult> {

    @Setter private List<String> authKeys;
    @Setter private String channel;
    @Setter private String channelGroup;

    public Audit(Pubnub pubnub) {
        super(pubnub);
    }

    @Override
    protected boolean validateParams() {
        if (pubnub.getConfiguration().getSecretKey() != null && pubnub.getConfiguration().getSecretKey().length() == 0) {
            return false;
        }

        return true;
    }

    @Override
    protected Call<Envelope<PNAccessManagerAuditData>> doWork(Map<String, String> queryParams) throws PubnubException {
        String signature;

        int timestamp = pubnub.getTimestamp();

        String signInput = this.pubnub.getConfiguration().getSubscribeKey() + "\n"
                + this.pubnub.getConfiguration().getPublishKey() + "\n"
                + "audit" + "\n";

        queryParams.put("timestamp", String.valueOf(timestamp));

        if (channel != null) {
            queryParams.put("channel", channel);
        }

        if (channelGroup != null) {
            queryParams.put("channel-group", channelGroup);
        }

        if (authKeys.size() > 0) {
            queryParams.put("auth", PubnubUtil.joinString(authKeys, ","));
        }

        signInput += PubnubUtil.preparePamArguments(queryParams);

        signature = PubnubUtil.signSHA256(this.pubnub.getConfiguration().getSecretKey(), signInput);

        queryParams.put("signature", signature);

        AccessManagerService service = this.createRetrofit().create(AccessManagerService.class);
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
