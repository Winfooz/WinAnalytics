package com.winanalytics.java.sample

import android.content.Context
import android.util.Log
import android.util.Pair
import android.widget.Toast
import com.winfooz.AnalyticsAdapter
import com.winfooz.toJsonObject
import java.util.HashMap

/**
 * Project: WinAnalytics2
 * Created: February 08, 2019
 *
 * @author Mohamed Hamdan
 */
class MyCustomAnalyticsAdapter(
    private val context: Context
) : AnalyticsAdapter {

    override fun log(pair: Pair<String, HashMap<String, Any?>>) {
        val event = pair.first
        val data = pair.second.toJsonObject().toString()
        Log.d("TEST_$event".replace(" ", "_").toUpperCase(), data)
        Toast.makeText(context, "$event: $data", Toast.LENGTH_LONG).show()
    }
}