package com.winanalytics.java.sample.viewclicks

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.winanalytics.java.sample.MyAnalyticsWrapper
import com.winanalytics.java.sample.NETWORK_CLICK_EVENT
import com.winanalytics.java.sample.R
import com.winanalytics.java.sample.network.NetworkActivity
import com.winfooz.Bind
import com.winfooz.EventOnClick
import com.winfooz.Name
import com.winfooz.WinAnalytics

/**
 * Project: WinAnalytics2
 * Created: February 08, 2019
 *
 * @author Mohamed Hamdan
 */
class ViewClicksActivity : AppCompatActivity() {

    @Name("value")
    @Bind(R.id.btn_network)
    lateinit var value: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_clicks)
        WinAnalytics.bind(this)

        value = "Value"

        findViewById<View>(R.id.btn_manual).setOnClickListener {
            WinAnalytics.create(MyAnalyticsWrapper::class.java)
                .viewClicksAnalytics()
                .failedGetPosts(value)
        }
    }

    @EventOnClick(R.id.btn_network, event = NETWORK_CLICK_EVENT)
    fun onNetworkClicked() {
        startActivity(Intent(this, NetworkActivity::class.java))
    }
}