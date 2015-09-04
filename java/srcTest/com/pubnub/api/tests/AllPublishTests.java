package com.pubnub.api.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AllPublishPamTests.class, AllPublishNonPamTests.class })
public class AllPublishTests {

}
