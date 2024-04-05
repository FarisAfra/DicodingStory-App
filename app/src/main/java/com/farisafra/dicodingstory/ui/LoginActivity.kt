package com.farisafra.dicodingstory.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.farisafra.dicodingstory.R
import com.farisafra.dicodingstory.data.preferences.LoginPreference
import com.farisafra.dicodingstory.data.response.login.LoginResponse
import com.farisafra.dicodingstory.data.retrofit.ApiClient
import com.farisafra.dicodingstory.data.retrofit.ApiService
import com.farisafra.dicodingstory.databinding.ActivityLoginBinding
import com.farisafra.dicodingstory.databinding.ActivityOnboardingBinding
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressBar = binding.progressBar

        binding.BtnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString().trim()
            val password = binding.edLoginPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                showToast("Please fill in all fields")
            } else {
                login(email, password)
            }
        }
        binding.btnBack.setOnClickListener { moveToBackToMenu4() }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun login(email: String, password: String) {
        // Tampilkan ProgressBar
        progressBar.visibility = View.VISIBLE

        val apiService = ApiClient.getApiService()

        lifecycleScope.launch {
            try {
                val response = apiService.login(email, password)
                if (!response.error) {
                    showToast("Login successful")
                    val loginPreference = LoginPreference(this@LoginActivity)
                    loginPreference.setLogin(response.loginResult?.name ?: "", response.loginResult?.userId ?: "", response.loginResult?.token ?: "")
                    val loginData = loginPreference.getLogin()
                    Log.d("Login Data", "Name: ${loginData.name}, UserID: ${loginData.userId}, Token: ${loginData.token}")
                    moveToMainActivity()
                } else {
                    showToast(response.message)
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
                showToast(errorResponse.message)
            } catch (e: Exception) {
                showToast("An error occurred. Please try again.")
                e.printStackTrace()
            } finally {
                // Sembunyikan ProgressBar
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun moveToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun moveToBackToMenu4() {
        val intent = Intent(this, OnBoardingActivity::class.java)
        intent.putExtra("navigateToMenu", 4)
        startActivity(intent)
        finish()
    }
}