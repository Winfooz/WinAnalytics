package com.winfooz.winanalytics.sample

import com.winfooz.winanalytics.annotations.Analytics
import com.winfooz.winanalytics.annotations.Event

data class Model3(
        @Analytics(Event("HelloWorld"))
        val name: String,

        @Analytics(Event("Logout"))
        val email: String,

        @Analytics(Event("Logout"))
        val phone: String,

        @Analytics(Event("HelloWorld"))
        val age: Int
)