package com.winfooz

import android.content.Context
import android.util.Pair
import com.mixpanel.android.mpmetrics.MixpanelAPI
import java.util.HashMap

/**
 * Project: WinAnalytics2
 * Created: February 08, 2019
 *
 * @author Mohamed Hamdan
 */
class MixPanelAdapter(
    private val context: Context,
    private val token: String
) : AnalyticsAdapter {

    private val api: MixpanelAPI by lazy { MixpanelAPI.getInstance(context, token) }

    override fun log(pair: Pair<String, HashMap<String, Any?>>) {
        api.track(pair.first, pair.second.toJsonObject())
    }
}
