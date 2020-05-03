package com.missclick.eco.main.feed

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.missclick.eco.HttpClient
import com.missclick.eco.R
import com.missclick.eco.main.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FeedAdapter(val items: MutableList<PostItem>,val context : Context,val callback: Callback) : androidx.recyclerview.widget.RecyclerView.Adapter<FeedAdapter.MainHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = MainHolder(LayoutInflater.from(parent.context).inflate(R.layout.feed_post, parent, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        holder.bind(items[position])
    }
    inner class MainHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        private val username = itemView.findViewById<TextView>(R.id.feed_post_name)
        private val score = itemView.findViewById<TextView>(R.id.feed_post_score)
        private val description = itemView.findViewById<TextView>(R.id.feed_post_description)
        private val like = itemView.findViewById<TextView>(R.id.feed_post_like)
        private val image = itemView.findViewById<ImageView>(R.id.feed_post_image)
        private val imageProfile = itemView.findViewById<ImageView>(R.id.feed_post_image_profile)
        private val loading = itemView.findViewById<ProgressBar>(R.id.loadingPanelFeedPost)
        fun bind(item: PostItem) {
            username.text = item.username
            score.text = item.score.toString()
            val desc = if(item.description != "NULL") " and says " + item.description else ""
            description.text = item.action + desc

            var imageBitProfile =  BitmapFactory.decodeFile(context.filesDir.path + "/" +item.imageProfileName)
            if(imageBitProfile != null){
                imageBitProfile = Bitmap.createScaledBitmap(imageBitProfile, 150, 150, false)
                imageProfile.setImageBitmap(imageBitProfile)
            }

            var imageBit =  BitmapFactory.decodeFile(context.filesDir.path + "/" +item.imageName)
            if(imageBit != null){
                imageBit = Bitmap.createScaledBitmap(imageBit, 150, 150, false)
                image.setImageBitmap(imageBit)
                loading.visibility = View.GONE
            }

            itemView.setOnClickListener {
                if (adapterPosition != androidx.recyclerview.widget.RecyclerView.NO_POSITION) callback.onItemClicked(items[adapterPosition])
            }
        }
    }
    interface Callback {
        fun onItemClicked(item: PostItem)
    }
}