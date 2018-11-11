package com.winfooz.winanalytics.sample

import com.winfooz.winanalytics.annotations.Analytics
import com.winfooz.winanalytics.annotations.Event

data class Model4(
        @Analytics("test7", events = [Event("Logout")])
        val test8: String?,

        @Analytics("test8", events = [Event("Logout")])
        val test9: String?,

        @Analytics("test9", events = [Event("Logout")])
        val test10: String?
)
