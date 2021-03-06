package com.missclick.eco.main.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.ViewCompat
import com.missclick.eco.R
import kotlinx.android.synthetic.main.profile_positive_item.view.*

class PositiveAdapter(var items: List<PositiveItem>, val callback: Callback) : androidx.recyclerview.widget.RecyclerView.Adapter<PositiveAdapter.MainHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = MainHolder(LayoutInflater.from(parent.context).inflate(R.layout.profile_positive_item, parent, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        holder.bind(items[position])
    }
    inner class MainHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        private val firstName = itemView.findViewById<TextView>(R.id.firstName1)
        private val lastName = itemView.findViewById<TextView>(R.id.lastName1)
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