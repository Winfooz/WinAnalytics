package com.winanalytics.kotlin.sample

import android.content.Context
import android.util.Pair
import com.mixpanel.android.mpmetrics.MixpanelAPI
import com.winfooz.AnalyticsAdapter
import com.winfooz.MapUtils

/**
 * Project: MyApplication6 Created: November 15, 2018
 *
 * @author Mohamed Hamdan
 */
class KotlinMixpanelAdapter internal constructor(private val context: Context, private val token: String) : AnalyticsAdapter {

    override fun log(pair: Pair<String, HashMap<String, Any>>) {
        val api = MixpanelAPI.getInstance(context, token)
        api.track(pair.first, MapUtils.toJsonObject(pair.second))
    }
}
