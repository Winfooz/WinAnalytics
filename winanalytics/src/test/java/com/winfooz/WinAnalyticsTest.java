package com.winfooz;

import com.winfooz.examples.ExampleActivityWithoutAnalyticsClass;
import com.winfooz.examples.analytics.ExampleActivityWithAnalyticsClass;
import com.winfooz.examples.analytics.ExampleActivityWithAnalyticsClass_Analytics;
import com.winfooz.examples.wrapper.ExampleAnalyticsWrapper;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

/**
 * Project: WinAnalytics2
 * Created: November 18, 2018
 *
 * @author Mohamed Hamdan
 */
@RunWith(RobolectricTestRunner.class)
public class WinAnalyticsTest {

    @Test
    public void test_notFoundClass() {
        Assert.assertEquals(WinAnalytics.Companion.bind(new ExampleActivityWithoutAnalyticsClass()), Destroyable.EMPTY_DESTROYABLE);
    }

    @Test
    public void test_findAnalyticsClass() {
        Assert.assertNotEquals(WinAnalytics.Companion.bind(new ExampleActivityWithAnalyticsClass()), Destroyable.EMPTY_DESTROYABLE);
        assert WinAnalytics.Companion.bind(new ExampleActivityWithAnalyticsClass()) instanceof ExampleActivityWithAnalyticsClass_Analytics;
    }

    @Test
    public void test_initAnalytics() {
        WinAnalytics.Companion.init(WinConfiguration.builder().build());
        Assert.assertNotNull(WinAnalytics.getConfiguration());
    }

    @Test
    public void test_createExistWrapper() {
        Assert.assertNotNull(WinAnalytics.Companion.create(ExampleAnalyticsWrapper.class));
    }
}
