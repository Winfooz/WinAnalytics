package com.winfooz

/**
 * Project: MyApplication6 Created: November 15, 2018
 *
 * @author Mohamed Hamdan
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
@MustBeDocumented
annotation class CallArgument(
    val value: Array<String>,
    val names: Array<String>
)
