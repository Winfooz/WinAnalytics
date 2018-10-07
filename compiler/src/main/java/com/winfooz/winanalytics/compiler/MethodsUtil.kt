package com.winfooz.winanalytics.compiler

object MethodsUtil {

    /**
     * For get method name + Event suffix based on [methodName]
     */
    fun getAnalyticsMethod(methodName: String): String {
        return methodName[0].toLowerCase() + methodName.substring(1) + "Event"
    }
}