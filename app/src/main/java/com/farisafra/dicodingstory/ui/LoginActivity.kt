package com.farisafra.dicodingstory.ui

import LoginViewModel
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import com.farisafra.dicodingstory.R
import com.farisafra.dicodingstory.data.preferences.LoginPreference
import com.farisafra.dicodingstory.data.response.login.LoginResponse
import com.farisafra.dicodingstory.data.viewmodel.ViewModelFactory
import com.farisafra.dicodingstory.databinding.ActivityLoginBinding
import com.farisafra.dicodingstory.ui.customview.ResponseView

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var progressBar: ProgressBar
    private lateinit var vmFactory: ViewModelFactory
    private val loginViewModel: LoginViewModel by viewModels { vmFactory }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressBar = binding.progressBar

        loginBtnClick()
        backActivity()
        moveToRegister()
        setupViewModel()
        playAnimation()
    }

    private fun setupViewModel() {
        vmFactory = ViewModelFactory.getInstance(binding.root.context)
    }

    private fun loginBtnClick() {
        binding.BtnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString().trim()
            val password = binding.edLoginPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || password.length < 8 || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, R.string.fill_data, Toast.LENGTH_SHORT).show()
            } else {
                login(email, password)
            }
        }
    }

    private fun login(email: String, password: String) {
        loginViewModel.postLogin(email, password).observe(this@LoginActivity) { result ->
            if (result != null) {
                when(result) {
                    is com.farisafra.dicodingstory.data.repository.Result.Loading -> {
                        progressBar.visibility = View.VISIBLE
                    }
                    is com.farisafra.dicodingstory.data.repository.Result.Error -> {
                        progressBar.visibility = View.GONE
                        errorResponse()
                    }
                    is com.farisafra.dicodingstory.data.repository.Result.Success -> {
                        progressBar.visibility = View.GONE
                        successLoginHandler(result.data)
                    }
                }
            }
        }
    }

    private fun successLoginHandler(loginResponse: LoginResponse) {
        saveData(loginResponse)
        Toast.makeText(this, R.string.login_success, Toast.LENGTH_SHORT).show()
        moveToMainActivity()
    }

    private fun saveData(response: LoginResponse) {
        val loginPreference = LoginPreference(this)
        loginPreference.setLogin(response.loginResult?.name ?: "", response.loginResult?.userId ?: "", response.loginResult?.token ?: "")
    }

    private fun errorResponse() {
        ResponseView(this, R.string.error_message, R.drawable.symbols_error).show()
    }

    private fun moveToMainActivity() {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
    }

    private fun moveToRegister() {
        binding.btnDaftar.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun backActivity() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.ivLogin, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val tvLogin = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1f).setDuration(300)
        val descLogin1 = ObjectAnimator.ofFloat(binding.desc1, View.ALPHA, 1f).setDuration(300)
        val descLogin2 = ObjectAnimator.ofFloat(binding.desc2, View.ALPHA, 1f).setDuration(300)
        val tvEmail = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(300)
        val edEmail = ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 1f).setDuration(300)
        val tvPass = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(300)
        val edPass = ObjectAnimator.ofFloat(binding.edLoginPassword, View.ALPHA, 1f).setDuration(300)
        val btnLogin = ObjectAnimator.ofFloat(binding.BtnLogin, View.ALPHA, 1f).setDuration(300)
        val tvTanya = ObjectAnimator.ofFloat(binding.tvTanyaAkun, View.ALPHA, 1f).setDuration(300)
        val btnDaftar = ObjectAnimator.ofFloat(binding.btnDaftar, View.ALPHA, 1f).setDuration(300)

        val together = AnimatorSet().apply {
            playTogether(btnLogin,tvTanya,btnDaftar)
        }

        AnimatorSet().apply {
            playSequentially(tvLogin,descLogin1,descLogin2,tvEmail,edEmail,tvPass,edPass,together)
            start()
        }
    }
}