package com.farisafra.dicodingstory.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.farisafra.dicodingstory.R
import com.farisafra.dicodingstory.data.preferences.LoginPreference
import com.farisafra.dicodingstory.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var loginPreference: LoginPreference // Tambahkan ini

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginPreference = LoginPreference(this)

        val userData = loginPreference.getLogin()

        binding.tvUsername.text = userData.userId

        binding.btnLogout.setOnClickListener {
            // Panggil clearLogin() ketika tombol logout ditekan
            loginPreference.clearLogin()
            // Setelah logout, arahkan pengguna ke halaman login atau onboarding
            moveToLoginOrOnBoarding()
        }
    }

    private fun moveToLoginOrOnBoarding() {
        val intent = Intent(this, OnBoardingActivity::class.java)
        startActivity(intent)
        finish() // Selesai dengan MainActivity setelah logout
    }
}