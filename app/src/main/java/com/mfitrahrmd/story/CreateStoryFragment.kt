package com.mfitrahrmd.story

import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.mfitrahrmd.story.data.Result
import com.mfitrahrmd.story.data.entity.Story
import com.mfitrahrmd.story.data.util.ImageProvider
import com.mfitrahrmd.story.databinding.FragmentCreateStoryBinding
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

class CreateStoryFragment(
    private val name: String?,
    private val onCreateClicked: suspend (Story, Uri) -> Result<Boolean>
) : DialogFragment() {
    private var storyImageViewId: Int = View.generateViewId()
    private var storyImageUri: Uri? = null

    private lateinit var binding: FragmentCreateStoryBinding
    private val launcherGallery =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                storyImageUri = uri
                appendStoryImageViewIfNotExist(storyImageViewId)
                updateImageSrc(storyImageViewId, uri)
            }
        }
    private val launcherCamera =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                val uri = storyImageUri
                if (uri != null) {
                    appendStoryImageViewIfNotExist(storyImageViewId)
                    updateImageSrc(storyImageViewId, uri)
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateStoryBinding.inflate(inflater, container, false)
        with(binding) {
            userName.text = name
            Glide.with(requireContext())
                .load("https://api.dicebear.com/9.x/thumbs/png?seed=$name&backgroundColor=f88c49&randomizeIds=true&mouth=variant2,variant3,variant4,variant5,variant1&shapeColor=f1f4dc,69d2e7,1c799f,0a5b83")
                .into(userAvatar)
            toolbar.setNavigationOnClickListener {
                dismiss()
            }
            btnGallery.setOnClickListener {
                launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
            btnCamera.setOnClickListener {
                storyImageUri = ImageProvider.createImageFile(requireContext())
                launcherCamera.launch(storyImageUri)
            }
            btnCreate.setOnClickListener {
                if (!storyText.validate()) {
                    storyText.requestFocus()

                    return@setOnClickListener
                }
                val uri = storyImageUri
                if (uri != null) {
                    lifecycleScope.launch {
                        val result = onCreateClicked(
                            Story(
                                id = "",
                                photoUrl = "",
                                createdAt = "",
                                description = storyText.text.toString(),
                                author = "",
                                lat = null,
                                lon = null,
                                _postedAt = null,
                                _locationName = null,
                                _readingTime = null
                            ),
                            uri
                        )
                        when (result) {
                            is Result.Success -> {
                                setFragmentResult(
                                    REQUEST_KEY,
                                    bundleOf(
                                        BUNDLE_KEY_RESULT to CreateResult(
                                            result.message,
                                            false
                                        )
                                    )
                                )
                                dismiss()
                            }

                            is Result.Failed.ApiError -> {
                                lStoryText.error = result.message
                            }

                            is Result.Failed -> {
                                Toast.makeText(
                                    requireContext(),
                                    "${result.message} (code:${result.code})",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                }
            }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.apply {
            window?.setWindowAnimations(R.style.Theme_Story_FullScreenDialog_Animation_Slide)
        }
    }

    override fun getTheme(): Int {
        return R.style.Theme_Story_FullScreenDialog
    }

    private fun appendStoryImageViewIfNotExist(imageViewId: Int) {
        if (binding.layoutStory.findViewById<ImageView>(imageViewId) == null) {
            val storyImageView = ImageView(requireContext()).apply {
                id = imageViewId
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                scaleType = ImageView.ScaleType.FIT_CENTER
            }
            val set = ConstraintSet()
            binding.layoutStory.addView(storyImageView, 0)
            set.clone(binding.layoutStory)
            set.connect(
                storyImageView.id,
                ConstraintSet.TOP,
                binding.lStoryText.id,
                ConstraintSet.BOTTOM
            )
            set.applyTo(binding.layoutStory)
        }
    }

    private fun updateImageSrc(imageViewId: Int, imageUri: Uri) {
        Glide.with(this)
            .load(imageUri)
            .into(binding.root.findViewById(imageViewId))
            .waitForLayout()
    }

    @Parcelize
    data class CreateResult(
        val message: String,
        val isError: Boolean
    ) : Parcelable

    companion object {
        const val REQUEST_KEY = "create_story_request"
        const val BUNDLE_KEY_RESULT = "create_story_result"
    }
}