package com.winfooz.winanalytics.annotations

/**
 * That annotation for register fields with multiple events
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Analytics(val value: String, vararg val events: Event)
