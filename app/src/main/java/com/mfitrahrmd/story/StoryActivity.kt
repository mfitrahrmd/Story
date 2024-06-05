package com.mfitrahrmd.story

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfitrahrmd.story.databinding.ActivityStoryBinding
import com.mfitrahrmd.story.ui.adapter.StoryAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StoryActivity : RequireAuthentication(
    MainActivity::class.java
) {
    private lateinit var viewBinding: ActivityStoryBinding
    private val storyAdapter = StoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewBinding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        with(viewBinding) {
            rv.apply {
                layoutManager = LinearLayoutManager(this@StoryActivity)
                adapter = storyAdapter
            }
        }
        val authentication =
            (application as StoryApplication).applicationContainer.authentication
        val storyRepository =
            (application as StoryApplication).applicationContainer.storyRepository
        Toast.makeText(this@StoryActivity, "Story Activity", Toast.LENGTH_LONG).show()
        lifecycleScope.launch {
            authentication.getToken().collect { token ->
                val storyPaging =
                    storyRepository.getAllStories(token, null, null, null)
                storyPaging.collect {
                    storyAdapter.submitData(lifecycle, it)
                }
            }
        }
    }
}