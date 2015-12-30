package com.pubnub.api.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ PublishPamEncSslTest.class, PublishPamEncTest.class, PublishPamSslTest.class, PublishPamTest.class,
        PublishTest.class })
public class AllPublishPamTests {

}
