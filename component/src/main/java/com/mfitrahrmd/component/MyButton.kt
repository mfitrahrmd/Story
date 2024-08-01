package com.mfitrahrmd.component

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.mfitrahrmd.component.databinding.MyButtonBinding

class MyButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RelativeLayout(context, attrs) {
    private val viewBinding: MyButtonBinding

    init {
        viewBinding = MyButtonBinding.inflate(LayoutInflater.from(context), this, true)
        val arr = context.obtainStyledAttributes(
            attrs,
            R.styleable.MyButton,
            0,
            0
        )
        val text = arr.getString(R.styleable.MyButton_text)
        val loading = arr.getBoolean(R.styleable.MyButton_loading, false)
        val enabled = arr.getBoolean(R.styleable.MyButton_enabled, true)
        val lottieResId = arr.getResourceId(R.styleable.MyButton_lottieResId, R.raw.spinner_white)
        arr.recycle()
        this.isEnabled = enabled
        with(viewBinding) {
            textView.text = text
            textView.isEnabled = enabled
            animationView.setAnimation(lottieResId)
        }
        setText(text)
        setLoading(loading)
    }

    fun setText(text: String?) {
        viewBinding.textView.text = text
    }

    fun setLoading(loading: Boolean) {
        isClickable = !loading
        if (loading) {
            with(viewBinding) {
                textView.visibility = GONE
                animationView.visibility = VISIBLE
            }
        } else {
            with(viewBinding) {
                textView.visibility = VISIBLE
                animationView.visibility = GONE
            }
        }
    }
}