package com.winanalytics.kotlin.sample

import android.app.Application

import com.winfooz.WinAnalytics
import com.winfooz.WinConfiguration

/**
 * Project: WinAnalytics2 Created: November 16, 2018
 *
 * @author Mohamed Hamdan
 */
class KotlinMyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val configuration = WinConfiguration.builder()
            .registerAdapter(
                KotlinMixpanelAdapter(this, "6ce26d515c8a1b3756be42feaeda4cb3"))
            .build()
        WinAnalytics.init(configuration)
    }
}
