package com.mfitrahrmd.component

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.mfitrahrmd.component.databinding.MyTipTextViewBinding

class MyTipTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RelativeLayout(context, attrs) {
    private val viewBinding: MyTipTextViewBinding

    private var defaultParentLayoutHeight: Int = 0
    private var defaultTextGravity: Int = 0
    private var defaultTextEllipsize: TextUtils.TruncateAt? = null

    init {
        viewBinding = MyTipTextViewBinding.inflate(LayoutInflater.from(context), this, true)
        val arr = context.obtainStyledAttributes(
            attrs,
            R.styleable.MyTipTextView,
            0,
            0
        )
        val background = arr.getResourceId(R.styleable.MyTipTextView_my_background, R.drawable.tip_background)
        val src = arr.getResourceId(R.styleable.MyTipTextView_my_src, R.drawable.exclamation)
        val tint = arr.getColor(R.styleable.MyTipTextView_my_tint, 0)
        val text = arr.getText(R.styleable.MyTipTextView_my_text)
        arr.recycle()
        with(viewBinding) {
            root.setBackgroundResource(background)
            icon.setImageResource(src)
            if (tint != 0) {
                icon.setColorFilter(tint)
            }
            this.text.text = text
            this.root.setOnClickListener {
                toggleTextVisibility()
            }
        }
    }

    fun setText(text: CharSequence?) {
        viewBinding.text.text = text
    }

    private fun toggleTextVisibility() {
        with(viewBinding) {
            if (text.visibility == View.GONE) {
                showText()
            } else {
                hideText()
            }
        }
    }

    private fun hideText() {
        with(viewBinding) {
            // Backup default properties
            defaultParentLayoutHeight = root.layoutParams.height
            defaultTextGravity = text.gravity
            defaultTextEllipsize = text.ellipsize

            // Make the root' height fixed
            root.layoutParams.height = root.height

            text.maxLines = 1
            text.gravity = Gravity.START
            text.ellipsize =
                TextUtils.TruncateAt.MARQUEE // TODO : change ellipsize programmatically. <- this doesn't work

            // Measure the size change
            text.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)

            // Create width animation
            val initialWidth = root.width
            val targetWidth =
                icon.measuredWidth + root.paddingStart + root.paddingEnd
            val widthAnimator = ValueAnimator.ofInt(initialWidth, targetWidth)
            widthAnimator.addUpdateListener { animation ->
                val layoutParams = root.layoutParams
                layoutParams.width = animation.animatedValue as Int
                root.layoutParams = layoutParams
            }
            widthAnimator.duration = 250
            // Create fade animation
            val initialAlpha = root.alpha
            val targetAlpha = 0.25f
            val fadeAnimator = ValueAnimator.ofFloat(initialAlpha, targetAlpha)
            fadeAnimator.addUpdateListener { animation ->
                root.alpha = animation.animatedValue as Float
            }
            fadeAnimator.duration = 50
            val animatorSet = AnimatorSet().apply {
                playSequentially(widthAnimator, fadeAnimator)
            }
            animatorSet.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    super.onAnimationStart(animation)
                    root.isClickable = false
                }

                override fun onAnimationEnd(animation: Animator) {
                    text.visibility = View.GONE

                    // Restore default properties
                    root.layoutParams.height = defaultParentLayoutHeight
                    text.gravity = defaultTextGravity
                    text.ellipsize = defaultTextEllipsize

                    root.isClickable = true
                }
            })
            animatorSet.start()
        }
    }

    private fun showText() {
        with(viewBinding) {
            text.visibility = View.VISIBLE

            // Backup default properties
            defaultParentLayoutHeight = root.layoutParams.height
            defaultTextGravity = text.gravity
            defaultTextEllipsize = text.ellipsize

            // Make the root' height fixed
            root.layoutParams.height = root.height

            text.maxLines = 1
            text.gravity = Gravity.START
            text.ellipsize =
                TextUtils.TruncateAt.MARQUEE // TODO : change ellipsize programmatically. <- this doesn't work

            // Measure the size change
            text.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)

            // Create width animation
            val targetWidth =
                text.measuredWidth + icon.measuredWidth + root.paddingStart + root.paddingEnd + (root.layoutParams as LayoutParams).marginStart
            val initialWidth = root.width
            val widthAnimator = ValueAnimator.ofInt(initialWidth, targetWidth)
            widthAnimator.addUpdateListener { animation ->
                val layoutParams = root.layoutParams
                layoutParams.width = animation.animatedValue as Int
                root.layoutParams = layoutParams
            }
            widthAnimator.duration = 300
            // Create fade animation
            val initialAlpha = root.alpha
            val targetAlpha = 1f
            val fadeAnimator = ValueAnimator.ofFloat(initialAlpha, targetAlpha)
            fadeAnimator.addUpdateListener { animation ->
                root.alpha = animation.animatedValue as Float
            }
            fadeAnimator.duration = 50
            val animatorSet = AnimatorSet().apply {
                playSequentially(widthAnimator, fadeAnimator)
            }
            animatorSet.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    super.onAnimationStart(animation)
                    root.isClickable = false
                }

                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    // Restore default properties
                    root.layoutParams.height = defaultParentLayoutHeight

                    text.gravity = defaultTextGravity
                    text.ellipsize = defaultTextEllipsize

                    root.isClickable = true
                }
            })
            animatorSet.start()
        }
    }
}