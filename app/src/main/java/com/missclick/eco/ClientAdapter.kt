package com.missclick.eco

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.VideoView
import kotlinx.android.synthetic.main.fragment_feed.view.*
import layout.Client

class ClientAdapter(val userList: ArrayList<Client>) : RecyclerView.Adapter<ClientAdapter.ViewHolder>() {
    override fun onBindViewHolder(p0: ClientAdapter.ViewHolder, p1: Int) {
       val client: Client = userList[p1]
        p0.textViewName.text = client.name

    }
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ClientAdapter.ViewHolder {
       val v = LayoutInflater.from(p0.context).inflate(R.layout.feeditems,p0,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return userList.size
    }



    //this method is returning the view for each item in the list
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textViewName = itemView.findViewById(R.id.nickText) as TextView

    }
}


