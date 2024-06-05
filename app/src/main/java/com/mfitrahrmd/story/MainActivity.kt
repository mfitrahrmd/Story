package com.mfitrahrmd.story

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.mfitrahrmd.story.data.Result
import com.mfitrahrmd.story.data.entity.User
import com.mfitrahrmd.story.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import okhttp3.internal.wait

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        with(viewBinding) {
            btnLogin.setOnClickListener {
                val authenticationRepository =
                    (application as StoryApplication).applicationContainer.authenticationRepository
                val authentication =
                    (application as StoryApplication).applicationContainer.authentication
                lifecycleScope.launch {
                    val result = authenticationRepository.login(
                        User.Account(
                            "tgcfitrah26@gmail.com",
                            "12345678",
                            ""
                        )
                    )
                    when (result) {
                        is Result.Success -> {
                            authentication.setToken {
                                result.data.token
                            }
                            startActivity(
                                Intent(
                                    this@MainActivity,
                                    StoryActivity::class.java
                                ).apply {
                                    flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                })
                        }

                        is Result.Failed -> {
                            Toast.makeText(
                                this@MainActivity,
                                "Login Failed : ${result.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
    }
}