package com.winfooz

import android.util.Pair
import java.util.HashMap

/**
 * Project: MyApplication6 Created: November 15, 2018
 *
 * @author Mohamed Hamdan
 */
interface AnalyticsAdapter {

    fun log(pair: Pair<String, HashMap<String, Any?>>)
}
