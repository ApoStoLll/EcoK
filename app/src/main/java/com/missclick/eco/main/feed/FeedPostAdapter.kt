package com.missclick.eco.main.feed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.missclick.eco.R
import com.missclick.eco.main.profile.PositiveItem

class FeedPostAdapter(var items: List<PositiveItem>, val callback: Callback) : androidx.recyclerview.widget.RecyclerView.Adapter<FeedPostAdapter.MainHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = MainHolder(LayoutInflater.from(parent.context).inflate(R.layout.feed_post, parent, false))
    override fun getItemCount() = items.size
    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        holder.bind(items[position])
    }
    inner class MainHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        private val firstName = itemView.findViewById<TextView>(R.id.actionFeed)
        private val lastName = itemView.findViewById<TextView>(R.id.scoreFeed)
        fun bind(item: PositiveItem) {
            firstName.text = item.action
            lastName.text = item.score.toString()
            itemView.setOnClickListener {
                if (adapterPosition != androidx.recyclerview.widget.RecyclerView.NO_POSITION) callback.onItemClicked(items[adapterPosition])
            }
        }
    }
    interface Callback {
        fun onItemClicked(item: PositiveItem)
    }
}