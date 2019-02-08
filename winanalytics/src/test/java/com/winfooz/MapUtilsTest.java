package com.winfooz;

import java.util.HashMap;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

/**
 * Project: WinAnalytics2 Created: November 18, 2018
 *
 * @author Mohamed Hamdan
 */
@RunWith(RobolectricTestRunner.class)
public class MapUtilsTest {

    @Test
    public void testToJsonObject() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("test", "test");

        Assert.assertTrue(MapUtils.INSTANCE.toJsonObject(hashMap).has("test"));
    }
}
