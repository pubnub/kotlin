package com.pubnub.api.integration.objects;

import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.UserId;
import com.pubnub.api.enums.PNLogVerbosity;
import com.pubnub.api.integration.util.ITTestConfig;
import com.pubnub.api.java.PubNubForJava;
import com.pubnub.api.java.v2.PNConfiguration;
import org.aeonbits.owner.ConfigFactory;
import org.junit.Before;

import java.util.UUID;

import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assume.assumeThat;

public abstract class ObjectsApiBaseIT {
    //See README.md in integrationTest directory for more info on running integration tests
    private ITTestConfig itTestConfig = ConfigFactory.create(ITTestConfig.class, System.getenv());

    protected final PubNubForJava pubNubUnderTest = pubNub();

    private PubNubForJava pubNub() {
        PNConfiguration.Builder pnConfiguration;
        try {
            pnConfiguration = PNConfiguration.builder(new UserId("pn-" + UUID.randomUUID()), "");
        } catch (PubNubException e) {
            throw new RuntimeException(e);
        }
        pnConfiguration.subscribeKey(itTestConfig.subscribeKey());
        pnConfiguration.logVerbosity(PNLogVerbosity.BODY);

        return PubNubForJava.create(pnConfiguration.build());
    }

    @Before
    public void assumeTestsAreConfiguredProperly() {
        assumeThat("Subscription key must be set in test.properties", itTestConfig.subscribeKey(), not(isEmptyOrNullString()));
    }
}
