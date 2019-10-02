package com.pubnub.api.models.consumer.access_manager.v3;

public class Group extends PNResource<Group> {

    private Group() {

    }

    public static Group id(String groupName) {
        Group group = new Group();
        group.resourceName = groupName;
        return group;
    }

    public static Group pattern(String groupPattern) {
        Group group = new Group();
        group.resourcePattern = groupPattern;
        return group;
    }

    @Override
    public Group read() {
        return super.read();
    }

    @Override
    public Group manage() {
        return super.manage();
    }
}
