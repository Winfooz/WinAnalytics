package com.winfooz

/**
 * Project: MyApplication6 Created: November 15, 2018
 *
 * @author Mohamed Hamdan
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION)
@MustBeDocumented
annotation class Event(
    val value: String,
    val timestamp: Boolean = false,
    val events: Array<Data> = []
)
