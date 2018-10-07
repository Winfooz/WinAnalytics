package com.winfooz.winanalytics.annotations

/**
 * This annotation used with [AnalyticsConfiguration] for enable or disable clients
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class WinAnalyticsClient(
        val key: String = "",
        val type: AnalyticsTypes,
        val enabled: Boolean = true
)
