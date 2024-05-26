package com.jimbonlemu.whacchudoin.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.jimbonlemu.whacchudoin.core.CoreActivity
import com.jimbonlemu.whacchudoin.data.network.response.ResponseState
import com.jimbonlemu.whacchudoin.databinding.ActivityDetailBinding
import com.jimbonlemu.whacchudoin.view_models.GetDetailStoryViewModel
import org.koin.android.ext.android.inject

class DetailActivity : CoreActivity<ActivityDetailBinding>() {
    private val storyViewModel: GetDetailStoryViewModel by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val passedValue = intent.extras?.getString(STORY_ID)
        passedValue?.let { storyViewModel.getDetailStory(it) }
        setupObserver()
    }

    override fun setupBinding(layoutInflater: LayoutInflater): ActivityDetailBinding =
        ActivityDetailBinding.inflate(layoutInflater)

    private fun setupLoader(isEnable: Boolean) {
        binding.apply {
            if (isEnable) {
                layoutDetailLoader.root.visibility = View.VISIBLE
                layoutDetailLoader.root.startShimmer()
            } else {
                layoutDetailLoader.root.visibility = View.GONE
                layoutDetailLoader.root.stopShimmer()
            }
        }
    }

    private fun setupObserver() {
        storyViewModel.detailStory.observe(this) { response ->
            when (response) {
                is ResponseState.Loading -> {
                    setupLoader(true)
                }

                is ResponseState.Success -> {
                    setupLoader(false)
                    response.data.story.apply {
                        binding.layoutDetail.apply {
                            Glide.with(this@DetailActivity).load(photoUrl).into(ivDetailPhoto)
                            tvDetailName.text = name
                            tvDetailDescription.text = description
                        }
                    }
                }

                is ResponseState.Error -> {
                    setupLoader(false)
                    Toast.makeText(this, "Failed to load detail of story", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        const val STORY_ID = "story_id"
    }

}