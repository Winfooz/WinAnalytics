package com.winfooz.winanalytics.annotations

/**
 * That annotation for enable or disable clients
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class AnalyticsConfiguration(vararg val value: WinAnalyticsClient)
