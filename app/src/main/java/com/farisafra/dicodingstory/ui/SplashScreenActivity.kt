package com.farisafra.dicodingstory.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.transition.TransitionManager
import androidx.constraintlayout.motion.widget.MotionLayout
import com.farisafra.dicodingstory.R


class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val motionLayout = findViewById<MotionLayout>(R.id.splashMotionLayout)
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)

        Handler(Looper.getMainLooper()).postDelayed({
            TransitionManager.beginDelayedTransition(motionLayout)

            motionLayout.transitionToEnd()

            Handler(Looper.getMainLooper()).postDelayed({
                if (token != null) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    startActivity(Intent(this, OnBoardingActivity::class.java))
                    finish()
                }
            }, SPLASH_DELAY_LONG)
        }, SPLASH_DELAY_SHORT)
    }

    companion object {
        private const val SPLASH_DELAY_SHORT = 1000L
        private const val SPLASH_DELAY_LONG = 3000L
    }
}