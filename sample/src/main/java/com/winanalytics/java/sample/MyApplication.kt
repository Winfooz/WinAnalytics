package com.winanalytics.java.sample

import android.app.Application
import com.winfooz.MixPanelAdapter
import com.winfooz.WinAnalytics
import com.winfooz.WinConfiguration

/**
 * Project: WinAnalytics2
 * Created: February 08, 2019
 *
 * @author Mohamed Hamdan
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val config = WinConfiguration.builder()
            .debugMode(true)
            .indexingClass(WinAnalyticsIndex::class.java)
            .registerAdapter(MixPanelAdapter(this, "token"))
            .registerAdapter(MyCustomAnalyticsAdapter(this))
            .build()
        WinAnalytics.init(config)
    }
}