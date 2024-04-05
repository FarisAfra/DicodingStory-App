package com.farisafra.dicodingstory.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.farisafra.dicodingstory.data.response.story.Story
import com.farisafra.dicodingstory.databinding.ItemStoryBinding

class StoryAdapter(private val stories: ArrayList<Story>) : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    inner class StoryViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
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