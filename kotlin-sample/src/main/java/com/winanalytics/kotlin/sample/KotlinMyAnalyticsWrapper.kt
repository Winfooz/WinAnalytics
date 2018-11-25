package com.winanalytics.kotlin.sample

import com.winanalytics.kotlin.sample.test.KotlinMainActivityAnalytics
import com.winfooz.AnalyticsWrapper

/**
 * Project: WinAnalytics2
 * Created: November 22, 2018
 *
 * @author Mohamed Hamdan
 */
@AnalyticsWrapper
interface KotlinMyAnalyticsWrapper {

    fun mainActivityAnalytics(): KotlinMainActivityAnalytics
}
