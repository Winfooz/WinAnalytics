package com.winfooz.winanalytics.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.hello_world).setOnClickListener(this::onHelloWorldClicked)
    }

    private fun onHelloWorldClicked(view: View) {
    }
}
