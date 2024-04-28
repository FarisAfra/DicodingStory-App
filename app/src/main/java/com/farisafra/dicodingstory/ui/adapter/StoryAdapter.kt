package com.farisafra.dicodingstory.ui.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.farisafra.dicodingstory.data.response.story.Story
import com.farisafra.dicodingstory.databinding.ItemStoryBinding
import com.farisafra.dicodingstory.ui.DetailActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import androidx.core.util.Pair

class StoryAdapter(private val stories: ArrayList<Story>)
    : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    inner class StoryViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.root.setOnClickListener(this)
        }


        override fun onClick(v: View?) {
            val context = binding.root.context
            val intent = Intent(context, DetailActivity::class.java)

            val ivPhoto = binding.ivItemPhoto
            val tvName = binding.tvItemName
            val tvDesc = binding.tvDesc
            val tvCreate = binding.tvCreatAt

            val optionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    itemView.context as Activity,
                    Pair(ivPhoto, "photo"),
                    Pair(tvName, "username"),
                    Pair(tvDesc, "description"),
                    Pair(tvCreate, "create"),
                )

            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val story = stories[position]

                intent.putExtra("EXTRA_STORY", story)
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }

            fun bind(story: Story) {

            binding.apply {
                tvItemName.text = story.name
                tvDesc.text = story.description
                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
                val date = simpleDateFormat.parse(story.createdAt)

                val timeZone = TimeZone.getTimeZone("Asia/Jakarta")

                val calendar = Calendar.getInstance()
                date?.let {
                    calendar.time = it
                    calendar.add(Calendar.HOUR_OF_DAY, 7)
                }

                val adjustedTime = calendar.time

                val formattedAdjustedTime = SimpleDateFormat("dd MMMM yyyy 'at' HH:mm", Locale.ENGLISH).apply {
                    setTimeZone(timeZone)
                }.format(adjustedTime)

                tvCreatAt.text = formattedAdjustedTime

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

    fun submitList(newStories: List<Story>) {
        stories.clear()
        stories.addAll(newStories)
        notifyDataSetChanged()
    }
}


