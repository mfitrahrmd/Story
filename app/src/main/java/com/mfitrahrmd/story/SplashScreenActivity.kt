package com.mfitrahrmd.story

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mfitrahrmd.story.databinding.ActivitySplashScreenBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Check authentication status
        val authentication = (application as StoryApplication).applicationContainer.authentication
        lifecycleScope.launch {
            authentication.getToken().collectLatest {
                if (it.isNotEmpty()) {
                    // If authenticated, launch HomeActivity
                    startActivity(Intent(this@SplashScreenActivity, StoryActivity::class.java))
                } else {
                    // If not authenticated, launch LoginActivity
                    startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                }
            }
        }

        // Finish the splash screen activity so the user cannot go back to it
        finish()
    }
}
