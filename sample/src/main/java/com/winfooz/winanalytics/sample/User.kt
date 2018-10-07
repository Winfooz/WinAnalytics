package com.winfooz.winanalytics.sample

import com.winfooz.winanalytics.annotations.WinEvent
import com.winfooz.winanalytics.annotations.AnalyticsEmbedded
import com.winfooz.winanalytics.annotations.Analytics

data class User(
        @Analytics(
                WinEvent("Login"),
                WinEvent("Logout")
        )
        val name: String,

        @Analytics(WinEvent("Login"), WinEvent("Logout"))
        val email: String,

        @Analytics(WinEvent("Login"))
        val password: String,

        @Analytics(WinEvent("Login"))
        val phone: String,

        @Analytics(WinEvent("Login"))
        val age: Int,

        @AnalyticsEmbedded
        val address: Address?
)