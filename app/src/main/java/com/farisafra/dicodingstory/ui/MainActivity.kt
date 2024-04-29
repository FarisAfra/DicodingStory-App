package com.farisafra.dicodingstory.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.farisafra.dicodingstory.data.repository.Result
import com.farisafra.dicodingstory.data.response.story.Story
import com.farisafra.dicodingstory.ui.adapter.LoadingStateAdapter

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

        storyAdapter = StoryAdapter(StoryAdapter.DIFF_CALLBACK) // Use the DiffCallback
        setupViewModel()
        getusername()
        setupRecyclerView()
        fetchStories()
        moveToAddStory()
        refreshPage()
        moveToMaps()
    }

    private fun setupRecyclerView() {
        binding.rvStories.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = storyAdapter
        }
    }

    private fun setupViewModel() {
        vmFactory = ViewModelFactory.getInstance(binding.root.context)
    }

    private fun fetchStories() {
        val adapter = storyAdapter
        binding.rvStories.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        storiesViewModel.getstory.observe(this) { pagingData ->
            storyAdapter.submitData(lifecycle, pagingData)
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
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun moveToAddStory() {
        binding.addStory.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun moveToMaps() {
        binding.btnMaps.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
    }

    // Handle back button logic (optional)
    private var backPressedOnce = false

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event?.action == KeyEvent.ACTION_DOWN) {
            if (backPressedOnce) {
                finishAffinity()
            } else {
                backPressedOnce = true
                Toast.makeText(this, R.string.backToExit, Toast.LENGTH_SHORT).show()
                Handler(Looper.getMainLooper()).postDelayed({ backPressedOnce = false }, EXIT_DELAY)
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    companion object {
        private const val EXIT_DELAY = 2000L
    }
}
