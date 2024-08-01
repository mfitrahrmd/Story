package com.mfitrahrmd.story

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfitrahrmd.story.data.Result
import com.mfitrahrmd.story.databinding.FragmentStoryBinding
import com.mfitrahrmd.story.ui.adapter.StoryAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.launch

class StoryFragment : Fragment() {
    private lateinit var binding: FragmentStoryBinding
    private val viewModel: StoryViewModel by lazy {
        ViewModelProvider(this, AppViewModelProvider.Factory)[StoryViewModel::class.java]
    }
    private lateinit var storyAdapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().supportFragmentManager.setFragmentResultListener(CreateStoryFragment.REQUEST_KEY, this) { requestKey, bundle ->
            val message = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable(CreateStoryFragment.BUNDLE_KEY_RESULT, CreateStoryFragment.CreateResult::class.java)
            } else {
                bundle.getParcelable<CreateStoryFragment.CreateResult>(CreateStoryFragment.BUNDLE_KEY_RESULT)
            }
            Toast.makeText(this.requireContext(), message?.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStoryBinding.inflate(layoutInflater, container, false)
        storyAdapter = StoryAdapter(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            swipeToRefresh.setOnRefreshListener {
                viewModel.refresh()
                with(binding) {
                    listStory.scrollToPosition(0)
                }
            }
            listStory.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = storyAdapter
                addItemDecoration(
                    DividerItemDecoration(
                        requireContext(),
                        LinearLayoutManager.VERTICAL
                    ).apply {
                        setDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.empty_divier
                            )!!
                        )
                    })
            }
            lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    viewModel.storyPagingDataFlow.collect {
                        storyAdapter.submitData(lifecycle, it)
                    }
                }
            }
            lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    storyAdapter.loadStateFlow
                        .distinctUntilChangedBy {
                            it.refresh
                        }
                        .collectLatest {
                            swipeToRefresh.isRefreshing = it.refresh is LoadState.Loading
                        }
                }
            }
            viewModel.refresh()
        }
    }
}