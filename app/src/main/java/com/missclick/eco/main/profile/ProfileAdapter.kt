package com.missclick.eco.main.profile

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.missclick.eco.R


class ProfileAdapter(var items: List<PositiveItem>, val callback: Callback) : RecyclerView.Adapter<ProfileAdapter.MainHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = MainHolder(LayoutInflater.from(parent.context).inflate(R.layout.profile_items, parent, false))
    override fun getItemCount() = items.size
    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        holder.bind(items[position])
    }
    inner class MainHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val firstName = itemView.findViewById<TextView>(R.id.firstName)
        private val lastName = itemView.findViewById<TextView>(R.id.lastName)
        fun bind(item: PositiveItem) {
            firstName.text = item.id.toString()
            lastName.text = item.action
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) callback.onItemClicked(items[adapterPosition])
            }
        }
    }
    interface Callback {
        fun onItemClicked(item: PositiveItem)
    }
}