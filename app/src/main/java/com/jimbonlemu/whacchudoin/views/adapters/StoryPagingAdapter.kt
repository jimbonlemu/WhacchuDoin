package com.jimbonlemu.whacchudoin.views.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jimbonlemu.whacchudoin.data.network.dto.Story
import com.jimbonlemu.whacchudoin.databinding.LayoutItemStoryBinding
import com.jimbonlemu.whacchudoin.views.DetailActivity

class StoryPagingAdapter : PagingDataAdapter<Story, StoryPagingAdapter.StoryViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = LayoutItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(story)
        }
    }

    inner class StoryViewHolder(private val binding: LayoutItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story) {
            binding.apply {
                Glide.with(root).load(story.photoUrl).into(ivItemPhoto)
                tvItemName.text = story.name
                root.setOnClickListener {
                    it.context.startActivity(
                        Intent(
                            it.context,
                            DetailActivity::class.java
                        ).putExtra(DetailActivity.STORY_ID, story.id)
                    )
                }
            }
        }
    }

    companion object {
         val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }
        }
    }

}
