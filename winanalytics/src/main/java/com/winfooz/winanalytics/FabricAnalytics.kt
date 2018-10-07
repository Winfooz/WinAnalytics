package com.winfooz.winanalytics

import android.content.Context
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.CustomEvent

class FabricAnalytics(private val context: Context) : Analytics {

    override fun log(data: Pair<String, MutableMap<String, String>>) {
        val event = CustomEvent(data.first)
        data.second.keys.forEach {
            event.putCustomAttribute(it, data.second[it])
        }
        Answers.getInstance().logCustom(event)
    }
}
