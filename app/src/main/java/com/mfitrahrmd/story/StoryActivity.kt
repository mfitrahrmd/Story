package com.mfitrahrmd.story

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.mfitrahrmd.story.data.util.ImageProvider
import com.mfitrahrmd.story.databinding.ActivityStoryBinding
import com.mfitrahrmd.story.databinding.PermissionBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StoryActivity : AppCompatActivity() {
    private lateinit var activityStoryBinding: ActivityStoryBinding
    private lateinit var permissionBinding: PermissionBinding
    private val viewModel: StoryViewModel by lazy {
        ViewModelProvider(this, AppViewModelProvider.Factory)[StoryViewModel::class.java]
    }
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                showMainContent()
            } else {
                showPermissionDeniedExplanation()
                if (!shouldShowRequestPermissionRationale(REQUIRED_PERMISSION)) {
                    showPermissionDeniedPermanentlyDialog()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (allPermissionGranted()) {
            showMainContent()
        } else {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }
        observe()
    }

    private fun showMainContent() {
        if (!this::activityStoryBinding.isInitialized) {
            activityStoryBinding = ActivityStoryBinding.inflate(layoutInflater)
        }
        setContentView(activityStoryBinding.root)
        setSupportActionBar(activityStoryBinding.toolbar)
        with(activityStoryBinding) {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
            bottomNavigation.setupWithNavController(navHostFragment.navController)
        }
        setupBtnCreateStory()
    }

    private fun showPermissionDeniedExplanation() {
        if (!this::permissionBinding.isInitialized) {
            permissionBinding = PermissionBinding.inflate(layoutInflater)
        }
        setContentView(permissionBinding.root)
        with(permissionBinding) {
            btnAllow.setOnClickListener {
                requestPermissionLauncher.launch(REQUIRED_PERMISSION)
            }
        }
    }

    private fun showPermissionDeniedPermanentlyDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.permission_needed))
            .setMessage(getString(R.string.permission_camera_denied_permanently))
            .setPositiveButton("Go to Settings") { dialog, which ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun setupBtnCreateStory() {
        with(activityStoryBinding) {
            btnCreateStory.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.session.getName().collect { name ->
                        val createStoryFragment = CreateStoryFragment(name) { story, uri ->
                            viewModel.createStoryAsGuest(
                                story,
                                ImageProvider.uriToImageFile(uri, this@StoryActivity)
                            )
                        }
                        createStoryFragment.show(supportFragmentManager, null)
                    }
                }
            }
        }
    }

    private fun allPermissionGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.session.getToken().collectLatest {
                    if (it.isEmpty()) {
                        startActivity(Intent(this@StoryActivity, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        })
                    }
                }
            }
        }
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}