package com.missclick.eco.main.feed

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.missclick.eco.R

class FeedAdapter(var items: List<PostItem>, val callback: Callback) : androidx.recyclerview.widget.RecyclerView.Adapter<FeedAdapter.MainHolder>() {
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
        fun bind(item: PostItem) {
            username.text = item.username
            score.text = item.score.toString()
            Log.e("des",item.imageName)
            val desc = if(item.description != "NULL") " and says " + item.description else ""
            description.text = item.action + desc
            var imageBit =  BitmapFactory.decodeFile(item.imageName)
            if(imageBit != null){
                imageBit = if (imageBit.width != null) Bitmap.createScaledBitmap(imageBit, 150, 150, false) else return
                image.setImageBitmap(imageBit)
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