package com.mfitrahrmd.story.ui.adapter

import android.content.Context
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mfitrahrmd.story.data.entity.Story
import com.mfitrahrmd.story.databinding.ItemStoryBinding
import com.mfitrahrmd.story.util.StoryDiff
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class StoryAdapter(
    private val context: Context,
) : PagingDataAdapter<Story, StoryAdapter.StoryViewHolder>(StoryDiff) {
    private lateinit var locationHandler: LocationHandler

    inner class StoryViewHolder(
        private val viewBinding: ItemStoryBinding
    ) : RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(story: Story?) {
            if (story != null) {
                with(viewBinding) {
                    Glide.with(context)
                        .load("https://api.dicebear.com/9.x/thumbs/png?seed=${story.author}&backgroundColor=f88c49&randomizeIds=true&mouth=variant2,variant3,variant4,variant5,variant1&shapeColor=f1f4dc,69d2e7,1c799f,0a5b83")
                        .into(avatar)
                    authorName.text = story.author
                    if (story.description.isNotEmpty()) {
                        storyText.apply {
                            text = story.description
                            visibility = View.VISIBLE
                        }
                    } else {
                        storyText.visibility = View.GONE
                    }
                    if (!story.readingTime.isNullOrEmpty()) {
                        storyReadingTime.apply {
                            text = story.readingTime
                            visibility = View.VISIBLE
                        }
                    } else {
                        storyReadingTime.visibility = View.GONE
                    }
                    if (!story.postedAt.isNullOrEmpty()) {
                        storyPostedTime.apply {
                            text = story.postedAt
                            visibility = View.VISIBLE
                        }
                    } else {
                        storyPostedTime.visibility = View.GONE
                    }
                    if (!story.locationName.isNullOrEmpty()) {
                        storyLocation.apply {
                            setText(story.locationName)
                            visibility = View.VISIBLE
                        }
                    } else {
                        storyLocation.visibility = View.GONE
                    }
                    Glide.with(context)
                        .load(story.photoUrl)
                        .into(storyImage)
                }
            }
        }

        fun updateLocationName(locationName: String?) {
            with(viewBinding) {
                storyLocation.apply {
                    if (!locationName.isNullOrEmpty()) {
                        setText(locationName)
                        visibility = View.VISIBLE
                    } else {
                        visibility = View.GONE
                    }
                }
            }
        }
    }

    // Asynchronously get location name from latitude and longitude
    internal inner class LocationHandler {
        private val _locationName = MutableStateFlow(mapOf<Int, String>())
        val locationName: StateFlow<Map<Int, String>> = _locationName
        private val geocoder = Geocoder(context, Locale.getDefault())

        fun setLocationNameFromLatLon(story: Story, position: Int) {
            if (story.lat != null && story.lon != null && story.lat.toDouble() != 0.0 && story.lon.toDouble() != 0.0) {
                val currentMap = _locationName.value.toMutableMap()
                CoroutineScope(Dispatchers.IO).launch {
                    geocoder.getFromLocation(story.lat.toDouble(), story.lon.toDouble(), 1)
                        ?.let {
                            if (it.isEmpty()) return@let
                            val address = it[0]
                            val locality = address.locality
                            val subAdminArea = address.subAdminArea
                            currentMap[position] = "$locality, $subAdminArea"
                            withContext(Dispatchers.Main) {
                                _locationName.value = currentMap
                            }
                        }
                }
            }
        }
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = getItem(position)
        story?.let {
            story.setPostedAtFromCreatedAt(context)
            story.setReadingTimeFromDescription(context)
            holder.bind(story)
            locationHandler.setLocationNameFromLatLon(story, position)
        }
        CoroutineScope(Dispatchers.Main).launch {
            locationHandler.locationName.collect { locationMap ->
                locationMap[position]?.let {
                    story?.setLocationName(it)
                    holder.updateLocationName(story?.locationName)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        locationHandler = LocationHandler()
        return StoryViewHolder(
            ItemStoryBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ),
                parent,
                false
            )
        )
    }
}