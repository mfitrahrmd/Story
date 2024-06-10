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
                    tvName.text = story.author
                    if (!story.readingTime.isNullOrEmpty()) {
                        tvLength.apply {
                            text = story.readingTime
                            visibility = View.VISIBLE
                        }
                    } else {
                        tvLength.visibility = View.GONE
                    }
                    if (!story.postedAt.isNullOrEmpty()) {
                        tvTime.apply {
                            text = story.postedAt
                            visibility = View.VISIBLE
                        }
                    } else {
                        tvTime.visibility = View.GONE
                    }
                    if (!story.locationName.isNullOrEmpty()) {
                        tvLocation.apply {
                            text = story.locationName
                            visibility = View.VISIBLE
                        }
                    } else {
                        tvLocation.visibility = View.GONE
                    }
                    Glide.with(context)
                        .load(story.photoUrl)
                        .into(ivPhoto)
                }
            }
        }

        fun updateLocationName(locationName: String?) {
            with(viewBinding) {
                tvLocation.apply {
                    if (!locationName.isNullOrEmpty()) {
                        text = locationName
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