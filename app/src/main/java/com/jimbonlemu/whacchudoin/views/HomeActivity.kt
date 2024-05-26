package com.jimbonlemu.whacchudoin.views

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.jimbonlemu.whacchudoin.R
import com.jimbonlemu.whacchudoin.core.CoreActivity
import com.jimbonlemu.whacchudoin.data.network.response.ResponseState
import com.jimbonlemu.whacchudoin.databinding.ActivityHomeBinding
import com.jimbonlemu.whacchudoin.view_models.AuthViewModel
import com.jimbonlemu.whacchudoin.view_models.GetAllStoryViewModel
import com.jimbonlemu.whacchudoin.views.adapters.StoryPagingAdapter
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class HomeActivity : CoreActivity<ActivityHomeBinding>() {
    private val getAllStoryViewModel: GetAllStoryViewModel by inject()
    private lateinit var storyPagingAdapter: StoryPagingAdapter
    private val authViewModel: AuthViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupPagingAdapterAndRV()
        setupObserver()
        setupAppBar()
        setupSwipeRefresh()
    }

    private fun setupPagingAdapterAndRV() {
        binding.apply {
            storyPagingAdapter = StoryPagingAdapter().apply {
                addOnPagesUpdatedListener {
                    rvStory.scrollToPosition(0)
                }
            }
            rvStory.apply {
                val linearLayoutManager = LinearLayoutManager(this@HomeActivity)
                this.layoutManager = linearLayoutManager
                adapter = storyPagingAdapter
            }
        }
    }

    private fun setupObserver() {
        getAllStoryViewModel.stories.observe(this) { response ->
            binding.apply {
                when (response) {
                    is ResponseState.Loading -> {
                        setupLoader(true)
                        rvStory.visibility = View.INVISIBLE
                    }

                    is ResponseState.Success -> {
                        setupLoader(false)
                        lifecycleScope.launch {
                            storyPagingAdapter.submitData(response.data)
                        }
                        rvStory.visibility = View.VISIBLE
                    }

                    is ResponseState.Error -> {
                        setupLoader(false)
                        rvStory.visibility = View.VISIBLE

                    }
                }
            }
        }
        getAllStoryViewModel.getAllStories()
    }

    private fun setupLoader(isLoaderEnabled: Boolean) {
        binding.apply {
            if (isLoaderEnabled) {
                itemStoryLoader.root.visibility = View.VISIBLE
                itemStoryLoader.root.startShimmer()
                swipeRefreshLayout.isRefreshing = true
            } else {
                itemStoryLoader.root.stopShimmer()
                itemStoryLoader.root.visibility = View.GONE
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun setupAppBar() {
        binding.abToolbar.setOnMenuItemClickListener { itemClicked ->
            when (itemClicked.itemId) {
                R.id.action_add_story -> {
                    startActivity(Intent(this@HomeActivity, AddStoryActivity::class.java))
                    true
                }

                R.id.action_logout -> {
                    AlertDialog.Builder(this).apply {
                        setTitle(getString(R.string.confirmation))
                        setMessage(getString(R.string.log_out_confirm))
                        setPositiveButton(getString(R.string.yes_answer)) { _, _ ->
                            authViewModel.logout()
                            val intent = Intent(context, LoginActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }
                        setNegativeButton(getString(R.string.no_answer)) { dialog, _ ->
                            dialog.dismiss()
                        }
                        create()
                        show()
                    }
                    true
                }

                else -> false
            }
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            getAllStoryViewModel.getAllStories()
            storyPagingAdapter.refresh()
        }
    }

    override fun setupBinding(layoutInflater: LayoutInflater): ActivityHomeBinding =
        ActivityHomeBinding.inflate(layoutInflater)
}