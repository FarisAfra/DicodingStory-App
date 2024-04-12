package com.farisafra.dicodingstory.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.farisafra.dicodingstory.R
import com.farisafra.dicodingstory.data.preferences.LoginPreference
import com.farisafra.dicodingstory.data.viewmodel.StoriesViewModel
import com.farisafra.dicodingstory.data.viewmodel.ViewModelFactory
import com.farisafra.dicodingstory.databinding.ActivityMainBinding
import com.farisafra.dicodingstory.ui.adapter.StoryAdapter
import com.farisafra.dicodingstory.ui.customview.ResponseView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var loginPreference: LoginPreference
    private lateinit var storyAdapter: StoryAdapter
    private lateinit var vmFactory: ViewModelFactory
    private val storiesViewModel: StoriesViewModel by viewModels { vmFactory }
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storyAdapter = StoryAdapter(ArrayList())

        setupViewModel()
        getusername()
        setupRecyclerView()
        fetchStories()
        moveToAddStory()
        refreshPage()

    }

    private fun setupRecyclerView() {
        storyAdapter = StoryAdapter(ArrayList())
        binding.rvStories.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = storyAdapter
        }
    }

    private fun setupViewModel() {
        vmFactory = ViewModelFactory.getInstance(binding.root.context)
    }

    private fun fetchStories() {
        storiesViewModel.getStories().observe(this) { result ->
            if (result != null) {
                when (result) {
                    is com.farisafra.dicodingstory.data.repository.Result.Loading -> {
                        binding?.progressBar?.visibility = View.VISIBLE
                    }
                    is com.farisafra.dicodingstory.data.repository.Result.Success -> {
                        binding?.progressBar?.visibility = View.GONE
                        val storyData = result.data.listStory
                        storyAdapter.submitList(storyData)
                        swipeRefreshLayout.isRefreshing = false
                    }
                    is com.farisafra.dicodingstory.data.repository.Result.Error -> {
                        binding?.progressBar?.visibility = View.GONE
                        errorResponse()
                    }
                }
            }
        }
    }

    private fun getusername() {
        loginPreference = LoginPreference(this)
        val userData = loginPreference.getLogin()
        binding.tvUsername.text = userData.userId

        binding.btnProfil.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun refreshPage() {
        swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            fetchStories()
            Toast.makeText(this, R.string.updateData, Toast.LENGTH_SHORT).show()
        }
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
                Toast.makeText(this, R.string.backToExit, Toast.LENGTH_SHORT).show()
                Handler().postDelayed({ backPressedOnce = false }, 2000)
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun errorResponse() {
        ResponseView(this, R.string.error_message, R.drawable.symbols_error).show()
        refreshPage()
    }
}
