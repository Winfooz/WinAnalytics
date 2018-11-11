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
data class Model1(
        @Analytics("test1", events = [Event("Logout")])
        val test1: String?,

        @Analytics("test2", events = [Event("Logout")])
        val test2: String?,

        @Analytics("test3", events = [Event("Logout")])
        val test3: String?,

        @AnalyticsEmbedded(override = "test1:Name, test2:Email, test3:Id")
        val model2: Model2?
)
