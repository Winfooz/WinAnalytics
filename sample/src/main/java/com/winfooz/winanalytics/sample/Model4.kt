package com.winfooz.winanalytics.sample

import com.winfooz.winanalytics.annotations.Analytics
import com.winfooz.winanalytics.annotations.WinEvent

data class Model4(
        @Analytics(WinEvent("Logout"))
        val address: String,

        @Analytics(WinEvent("Logout"))
        val latitude: String,

        @Analytics(WinEvent("Logout"))
        val longitude: String
)
