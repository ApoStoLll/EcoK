package com.missclick.eco.main.feed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.missclick.eco.R

class FeedFindAdapter(var items: List<FeedFindItem>, val callback: Callback) : androidx.recyclerview.widget.RecyclerView.Adapter<FeedFindAdapter.MainHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = MainHolder(LayoutInflater.from(parent.context).inflate(R.layout.feed_find_item, parent, false))
    override fun getItemCount() = items.size
    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        holder.bind(items[position])
    }
    inner class MainHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        private val findName = itemView.findViewById<TextView>(R.id.findName)
        private val findUsername = itemView.findViewById<TextView>(R.id.findUsername)
        fun bind(item: FeedFindItem) {
            findName.text = item.name
            findUsername.text = item.username
            itemView.setOnClickListener {
                if (adapterPosition != androidx.recyclerview.widget.RecyclerView.NO_POSITION) callback.onItemClicked(items[adapterPosition])
            }
        }
    }
    interface Callback {
        fun onItemClicked(item: FeedFindItem)
    }
}