package com.jimbonlemu.whacchudoin.views

import android.os.Bundle
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import com.jimbonlemu.whacchudoin.core.CoreActivity
import com.jimbonlemu.whacchudoin.data.network.dto.Story
import com.jimbonlemu.whacchudoin.databinding.ActivityDetailBinding
import com.jimbonlemu.whacchudoin.view_models.GetDetailStoryViewModel
import org.koin.android.ext.android.inject

class DetailActivity : CoreActivity<ActivityDetailBinding>() {
    private val storyViewModel: GetDetailStoryViewModel by inject()
    private var passedValue: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        passedValue = intent.extras?.getString(STORY_ID)
        setupObserver()
    }

    override fun setupBinding(layoutInflater: LayoutInflater): ActivityDetailBinding =
        ActivityDetailBinding.inflate(layoutInflater)


    private fun setupObserver() {
        storyViewModel.getDetailStory(passedValue.toString())
            .observe(this@DetailActivity) { storyValue ->
                setValueDetailItem(storyValue)
            }
    }

    private fun setValueDetailItem(story: Story) {
        binding.layoutDetail.apply {
            with(story) {
                tvDetailName.text = name
                tvDetailDescription.text = description
                Glide.with(this@DetailActivity).load(photoUrl)
                    .into(ivDetailPhoto)
            }
        }
    }


    companion object {
        const val STORY_ID = "story_id"
    }

}