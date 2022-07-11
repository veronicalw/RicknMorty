package com.leony.app.ricknmorty.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.leony.app.ricknmorty.R


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            val installSplashScreen = installSplashScreen()
            installSplashScreen.setKeepOnScreenCondition{true}
        }
        setContentView(R.layout.activity_splash)
        executeActionToIntroductionActivity()
    }

    private fun executeActionToIntroductionActivity(){
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, IntroductionActivity::class.java)
            startActivity(intent)
            finish()
        }, 1000)
    }
}