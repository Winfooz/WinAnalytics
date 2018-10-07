package com.winfooz.winanalytics

import android.content.Context
import com.mixpanel.android.mpmetrics.MixpanelAPI
import org.json.JSONObject
import java.lang.Exception

class MixPanelAnalytics(private val context: Context, private val token: String) : Analytics {

    override fun log(data: Pair<String, MutableMap<String, String>>) {
        try {
            val jsonObject = JSONObject()
            data.second.keys.forEach {
                jsonObject.put(it, data.second[it])
            }
            val mixpanel = MixpanelAPI.getInstance(context, token)
            mixpanel.track(data.first, jsonObject)
        } catch (ignored: Exception) {
        }
    }
}
