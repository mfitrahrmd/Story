package com.mfitrahrmd.story

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.mfitrahrmd.story.data.Result
import com.mfitrahrmd.story.databinding.FragmentDetailStoryBinding
import kotlinx.coroutines.launch

class DetailStoryFragment : Fragment() {
    private lateinit var binding: FragmentDetailStoryBinding
    private val viewModel: DetailStoryViewModel by lazy {
        ViewModelProvider(this, AppViewModelProvider.Factory)[DetailStoryViewModel::class.java]
    }
    private val args: DetailStoryFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailStoryBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
        viewModel.refresh(args.id)
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.detailStoryFlow.collect { uiState ->
                    when(uiState) {
                        is UiState.Finish -> {
                            when(uiState.result) {
                                is Result.Success.WithData -> {
                                    with(binding) {
                                        val data = uiState.result.data
                                        Glide.with(requireContext())
                                            .load(data.photoUrl)
                                            .into(storyImage)
                                        storyText.text = data.description
                                    }
                                }
                                else -> {}
                            }
                        }
                        else -> {}
                    }
                }
            }
        }
    }
}