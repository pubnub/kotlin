package com.pubnub.api.java.endpoints.files;

import com.pubnub.api.java.endpoints.BuilderSteps;
import com.pubnub.api.java.endpoints.Endpoint;
import com.pubnub.api.models.consumer.files.PNListFilesResult;

public interface ListFiles extends Endpoint<PNListFilesResult> {
    ListFiles limit(Integer limit);

    ListFiles next(com.pubnub.api.models.consumer.objects.PNPage.PNNext next);

    interface Builder extends BuilderSteps.ChannelStep<ListFiles> { }
}
