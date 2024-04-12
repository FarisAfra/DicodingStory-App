package com.farisafra.dicodingstory.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.farisafra.dicodingstory.data.response.story.Story
import com.farisafra.dicodingstory.databinding.ActivityDetailBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getDetailData()
        backActivity()
    }

    private fun getDetailData() {
        val story = intent.getParcelableExtra<Story>("EXTRA_STORY")

        binding.apply {
            tvDetailName.text = story?.name
            tvDetailDescription.text = story?.description
            val simpleDateFormatApi = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val date = story?.createdAt?.let { simpleDateFormatApi.parse(it) }

            if (date != null) {
                val timeZone = TimeZone.getTimeZone("Asia/Jakarta")
                val sdf = SimpleDateFormat("dd MMMM yyyy 'at' HH:mm 'WIB'", Locale.ENGLISH)
                sdf.setTimeZone(timeZone)

                val adjustedTime = Calendar.getInstance().apply {
                    time = date
                    add(Calendar.HOUR_OF_DAY, 7)
                }.time

                val formattedAdjustedTime = sdf.format(adjustedTime)
                tvCreatAt.text = formattedAdjustedTime
            } else {
                tvCreatAt.text = "Error: Could not parse creation time"
            }

            Glide.with(this@DetailActivity)
                .load(story?.photoUrl)
                .into(ivDetailPhoto)
        }
    }

    private fun backActivity() {
        binding.btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}