package com.winfooz

/**
 * Project: MyApplication6 Created: November 15, 2018
 *
 * @author Mohamed Hamdan
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FIELD)
@MustBeDocumented
annotation class Bind(
    vararg val value: Int
)
