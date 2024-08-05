package com.mfitrahrmd.story

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

open class RequireAuthentication(
    private val loginActivity: Class<*>
) : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val authentication = (application as StoryApplication).applicationContainer.authentication
        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.RESUMED) {
//                authentication.getToken().collectLatest {
//                    if (it.isEmpty()) {
//                        startActivity(Intent(this@RequireAuthentication, loginActivity).apply {
//                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                        })
//                    }
//                }
//            }
        }
    }
}