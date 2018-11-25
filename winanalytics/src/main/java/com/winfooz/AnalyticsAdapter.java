package com.winfooz;

import android.util.Pair;
import java.util.HashMap;

/**
 * Project: MyApplication6 Created: November 15, 2018
 *
 * @author Mohamed Hamdan
 */
public interface AnalyticsAdapter {

    void log(Pair<String, HashMap<String, Object>> pair);
}
