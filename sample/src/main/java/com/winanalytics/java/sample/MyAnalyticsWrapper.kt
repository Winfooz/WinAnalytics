package com.winanalytics.java.sample

import com.winanalytics.java.sample.network.NetworkAnalytics
import com.winanalytics.java.sample.viewclicks.ViewClicksAnalytics
import com.winfooz.AnalyticsWrapper

/**
 * Project: WinAnalytics2
 * Created: February 08, 2019
 *
 * @author Mohamed Hamdan
 */
@AnalyticsWrapper
interface MyAnalyticsWrapper {

    fun viewClicksAnalytics(): ViewClicksAnalytics

    fun networkAnalytics(): NetworkAnalytics
}
