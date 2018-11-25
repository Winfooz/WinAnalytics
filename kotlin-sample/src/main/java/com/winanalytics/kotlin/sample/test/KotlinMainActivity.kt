package com.winanalytics.kotlin.sample.test

import android.os.Bundle
import android.support.annotation.Keep
import android.support.v7.app.AppCompatActivity

import com.winanalytics.kotlin.sample.R
import com.winanalytics.kotlin.sample.R2
import com.winanalytics.kotlin.sample.models.User
import com.winfooz.Bind
import com.winfooz.EventOnClick
import com.winfooz.Name
import com.winfooz.Screen
import com.winfooz.WinAnalytics

@Keep
@Screen("KotlinMainActivity")
class KotlinMainActivity : AppCompatActivity() {

    @Name("user")
    @Bind(R2.id.btn_login, R2.id.btn_login1)
    lateinit var user: User

    @Name("str")
    @Bind(R2.id.btn_login)
    lateinit var str: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        WinAnalytics.bind(this)
    }

    @EventOnClick(value = R2.id.btn_login, event = "User login")
    fun onLoginClicked() {
    }

    @EventOnClick(value = R2.id.btn_login1, event = "User login")
    fun onLogin1Clicked() {
    }
}
