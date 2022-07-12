package com.leony.app.ricknmorty.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.leony.app.ricknmorty.R


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed(Runnable {
            // Start Main Activity
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            // Close this activity
            finish()
        }, 3000)
    }
}