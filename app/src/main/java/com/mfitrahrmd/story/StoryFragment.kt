package com.mfitrahrmd.story

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.mfitrahrmd.story.data.entity.Story
import com.mfitrahrmd.story.databinding.FragmentStoryBinding
import com.mfitrahrmd.story.ui.adapter.StoryAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.launch

class StoryFragment : Fragment(), StoryAdapter.OnItemClickListener {
    private lateinit var binding: FragmentStoryBinding
    private val activityViewModel: StoryActivityViewModel by activityViewModels {
        AppViewModelProvider.Factory
    }
    private lateinit var storyAdapter: StoryAdapter
    private lateinit var storyLayoutManager: LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storyAdapter = StoryAdapter(requireContext(), this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentStoryBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        storyLayoutManager = LinearLayoutManager(requireContext())
        bind()
        observe()
    }

    private fun bind() {
        with(binding) {
            listStory.apply {
                layoutManager = storyLayoutManager
                adapter = storyAdapter
                addItemDecoration(DividerItemDecoration(
                    requireContext(), LinearLayoutManager.VERTICAL
                ).apply {
                    setDrawable(
                        ContextCompat.getDrawable(
                            requireContext(), R.drawable.empty_divier
                        )!!
                    )
                })
            }
            swipeToRefresh.setOnRefreshListener {
                activityViewModel.refresh()
            }
            requireActivity().supportFragmentManager.setFragmentResultListener(
                CreateStoryFragment.REQUEST_KEY, this@StoryFragment
            ) { requestKey, bundle ->
                val message = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    bundle.getParcelable(
                        CreateStoryFragment.BUNDLE_KEY_RESULT,
                        CreateStoryFragment.CreateResult::class.java
                    )
                } else {
                    bundle.getParcelable<CreateStoryFragment.CreateResult>(CreateStoryFragment.BUNDLE_KEY_RESULT)
                }
                message?.apply {
                    Toast.makeText(requireContext(), message.message, Toast.LENGTH_SHORT).show()
                    if (!isError) {
                        activityViewModel.refresh()
                        binding.listStory.scrollToPosition(0)
                    }
                }
            }
        }
    }

    private fun observe() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                activityViewModel.storyPagingDataFlow.collect { storyPaging ->
                    storyAdapter.submitData(lifecycle, storyPaging)
                }
            }
        }
        lifecycleScope.launch {
            storyAdapter.loadStateFlow.distinctUntilChangedBy {
                it.refresh
            }.collectLatest {
                if (it.refresh is LoadState.NotLoading && it.prepend.endOfPaginationReached) {
                    binding.listStory.scrollToPosition(0)
                }
                binding.swipeToRefresh.isRefreshing = it.refresh is LoadState.Loading
            }
        }
    }

    override fun onItemClick(story: Story) {
        findNavController().navigate(StoryFragmentDirections.actionHomeToDetailStory(story.id))
    }
}