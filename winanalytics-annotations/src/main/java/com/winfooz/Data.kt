package com.winfooz

/**
 * Project: MyApplication6 Created: November 15, 2018
 *
 * @author Mohamed Hamdan
 */
@Retention(AnnotationRetention.SOURCE)
@Target
@MustBeDocumented
annotation class Data(
    val value: Value,
    val key: Key
)
