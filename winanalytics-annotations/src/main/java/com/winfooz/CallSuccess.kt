package com.winfooz

/**
 * Project: WinAnalytics2
 * Created: November 23, 2018
 *
 * @author Mohamed Hamdan
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION)
@MustBeDocumented
annotation class CallSuccess(val value: String, val name: String)
