package com.pubnub.api.models.server.objects_api;

import lombok.Getter;

import java.util.List;

@Getter
public class EntityArrayEnvelope<T> extends EntityEnvelope<List<T>> {

    protected Integer totalCount;
    protected String next;
    protected String prev;
}
