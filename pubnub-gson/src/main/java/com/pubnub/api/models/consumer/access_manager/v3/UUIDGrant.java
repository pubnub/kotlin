package com.pubnub.api.models.consumer.access_manager.v3;

public class UUIDGrant extends PNResource<UUIDGrant> {

    private UUIDGrant() {
    }

    public static UUIDGrant id(String groupName) {
        UUIDGrant uuidGrant = new UUIDGrant();
        uuidGrant.resourceName = groupName;
        return uuidGrant;
    }

    public static UUIDGrant pattern(String groupPattern) {
        UUIDGrant uuidGrant = new UUIDGrant();
        uuidGrant.resourcePattern = groupPattern;
        return uuidGrant;
    }

    @Override
    public UUIDGrant get() {
        return super.get();
    }

    @Override
    public UUIDGrant update() {
        return super.update();
    }

    @Override
    public UUIDGrant delete() {
        return super.delete();
    }
}
