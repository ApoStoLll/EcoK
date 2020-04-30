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
import java.net.ConnectException

class FeedFindAdapter(var items: List<FeedFindItem>, val callback: Callback) : androidx.recyclerview.widget.RecyclerView.Adapter<FeedFindAdapter.MainHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = MainHolder(LayoutInflater.from(parent.context).inflate(R.layout.feed_find_item, parent, false))
    override fun getItemCount() = items.size
    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        holder.bind(items[position])
    }
    inner class MainHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        private val findName1 = itemView.findViewById<TextView>(R.id.findName)
        private val findUsername1 = itemView.findViewById<TextView>(R.id.findUsername)
        private val image = itemView.findViewById<ImageView>(R.id.image1234)
        fun bind(item: FeedFindItem) {
            findName1.text = item.name
            findUsername1.text = item.username
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
        fun onItemClicked(item: FeedFindItem)
    }
}