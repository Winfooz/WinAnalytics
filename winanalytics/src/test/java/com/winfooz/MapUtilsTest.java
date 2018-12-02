package com.winfooz;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.HashMap;

/**
 * Project: WinAnalytics2 Created: November 18, 2018
 *
 * @author Mohamed Hamdan
 */
@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner.class)
public class MapUtilsTest {

    @Test
    public void test_toJsonObject() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("test", "test");
        JSONObject object = MapUtils.toJsonObject(hashMap);
        Assert.assertTrue(object.has("test"));
    }
}
