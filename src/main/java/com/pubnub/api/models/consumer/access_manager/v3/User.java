package com.pubnub.api.models.consumer.access_manager.v3;

public class User extends PNResource<User> {

    private User() {

    }

    public static User id(String userId) {
        User user = new User();
        user.resourceName = userId;
        return user;
    }

    public static User pattern(String userPattern) {
        User user = new User();
        user.resourcePattern = userPattern;
        return user;
    }

    @Override
    public User read() {
        return super.read();
    }

    @Override
    public User delete() {
        return super.delete();
    }

    @Override
    public User write() {
        return super.write();
    }

    @Override
    public User manage() {
        return super.manage();
    }

    @Override
    public User create() {
        return super.create();
    }
}
