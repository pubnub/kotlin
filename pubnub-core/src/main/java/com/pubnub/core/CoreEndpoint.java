package com.pubnub.core;

import retrofit2.Call;
import retrofit2.Response;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class CoreEndpoint<Input, Output, Callback, E extends Throwable, OperationType extends com.pubnub.core.OperationType, Conf extends CoreConfiguration, TokenManager, TelemetryManager, RetrofitManager extends com.pubnub.core.RetrofitManager, Mapper> implements CoreRemoteAction<Output, Callback, E> {

    private final ManagerHolder<Conf, TokenManager, TelemetryManager, RetrofitManager, Mapper> managerHolder;

    public CoreEndpoint(ManagerHolder<Conf, TokenManager, TelemetryManager, RetrofitManager, Mapper> managerHolder) {

        this.managerHolder = managerHolder;
    }

    protected boolean isAuthRequired() {
        return true;
    }


    protected List<String> getAffectedChannels() {
        return Collections.emptyList();
    }

    protected List<String> getAffectedChannelGroups() {
        return Collections.emptyList();
    }

    protected boolean isSubKeyRequired() {
        return true;
    }

    protected boolean isPubKeyRequired() {
        return true;
    }

    protected abstract Call<Input> doWork(Map<String, String> baseParams) throws E;

    protected abstract Output createResponse(Response<Input> input) throws E;

    protected void validateParams() throws E {
        if (isSubKeyRequired() && (managerHolder.getConfiguration().subKey() == null || managerHolder.getConfiguration().subKey().isEmpty())) {
            //
        }
        if (isPubKeyRequired() && (managerHolder.getConfiguration().pubKey() == null || managerHolder.getConfiguration().pubKey().isEmpty())) {
            //
        }
    }


    protected abstract OperationType operationType();
}
