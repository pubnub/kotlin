package com.pubnub.api.models.consumer.access_manager.v3;

public class Space extends PNResource<Space> {

    private Space() {

    }

    public static Space id(String spaceId) {
        Space space = new Space();
        space.resourceName = spaceId;
        return space;
    }

    public static Space pattern(String spacePattern) {
        Space space = new Space();
        space.resourcePattern = spacePattern;
        return space;
    }

    @Override
    public Space read() {
        return super.read();
    }

    @Override
    public Space delete() {
        return super.delete();
    }

    @Override
    public Space write() {
        return super.write();
    }

    @Override
    public Space manage() {
        return super.manage();
    }

    @Override
    public Space create() {
        return super.create();
    }
}