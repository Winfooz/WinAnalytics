package com.winfooz.winanalytics.annotations

/**
 * This annotation for register event to field.
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class WinEvent(val value: String)

