package com.mfitrahrmd.story

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.mfitrahrmd.story.databinding.ActivityStoryBinding
import kotlinx.coroutines.launch

class StoryActivity : RequireAuthentication(
    MainActivity::class.java
) {
    private lateinit var viewBinding: ActivityStoryBinding

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
            btnLogout.setOnClickListener {
                val authentication = (application as StoryApplication).applicationContainer.authentication
                lifecycleScope.launch {
                    authentication.setToken {
                        ""
                    }
                }
            }
        }
    }
}