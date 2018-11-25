package com.winfooz;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

/**
 * Project: WinAnalytics2 Created: November 16, 2018
 *
 * @author Mohamed Hamdan
 */
public class MapUtils {

    public static JSONObject toJsonObject(HashMap<String, Object> map) {
        JSONObject object = new JSONObject();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            try {
                object.put(entry.getKey(), entry.getValue());
            } catch (Exception ignored) {
            }
        }
        return object;
    }
}
