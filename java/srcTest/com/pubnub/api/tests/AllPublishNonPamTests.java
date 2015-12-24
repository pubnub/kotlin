package com.pubnub.api.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ PublishEncTest.class, PublishSslEncTest.class, PublishSslTest.class, PublishTest.class })
public class AllPublishNonPamTests {

}
