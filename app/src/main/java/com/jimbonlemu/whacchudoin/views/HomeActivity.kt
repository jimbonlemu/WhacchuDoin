package com.jimbonlemu.whacchudoin.views

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jimbonlemu.whacchudoin.R
import com.jimbonlemu.whacchudoin.core.CoreActivity
import com.jimbonlemu.whacchudoin.databinding.ActivityHomeBinding
import com.jimbonlemu.whacchudoin.view_models.AuthViewModel
import com.jimbonlemu.whacchudoin.view_models.GetAllStoryViewModel
import com.jimbonlemu.whacchudoin.views.adapters.LoaderStateAdapter
import com.jimbonlemu.whacchudoin.views.adapters.StoryPagingAdapter
import org.koin.android.ext.android.inject

class HomeActivity : CoreActivity<ActivityHomeBinding>() {
    private val getAllStoryViewModel: GetAllStoryViewModel by inject()
    private lateinit var storyPagingAdapter: StoryPagingAdapter
    private val authViewModel: AuthViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupAppBar()
        setupObserver()
        getAllStoryViewModel.getAllStories()
        setupSwipeRefresh()
    }

    private fun setupObserver() {
        storyPagingAdapter = StoryPagingAdapter()
        val linearLayoutManager = LinearLayoutManager(this)
        val itemDecoration = DividerItemDecoration(this, linearLayoutManager.orientation)

        binding.rvStory.apply {
            adapter = storyPagingAdapter.withLoadStateFooter(
                footer = LoaderStateAdapter { storyPagingAdapter.retry() }
            )
            layoutManager = linearLayoutManager
            addItemDecoration(itemDecoration)
        }

        getAllStoryViewModel.storyResult.observe(this) { result ->
            storyPagingAdapter.submitData(this@HomeActivity.lifecycle, result)
        }

        storyPagingAdapter.addLoadStateListener { loadState ->
            val isListEmpty = loadState.refresh is LoadState.NotLoading && storyPagingAdapter.itemCount == 0
            binding.rvStory.isVisible = !isListEmpty
            binding.emptyView.isVisible = isListEmpty
            binding.itemStoryLoader.root.isVisible = loadState.source.refresh is LoadState.Loading
            binding.swipeRefreshLayout.isRefreshing = loadState.mediator?.refresh is LoadState.Loading
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            storyPagingAdapter.refresh()
        }
    }

    private fun setupAppBar() {
        binding.abToolbar.setOnMenuItemClickListener { itemClicked ->
            when (itemClicked.itemId) {
                R.id.action_add_story -> {
                    startActivity(Intent(this@HomeActivity, AddStoryActivity::class.java))
                    true
                }

                R.id.action_view_map -> {
                    startActivity(Intent(this@HomeActivity, MapsActivity::class.java))
                    true
                }

                R.id.action_logout -> {
                    AlertDialog.Builder(this).apply {
                        setTitle(getString(R.string.confirmation))
                        setMessage(getString(R.string.log_out_confirm))
                        setPositiveButton(getString(R.string.yes_answer)) { _, _ ->
                            authViewModel.logout()
                            val intent = Intent(context, LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
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

    override fun setupBinding(layoutInflater: LayoutInflater): ActivityHomeBinding =
        ActivityHomeBinding.inflate(layoutInflater)
}
