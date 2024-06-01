package com.mfitrahrmd.story

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

open class RequireAuthentication(
    private val loginActivity: Class<*>
) : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val authentication = (application as StoryApplication).applicationContainer.authentication
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                authentication.getToken().collectLatest {
                    Log.d("TOKEN", it)
                    if (it.isEmpty()) {
                        startActivity(Intent(this@RequireAuthentication, loginActivity).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        })
                    }
                }
            }
        }
    }
}