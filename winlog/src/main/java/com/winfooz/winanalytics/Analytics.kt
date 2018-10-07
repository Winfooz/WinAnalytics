package com.winfooz.winanalytics

interface Analytics {

    fun log(data: Pair<String, MutableMap<String, String>>)
}
