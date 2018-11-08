package com.winfooz.winanalytics.annotations

/**
 * That annotation for enable or disable clients
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class AnalyticsConfiguration(val className: String, vararg val value: AnalyticsClient)
