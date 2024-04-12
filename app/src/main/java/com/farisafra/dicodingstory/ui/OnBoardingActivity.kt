package com.farisafra.dicodingstory.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat
import com.farisafra.dicodingstory.databinding.ActivityOnboardingBinding

class OnBoardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.BtnLogin.setOnClickListener{
            moveToLogin()
        }
        binding.BtnRegister.setOnClickListener { moveToRegister() }


    }

    private fun moveToLogin() {
        val intent = Intent(this, LoginActivity::class.java)

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this, binding.imageView, "logo" // Use 'binding.ivLogo' here
        )
        startActivity(intent, options.toBundle())
    }

    private fun moveToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this, binding.imageView, "logo" // Use 'binding.ivLogo' here
        )
        startActivity(intent, options.toBundle())
    }


}