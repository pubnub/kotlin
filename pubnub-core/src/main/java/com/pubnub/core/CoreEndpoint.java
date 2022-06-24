package com.pubnub.core;

import retrofit2.Call;
import retrofit2.Response;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class CoreEndpoint<Input, Output, Callback, E extends Throwable, OperationType extends com.pubnub.core.OperationType, Conf extends CoreConfiguration, TokenManager, TelemetryManager, RetrofitManager extends com.pubnub.core.RetrofitManager, Mapper> implements CoreRemoteAction<Output, Callback, E> {

    private final ManagerManager<Conf, TokenManager, TelemetryManager, RetrofitManager, Mapper> managerManager;

    public CoreEndpoint(ManagerManager<Conf, TokenManager, TelemetryManager, RetrofitManager, Mapper> managerManager) {

        this.managerManager = managerManager;
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
        if (isSubKeyRequired() && (managerManager.getConfiguration().subKey() == null || managerManager.getConfiguration().subKey().isEmpty())) {
            //
        }
        if (isPubKeyRequired() && (managerManager.getConfiguration().pubKey() == null || managerManager.getConfiguration().pubKey().isEmpty())) {
            //
        }
    }


    protected abstract OperationType operationType();
}
