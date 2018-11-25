package com.winanalytics.java.sample;

import android.app.Application;

import com.winfooz.WinAnalytics;
import com.winfooz.WinConfiguration;

/**
 * Project: WinAnalytics2 Created: November 16, 2018
 *
 * @author Mohamed Hamdan
 */
public class JavaMyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        WinConfiguration configuration = WinConfiguration.builder()
                .registerAdapter(new JavaMixpanelAdapter(this, "6ce26d515c8a1b3756be42feaeda4cb3"))
                .indexingClass(MyAnalyticsIndex.class)
                .build();
        WinAnalytics.init(configuration);
    }
}
