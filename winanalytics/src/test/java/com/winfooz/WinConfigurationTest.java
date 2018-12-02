package com.winfooz;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * Project: WinAnalytics2 Created: November 18, 2018
 *
 * @author Mohamed Hamdan
 */
@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class WinConfigurationTest {

    @Test
    public void test_configurationBuilderMethod() {
        Assert.assertNotNull(WinConfiguration.builder());
    }

    @Test
    public void test_registerAdapter() {
        Assert.assertTrue(
                WinConfiguration.builder().registerAdapter(pair -> {}).build().getAdapters().size()
                        > 0);
    }
}
