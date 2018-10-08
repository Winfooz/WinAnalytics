package com.winfooz.winanalytics.sample

import com.winfooz.winanalytics.annotations.Event
import com.winfooz.winanalytics.annotations.AnalyticsEmbedded
import com.winfooz.winanalytics.annotations.Analytics

data class User(
        @Analytics(
                Event("Login"),
                Event("Logout")
        )
        val name: String,

        @Analytics(Event("Login"), Event("Logout"))
        val email: String,

        @Analytics(Event("Login"))
        val phone: String,

        @Analytics(Event("Login"))
        val age: Int,

        @AnalyticsEmbedded
        val address: Address?
)