package com.winanalytics.java.sample.viewclicks

import com.winanalytics.java.sample.NETWORK_CLICK_EVENT
import com.winfooz.Analytics
import com.winfooz.Data
import com.winfooz.Event
import com.winfooz.Key
import com.winfooz.Name
import com.winfooz.Value

/**
 * Project: WinAnalytics2
 * Created: February 08, 2019
 *
 * @author Mohamed Hamdan
 */
@Analytics
interface ViewClicksAnalytics {

    @Event(
        value = NETWORK_CLICK_EVENT,
        events = [
            Data(value = Value("value"), key = Key("value"))
        ],
        timestamp = true
    )
    fun failedGetPosts(@Name("value") value: String)
}