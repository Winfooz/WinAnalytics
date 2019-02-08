package com.winfooz

import android.content.Context
import android.util.Pair
import com.google.firebase.analytics.FirebaseAnalytics
import java.util.HashMap

/**
 * Project: WinAnalytics2
 * Created: February 08, 2019
 *
 * @author Mohamed Hamdan
 */
class FirebaseAdapter(private val context: Context) : AnalyticsAdapter {

    private val api: FirebaseAnalytics by lazy { FirebaseAnalytics.getInstance(context) }

    override fun log(pair: Pair<String, HashMap<String, Any?>>) {
        api.logEvent(pair.first, pair.second.toBundle())
    }
}
