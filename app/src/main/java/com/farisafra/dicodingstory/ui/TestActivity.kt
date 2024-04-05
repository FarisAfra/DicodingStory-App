package com.farisafra.dicodingstory.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.farisafra.dicodingstory.R
import com.farisafra.dicodingstory.data.response.login.LoginResponse
import com.farisafra.dicodingstory.data.retrofit.ApiService
import com.farisafra.dicodingstory.databinding.ActivityTestBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TestActivity : AppCompatActivity() {
    lateinit var binding: ActivityTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}