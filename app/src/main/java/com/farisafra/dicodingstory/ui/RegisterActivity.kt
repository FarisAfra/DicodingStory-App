package com.farisafra.dicodingstory.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import com.farisafra.dicodingstory.R
import com.farisafra.dicodingstory.data.repository.Result
import com.farisafra.dicodingstory.data.viewmodel.RegisterViewModel
import com.farisafra.dicodingstory.data.viewmodel.ViewModelFactory
import com.farisafra.dicodingstory.databinding.ActivityRegisterBinding
import com.farisafra.dicodingstory.ui.customview.ResponseView

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var progressBar: ProgressBar
    private lateinit var vmFactory: ViewModelFactory
    private val registerViewModel: RegisterViewModel by viewModels { vmFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressBar = binding.progressBar

        setupViewModel()
        backActivity()
        registerBtnClick()

    }

    private fun setupViewModel() {
        vmFactory = ViewModelFactory.getInstance(binding.root.context)
    }

    private fun registerBtnClick() {
        binding.BtnRegister.setOnClickListener {
            val name = binding.edRegisterName.text.toString().trim()
            val email = binding.edRegisterEmail.text.toString().trim()
            val password = binding.edRegisterPassword.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || password.length < 8 || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this@RegisterActivity, R.string.fill_data, Toast.LENGTH_SHORT).show()
            } else {
                register(name, email, password)
            }
        }
    }

    private fun register(name: String, email: String, password: String) {
        registerViewModel.postRegister(name, email, password).observe(this@RegisterActivity) { result ->
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
                        successRegister()
                    }
                }
            }
        }
    }

    private fun successRegister() {
        binding.edRegisterName.text?.clear()
        binding.edRegisterEmail.text?.clear()
        binding.edRegisterPassword.text?.clear()
        ResponseView(this, R.string.register_message, R.drawable.registered, moveToLoginPage()).show()
    }

    private fun moveToLoginPage(): () -> Unit {
        return {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun errorResponse() {
        ResponseView(this, R.string.error_message, R.drawable.symbols_error).show()
    }
    private fun backActivity() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

}