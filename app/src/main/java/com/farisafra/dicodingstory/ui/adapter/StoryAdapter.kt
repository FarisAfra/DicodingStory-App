package com.farisafra.dicodingstory.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.farisafra.dicodingstory.data.response.story.Story
import com.farisafra.dicodingstory.databinding.ItemStoryBinding
import com.farisafra.dicodingstory.ui.DetailActivity

class StoryAdapter(private val stories: ArrayList<Story>)
    : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    inner class StoryViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val context = binding.root.context
            val intent = Intent(context, DetailActivity::class.java)

            // Mengambil posisi item yang diklik
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                // Mengambil data dari item yang diklik
                val story = stories[position]

                // Menambahkan objek Story sebagai ekstra pada Intent
                intent.putExtra("EXTRA_STORY", story)

                // Memulai DetailActivity dengan Intent yang telah diberi data ekstra
                context.startActivity(intent)
            }
        }

            fun bind(story: Story) {

            binding.apply {
                tvItemName.text = story.name
                tvDesc.text = story.description
                tvCreatAt.text = story.createdAt

                Glide.with(itemView.context)
                    .load(story.photoUrl)
                    .into(ivItemPhoto)

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = stories[position]
        holder.bind(story)
    }

    override fun getItemCount(): Int {
        return stories.size
    }

    fun setData(newStories: List<Story>) {
        stories.clear()
        stories.addAll(newStories)
        notifyDataSetChanged()
    }
}


