package com.pubnub.api.endpoints.access;

import com.pubnub.api.core.PnResponse;
import com.pubnub.api.core.Pubnub;
import com.pubnub.api.core.PubnubException;
import com.pubnub.api.core.PubnubUtil;
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
public class Audit extends Endpoint<Object, Object> {

    private Pubnub pubnub;
    @Singular private List<String> authKeys;
    @Singular private List<String> channels;
    @Singular private List<String> channelGroups;


    @Override
    protected boolean validateParams() {
        if (pubnub.getConfiguration().getSecretKey().length() == 0) {
            return false;
        }

        return true;
    }

    @Override
    protected Call<Object> doWork() throws PubnubException {
        String signature;
        Map<String, Object> queryParams = new HashMap<String, Object>();
        Map<String, String> signParams = new HashMap<String, String>();
        int timestamp = (int) ((new Date().getTime()) / 1000);

        String signInput = this.pubnub.getConfiguration().getSubscribeKey() + "\n"
                + this.pubnub.getConfiguration().getPublishKey() + "\n"
                + "audit" + "\n";

        if (channels.size() > 0) {
            signParams.put("channel", PubnubUtil.joinString(channels, ","));
        }

        if (channelGroups.size() > 0) {
            signParams.put("channel-group", PubnubUtil.joinString(channelGroups, ","));
        }

        if (authKeys.size() > 0) {
            signParams.put("auth", PubnubUtil.joinString(authKeys, ","));
        }

        signInput += PubnubUtil.preparePamArguments(signParams);

        signature = signSHA256(this.pubnub.getConfiguration().getSecretKey(), signInput);

        queryParams.put("timestamp", String.valueOf(timestamp));
        queryParams.put("signature", signature);

        AccessManagerService service = this.createRetrofit(pubnub).create(AccessManagerService.class);
        return service.audit(pubnub.getConfiguration().getSubscribeKey(), queryParams);
    }

    @Override
    protected PnResponse<Object> createResponse(final Response<Object> input) throws PubnubException {
        return null;
    }
}
