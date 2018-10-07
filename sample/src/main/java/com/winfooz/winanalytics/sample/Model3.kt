package com.winfooz.winanalytics.sample

import com.winfooz.winanalytics.annotations.Analytics
import com.winfooz.winanalytics.annotations.WinEvent

data class Model3(
        @Analytics(WinEvent("HelloWorld"))
        val name: String,

        @Analytics(WinEvent("Logout"))
        val email: String,

        @Analytics(WinEvent("HelloWorld"))
        val password: String,

        @Analytics(WinEvent("Logout"))
        val phone: String,

        @Analytics(WinEvent("HelloWorld"))
        val age: Int
)