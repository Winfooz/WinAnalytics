package com.winfooz.winanalytics.sample

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.winfooz.winanalytics.annotations.AnalyticsTypes
import com.winfooz.winanalytics.annotations.WinAnalyticsClient
import com.winfooz.winanalytics.annotations.AnalyticsConfiguration
import io.fabric.sdk.android.Fabric

@AnalyticsConfiguration(
        WinAnalyticsClient(type = AnalyticsTypes.FIREBASE),
        WinAnalyticsClient(type = AnalyticsTypes.FABRIC),
        WinAnalyticsClient(key = "mixpanelToken", type = AnalyticsTypes.MIXPANEL)
)
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics())
    }
}