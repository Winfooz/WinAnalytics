package com.winanalytics.java.sample;

import com.winanalytics.java.sample.test.JavaMainActivityAnalytics;
import com.winfooz.AnalyticsWrapper;

/**
 * Project: WinAnalytics2
 * Created: November 22, 2018
 *
 * @author Mohamed Hamdan
 */
@AnalyticsWrapper
public interface JavaMyAnalyticsWrapper {

    JavaMainActivityAnalytics mainActivityAnalytics();
}
