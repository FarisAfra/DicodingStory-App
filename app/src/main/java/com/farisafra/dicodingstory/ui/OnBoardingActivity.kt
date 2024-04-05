package com.farisafra.dicodingstory.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.motion.widget.MotionLayout
import com.farisafra.dicodingstory.R
import com.farisafra.dicodingstory.databinding.ActivityOnboardingBinding

class OnBoardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.BtnLogin.setOnClickListener{ moveToLogin() }
        binding.BtnRegister.setOnClickListener { moveToRegister() }

        val motionLayout = findViewById<MotionLayout>(R.id.motionOnboarding)
        val menuToNavigate = intent.getIntExtra("navigateToMenu", -1)
        if (menuToNavigate == 4) {
            motionLayout.post {
                motionLayout.transitionToState(R.id.menu4)
            }
        }
    }



    private fun moveToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun moveToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}