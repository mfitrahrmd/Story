package com.mfitrahrmd.story

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.mfitrahrmd.story.databinding.FragmentProfileBinding
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val activityViewModel: StoryActivityViewModel by activityViewModels {
        AppViewModelProvider.Factory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            lifecycleScope.launch {
                activityViewModel.session.getName().collect { name ->
                    userName.text = name
                    Glide.with(requireContext())
                        .load("https://api.dicebear.com/9.x/thumbs/png?seed=$name&backgroundColor=f88c49&randomizeIds=true&mouth=variant2,variant3,variant4,variant5,variant1&shapeColor=f1f4dc,69d2e7,1c799f,0a5b83")
                        .into(userAvatar)
                }
            }
            lifecycleScope.launch {
                activityViewModel.session.getEmail().collect { email ->
                    userEmail.text = email
                }
            }
            btnLogout.setOnClickListener {
                lifecycleScope.launch {
                    activityViewModel.session.setToken {
                        ""
                    }
                }
            }
        }
    }
}