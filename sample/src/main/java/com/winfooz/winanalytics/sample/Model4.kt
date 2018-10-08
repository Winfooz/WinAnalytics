package com.winfooz.winanalytics.sample

import com.winfooz.winanalytics.annotations.Analytics
import com.winfooz.winanalytics.annotations.Event

data class Model4(
        @Analytics(Event("Logout"))
        val address: String,

        @Analytics(Event("Logout"))
        val latitude: String,

        @Analytics(Event("Logout"))
        val longitude: String
)
