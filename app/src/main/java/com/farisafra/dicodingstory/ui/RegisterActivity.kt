package com.farisafra.dicodingstory.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputBinding
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.farisafra.dicodingstory.R
import com.farisafra.dicodingstory.data.response.register.RegisterResponse
import com.farisafra.dicodingstory.data.retrofit.ApiClient
import com.farisafra.dicodingstory.databinding.ActivityRegisterBinding
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { moveToBackToMenu4() }
        binding.BtnRegister.setOnClickListener {
            val name = binding.edRegisterName.text.toString().trim()
            val email = binding.edRegisterEmail.text.toString().trim()
            val password = binding.edRegisterPassword.text.toString().trim()

            // Validasi input sebelum melakukan registrasi
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                showToast("Please fill in all fields")
            } else {
                // Lakukan proses registrasi
                register(name, email, password)
            }
        }
    }

    private fun register(name: String, email: String, password: String) {

        progressBar.visibility = View.VISIBLE
        // Membuat instance dari ApiService
        val apiService = ApiClient.getApiService(this)

        // Memanggil endpoint register dengan menggunakan suspend function
        lifecycleScope.launch {
            try {
                // Mengirim data registrasi ke server
                val response = apiService.register(name, email, password)

                // Jika tidak terjadi error
                if (!response.error) {
                    // Menampilkan pesan sukses
                    showToast(response.message)

                    // Redirect ke halaman login atau halaman utama
                    moveToLoginPage()
                } else {
                    // Menampilkan pesan error
                    showToast(response.message)
                }
            } catch (e: HttpException) {
                // Handling error jika terjadi kesalahan pada HTTP request
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
                showToast(errorResponse.message)
            } catch (e: Exception) {
                // Handling error jika terjadi kesalahan lainnya
                showToast("Terjadi kesalahan. Silakan coba lagi.")
                e.printStackTrace()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun moveToBackToMenu4() {
        val intent = Intent(this, OnBoardingActivity::class.java)
        intent.putExtra("navigateToMenu", 4)
        startActivity(intent)
        finish()
    }

    private fun moveToLoginPage() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}