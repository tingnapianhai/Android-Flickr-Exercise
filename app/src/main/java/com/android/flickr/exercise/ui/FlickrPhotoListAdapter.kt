package com.android.flickr.exercise.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.flickr.exercise.R
import com.android.flickr.exercise.data.FlickrPhotoItem
import com.bumptech.glide.Glide

class FlickrPhotoListAdapter :
    ListAdapter<FlickrPhotoItem, FlickrPhotoListAdapter.PhotoViewHolder>(ListAdapterCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder =
        PhotoViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.photo_item, parent, false)
        )

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var icon: ImageView
        private lateinit var title: TextView
        fun bind(photo: FlickrPhotoItem) {
            icon = itemView.findViewById(R.id.icon)
            title = itemView.findViewById(R.id.title)
            title.text = photo.title.apply {
                if (this.isNullOrBlank()) "No name/description" else this
            }
            Glide.with(itemView.context)
                .load(photo.url)
                .into(icon)
        }
    }

    class ListAdapterCallBack : DiffUtil.ItemCallback<FlickrPhotoItem>() {
        override fun areItemsTheSame(oldItem: FlickrPhotoItem, newItem: FlickrPhotoItem): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(
            oldItem: FlickrPhotoItem,
            newItem: FlickrPhotoItem
        ): Boolean {
            return oldItem == newItem
        }
    }
}
