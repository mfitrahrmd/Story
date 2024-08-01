package com.mfitrahrmd.story

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.mfitrahrmd.story.data.Result
import com.mfitrahrmd.story.data.util.ImageProvider
import com.mfitrahrmd.story.databinding.ActivityStoryBinding
import kotlinx.coroutines.launch

class StoryActivity : RequireAuthentication(
    MainActivity::class.java
) {
    private lateinit var binding: ActivityStoryBinding
    private val viewModel: StoryViewModel by lazy {
        ViewModelProvider(this, AppViewModelProvider.Factory)[StoryViewModel::class.java]
    }
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setupBtnCreateStory()
        if (!allPermissionGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }
    }

    private fun setupBtnCreateStory() {
        with(binding) {
            btnCreateStory.setOnClickListener {
                val createStoryFragment = CreateStoryFragment { story, uri ->
                    viewModel.createStoryAsGuest(story, ImageProvider.uriToImageFile(uri, this@StoryActivity))
                }
                createStoryFragment.show(supportFragmentManager, null)
            }
        }
    }

    private fun allPermissionGranted() =
        ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSION) == PackageManager.PERMISSION_GRANTED


    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}