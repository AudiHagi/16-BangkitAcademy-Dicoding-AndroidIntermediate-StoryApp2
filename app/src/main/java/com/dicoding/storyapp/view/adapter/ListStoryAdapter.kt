package com.dicoding.storyapp.view.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.storyapp.data.remote.response.Story
import com.dicoding.storyapp.databinding.ItemStoryBinding
import com.dicoding.storyapp.utils.DateFormatter
import com.dicoding.storyapp.view.detailstory.DetailStoryActivity
import java.util.TimeZone

class ListStoryAdapter :
    PagingDataAdapter<Story, ListStoryAdapter.ListViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        return ListViewHolder(
            ItemStoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(story = data)
        }
    }

    class ListViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story) {
            with(binding) {
                Glide.with(itemView)
                    .load(story.photoUrl)
                    .into(storyImages)
                storyName.text = story.name
                storyDate.text = DateFormatter.formatDate(story.createdAt, TimeZone.getDefault().id)
                storyDesc.text = story.description
                val latitude = story.lat.toString()
                val longitude = story.lon.toString()
                if (story.lat != 0.0 && story.lon != 0.0) {
                    mapIcon.visibility = View.VISIBLE
                } else {
                    mapIcon.visibility = View.GONE
                }
                layoutStory.setOnClickListener {
                    val intent = Intent(itemView.context, DetailStoryActivity::class.java).apply {
                        putExtra(DetailStoryActivity.USERNAME, story.name)
                        putExtra(DetailStoryActivity.DESCRIPTION, story.description)
                        putExtra(DetailStoryActivity.PHOTO, story.photoUrl)
                        putExtra(DetailStoryActivity.DATE, story.createdAt)
                        putExtra(DetailStoryActivity.LAT, latitude)
                        putExtra(DetailStoryActivity.LON, longitude)
                    }
                    val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(storyImages, "images"),
                        Pair(storyName, "name"),
                        Pair(storyDate, "date"),
                        Pair(storyDesc, "desc")
                    )
                    itemView.context.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
