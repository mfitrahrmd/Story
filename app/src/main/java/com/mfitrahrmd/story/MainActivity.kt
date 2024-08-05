package com.mfitrahrmd.story

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.mfitrahrmd.story.data.Result
import com.mfitrahrmd.story.data.entity.User
import com.mfitrahrmd.story.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this, AppViewModelProvider.Factory)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        with(viewBinding) {
            btnLogin.setOnClickListener {
                if (!loginEmail.validate()) {
                    loginEmail.requestFocus()

                    return@setOnClickListener
                }
                if (!loginPassword.validate()) {
                    loginPassword.requestFocus()

                    return@setOnClickListener
                }
                btnLogin.setLoading(true)
                lifecycleScope.launch {
                    val result = viewModel.login(
                        User.Account(
                            email = loginEmail.text.toString(),
                            password = loginPassword.text.toString(),
                            token = ""
                        )
                    )
                    btnLogin.setLoading(false)
                    when (result) {
                        is Result.Success.WithData -> {
                            result.data.account?.apply {
                                viewModel.session.setToken {
                                    result.data.account.token
                                }
                                viewModel.session.setName {
                                    result.data.name
                                }
                                viewModel.session.setEmail {
                                    result.data.account.email
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
                        }

                        is Result.Success.Message -> {
                            Toast.makeText(this@MainActivity, result.message, Toast.LENGTH_SHORT)
                                .show()
                        }

                        is Result.Failed.ApiError -> {
                            lLoginEmail.error = "invalid email or password"
                            lLoginPassword.error = "invalid email or password"
                        }

                        is Result.Failed -> {
                            Toast.makeText(this@MainActivity, result.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = android.graphics.Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                    v.clearFocus()
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}