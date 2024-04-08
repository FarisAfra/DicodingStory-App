package com.farisafra.dicodingstory.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.farisafra.dicodingstory.data.response.story.Story
import com.farisafra.dicodingstory.data.viewmodel.DetailViewModel
import com.farisafra.dicodingstory.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val story = intent.getParcelableExtra<Story>("EXTRA_STORY")


        // Menampilkan data Story di layout
        binding.apply {
            tvDetailName.text = story?.name
            tvDetailDescription.text = story?.description
            tvCreatAt.text = story?.createdAt

            Glide.with(this@DetailActivity)
                .load(story?.photoUrl)
                .into(ivDetailPhoto)
        }

        backActivity()
    }

    private fun backActivity() {
        binding.btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}