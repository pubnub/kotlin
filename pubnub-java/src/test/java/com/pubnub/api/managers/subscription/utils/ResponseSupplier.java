package com.pubnub.api.managers.subscription.utils;

@FunctionalInterface
public interface ResponseSupplier<T> {
    ResponseHolder<T> get(RequestDetails requestDetails);
}
