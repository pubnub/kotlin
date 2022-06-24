package com.pubnub.api.endpoints.remoteaction;

import com.pubnub.api.PubNubException;
import com.pubnub.api.callbacks.PNCallback;

public interface RemoteAction<Output> extends com.pubnub.core.CoreRemoteAction<Output, PNCallback<Output>, PubNubException> {

}
