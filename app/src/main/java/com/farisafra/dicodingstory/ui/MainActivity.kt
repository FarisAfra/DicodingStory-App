package com.farisafra.dicodingstory.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import androidx.recyclerview.widget.LinearLayoutManager
import com.farisafra.dicodingstory.R
import com.farisafra.dicodingstory.data.preferences.LoginPreference
import com.farisafra.dicodingstory.data.response.story.Story
import com.farisafra.dicodingstory.data.retrofit.ApiClient
import com.farisafra.dicodingstory.databinding.ActivityMainBinding
import com.farisafra.dicodingstory.ui.adapter.StoryAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var loginPreference: LoginPreference // Tambahkan ini
    private lateinit var storyAdapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginPreference = LoginPreference(this)

        val userData = loginPreference.getLogin()

        binding.addStory.setOnClickListener {  }

        binding.tvUsername.text = userData.userId

        binding.actionLogout.setOnClickListener {
            // Panggil clearLogin() ketika tombol logout ditekan
            loginPreference.clearLogin()
            // Setelah logout, arahkan pengguna ke halaman login atau onboarding
            moveToLoginOrOnBoarding()
        }

        setupRecyclerView()
        fetchStories()
    }

    private fun setupRecyclerView() {
        storyAdapter = StoryAdapter(ArrayList())
        binding.rvStories.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = storyAdapter
        }
    }

    private fun fetchStories() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.getApiService(this@MainActivity).getAllStories()
                if (!response.error) {
                    updateStories(response.listStory)
                } else {
                    showError(response.message)
                }
            } catch (e: HttpException) {
                showError("An error occurred. Please try again.")
                e.printStackTrace()
            } catch (e: Exception) {
                showError("An error occurred. Please try again.")
                e.printStackTrace()
            }
        }
    }

    private fun updateStories(stories: ArrayList<Story>) {
        runOnUiThread {
            storyAdapter.setData(stories)
        }
    }

    private fun showError(message: String) {
        // Show error message to the user
    }

    private fun moveToLoginOrOnBoarding() {
        val intent = Intent(this, OnBoardingActivity::class.java)
        startActivity(intent)
        finish() // Selesai dengan MainActivity setelah logout
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event?.action == KeyEvent.ACTION_DOWN) {
            finishAffinity()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
