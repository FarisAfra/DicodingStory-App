package com.farisafra.dicodingstory.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.farisafra.dicodingstory.data.preferences.LoginPreference
import com.farisafra.dicodingstory.data.response.story.Story
import com.farisafra.dicodingstory.data.retrofit.ApiClient
import com.farisafra.dicodingstory.databinding.ActivityMainBinding
import com.farisafra.dicodingstory.ui.adapter.StoryAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainActivity : AppCompatActivity(), View.OnClickListener {
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

        // Initialize storyAdapter
        storyAdapter = StoryAdapter(ArrayList())



        setupRecyclerView()
        fetchStories()
        moveToAddStory()
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
                val loginPreference = LoginPreference(this@MainActivity)
                val token = loginPreference.getToken() ?: ""

                val response = ApiClient.getApiService(token).getAllStories()
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

    override fun onClick(v: View?) {

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

    private fun moveToAddStory() {
        binding.addStory.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }
    }

    private var backPressedOnce = false

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event?.action == KeyEvent.ACTION_DOWN) {
            if (backPressedOnce) {
                finishAffinity()
            } else {
                backPressedOnce = true
                showToast("Tekan sekali lagi untuk keluar")
                Handler().postDelayed({ backPressedOnce = false }, 2000) // Setelah 2 detik, reset backPressedOnce menjadi false
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
