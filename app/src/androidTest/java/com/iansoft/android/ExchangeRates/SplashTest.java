package com.iansoft.android.ExchangeRates;

import android.test.ActivityInstrumentationTestCase2;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class com.iansoft.android.ExchangeRates.SplashTest \
 * com.iansoft.android.ExchangeRates.tests/android.test.InstrumentationTestRunner
 */
public class SplashTest extends ActivityInstrumentationTestCase2<Splash> {

    public SplashTest() {
        super("com.iansoft.android.ExchangeRates", Splash.class);
    }

}
