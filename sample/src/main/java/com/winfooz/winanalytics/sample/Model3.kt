package com.winfooz.winanalytics.sample

import com.winfooz.winanalytics.annotations.Analytics
import com.winfooz.winanalytics.annotations.AnalyticsEmbedded
import com.winfooz.winanalytics.annotations.Event

data class Model3(
        @Analytics("test4", events = [Event("Logout")])
        val test4: String?,

        @Analytics("test5", events = [Event("Logout")])
        val test5: String?,

        @Analytics("test6", events = [Event("Logout")])
        val test6: String?,

        @AnalyticsEmbedded(override = "test7:Name, test8:Email, test9:Id")
        val test7: Model4?
)