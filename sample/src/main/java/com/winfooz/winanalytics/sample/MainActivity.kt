package com.winfooz.winanalytics.sample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.winfooz.winanalytics.annotations.AnalyticsEmbedded

class MainActivity : AppCompatActivity() {

    var user: User? = null

    @AnalyticsEmbedded
    var userEmbedded: User? = null

    @AnalyticsEmbedded
    var addressEmbedded: Address? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val address = Address("Address", "11.25454654", "10,45454654664")
        addressEmbedded = Address("Address", "11.25454654", "10,45454654664")

        user = User("Name", "email@email.com", "123456789", 0, address)
        userEmbedded = User("Name", "email@email.com", "123456789", 0, address)

        findViewById<View>(R.id.hello_world).setOnClickListener(this::onHelloWorldClicked)
    }

    private fun onHelloWorldClicked(view: View) {
        Example.getInstance(applicationContext).mainActivityAnalytics.loginEvent(this)
    }
}
