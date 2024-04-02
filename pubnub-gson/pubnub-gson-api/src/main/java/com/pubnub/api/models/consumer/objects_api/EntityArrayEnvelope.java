package com.pubnub.api.models.consumer.objects_api;

import com.pubnub.api.models.consumer.objects.PNPage;
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
        return new PNPage.PNNext(next);
    }

    public PNPage previousPage() {
        return new PNPage.PNPrev(prev);
    }
}
