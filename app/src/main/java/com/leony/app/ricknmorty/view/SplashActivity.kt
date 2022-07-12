package com.leony.app.ricknmorty.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.leony.app.ricknmorty.R


class SplashActivity : AppCompatActivity() {

    /**
     * To support splash screen official configuration by Google
     * I implemented a splash screen that can be accessed by android above 12 and below where for android 12
     * and above it has followed the standards set by Android.
     * As for Android 12 and below, we can use the desired custom splash screen.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            val installSplashScreen = installSplashScreen()
            installSplashScreen.setKeepOnScreenCondition{true}
        }
        setContentView(R.layout.activity_splash)
        executeActionToIntroductionActivity()
    }

    /**
     * The basic and traditional way of creating a splash screen. At first this class would bring
     * the user to the IntroductionActivity.
     */
    private fun executeActionToIntroductionActivity(){
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, IntroductionActivity::class.java)
            startActivity(intent)
            finish()
        }, 1000)
    }
}