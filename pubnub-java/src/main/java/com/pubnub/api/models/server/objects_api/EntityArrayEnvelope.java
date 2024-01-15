package com.pubnub.api.models.server.objects_api;

import com.pubnub.api.models.consumer.PNPage;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class EntityArrayEnvelope<T> extends EntityEnvelope<List<T>> {

    protected Integer totalCount;
    protected String next;
    protected String prev;

    public PNPage nextPage() {
        return PNPage.next(next);
    }

    public PNPage previousPage() {
        return PNPage.previous(prev);
    }
}
