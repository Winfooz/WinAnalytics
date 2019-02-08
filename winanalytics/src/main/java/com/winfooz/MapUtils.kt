package com.winfooz

import android.os.Bundle
import org.json.JSONObject
import java.util.HashMap

/**
 * Project: WinAnalytics2 Created: November 16, 2018
 *
 * @author Mohamed Hamdan
 */
fun <V> HashMap<String, V?>.toJsonObject(): JSONObject {
    val json = JSONObject()
    entries.forEach {
        json.put(it.key, it.value)
    }
    return json
}

fun <V> HashMap<String, V?>.toBundle(): Bundle {
    val bundle = Bundle()
    entries.forEach {
        bundle.putString(it.key, it.value.toString())
    }
    return bundle
}