package com.pubnub;

import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.user.models.consumer.UsersResult;
import org.junit.Test;

import static com.pubnub.user.UserExtensionKt.fetchUsers;

public class TestTestTest {

    @Test
    public void test() throws PubNubException {
        PubNub pubnub = new PubNub(new PNConfiguration("aaa")
                .setSubscribeKey(TestConfiguration.instance.getSubscribeKey())
                .setPublishKey(TestConfiguration.instance.getPublishKey()));

        UsersResult result = fetchUsers(pubnub).sync();
        System.out.println(result);
    }
}
