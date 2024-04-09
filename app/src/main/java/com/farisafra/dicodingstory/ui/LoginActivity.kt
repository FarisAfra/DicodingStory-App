package com.farisafra.dicodingstory.ui

import LoginViewModel
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
    lateinit var binding: ActivityLoginBinding
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
    }

    private fun setupViewModel() {
        vmFactory = ViewModelFactory.getInstance(binding.root.context)
    }

    private fun loginBtnClick() {
        binding.BtnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString().trim()
            val password = binding.edLoginPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || password.length < 8 || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showToast("Tolong Isi Data Sesuai dengan ketentuan")
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
        showToast("Telah Berhasil Login")
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

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}