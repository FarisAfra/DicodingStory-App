package com.farisafra.dicodingstory.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.farisafra.dicodingstory.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
//
//    private lateinit var binding : ActivityDetailBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityDetailBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        val id = intent.getStringExtra(EXTRA_ID)
//        val name = intent.getStringExtra(EXTRA_NAME)
//        val description = intent.getStringExtra(EXTRA_DESCRIPTION)
//        val photoUrl = intent.getStringExtra(EXTRA_PHOTO)
//        val createdAt = intent.getStringExtra(EXTRA_CREATED)
//        val bundle = Bundle()
//        bundle.putString(EXTRA_NAME, name)
//
//        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
//        viewModel.getStoryDetail().observe(this, {
//            if (it != null){
//                binding.apply {
//                    tvDetailName.text = it.name
//                    tvDesc.text = it.description
//                    tvCreatAt.text = it.createdAt
//
//                    Glide.with(this@DetailActivity)
//                        .load(it.photoUrl)
//                        .centerCrop()
//                        .into(ivDetailPhoto)
//                }
//            }
//        })
//    }
//
//    companion object {
//        const val EXTRA_ID = "extra_id"
//        const val EXTRA_NAME = "extra_name"
//        const val EXTRA_DESCRIPTION = "extra_description"
//        const val EXTRA_PHOTO = "extra_photo"
//        const val EXTRA_CREATED = "extra_created"
//    }
}