package com.winfooz

/**
 * Project: WinAnalytics2
 * Created: November 17, 2018
 *
 * @author Mohamed Hamdan
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@MustBeDocumented
annotation class Name(
    val value: String
)
