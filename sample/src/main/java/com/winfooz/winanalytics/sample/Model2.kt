package com.winfooz.winanalytics.sample

import com.winfooz.winanalytics.annotations.Analytics
import com.winfooz.winanalytics.annotations.AnalyticsEmbedded
import com.winfooz.winanalytics.annotations.Event

/**
 * Project: WinAnalytics
 * Created: November 08, 2018
 *
 * @author Mohamed Hamdan
 */
data class Model2(
        @Analytics("test1", events = [Event("Logout_pressed")])
        val test: String?,

        @Analytics("test2", events = [Event("Logout pressed")])
        val test1: String?,

        @Analytics("test3", events = [Event("Login pressed")])
        val test2: String?,

        @Analytics("test13", events = [Event("Logout pressed")])
        val test13: String?,

        @AnalyticsEmbedded(override = "test4:Name, test5:Email, test6:Id")
        val test3: Model3?
)
