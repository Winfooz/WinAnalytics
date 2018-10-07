package com.winfooz.winanalytics.sample

import com.winfooz.winanalytics.annotations.Analytics
import com.winfooz.winanalytics.annotations.WinEvent

data class Address(
        @Analytics(WinEvent("Login"))
        val address: String,

        @Analytics(WinEvent("Login"))
        val latitude: String,

        @Analytics(WinEvent("Login"))
        val longitude: String
)
