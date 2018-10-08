package com.winfooz.winanalytics.sample

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.winfooz.winanalytics.annotations.AnalyticsTypes
import com.winfooz.winanalytics.annotations.AnalyticsClient
import com.winfooz.winanalytics.annotations.AnalyticsConfiguration
import io.fabric.sdk.android.Fabric

@AnalyticsConfiguration(
        AnalyticsClient(type = AnalyticsTypes.FIREBASE),
        AnalyticsClient(type = AnalyticsTypes.FABRIC),
        AnalyticsClient(key = "mixpanelToken", type = AnalyticsTypes.MIXPANEL)
)
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics())
    }
}