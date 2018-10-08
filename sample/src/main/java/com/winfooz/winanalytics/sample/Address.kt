package com.winfooz.winanalytics.sample

import com.winfooz.winanalytics.annotations.Analytics
import com.winfooz.winanalytics.annotations.Event

data class Address(
        @Analytics(Event("Login"))
        val address: String,

        @Analytics(Event("Login"))
        val latitude: String,

        @Analytics(Event("Login"))
        val longitude: String
)
