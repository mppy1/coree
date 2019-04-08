package com.jew.coree

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv_hello.setText(R.string.app_name)
        SplashActivity().canJump
        var intent = Intent()
        intent.setClass(this, SplashActivity::class.java)
        startActivity(intent)
    }


}
