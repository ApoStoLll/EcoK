package com.missclick.eco.main


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.missclick.eco.ClientAdapter
import com.missclick.eco.R
import layout.Client
import kotlin.collections.ArrayList


class Feed : androidx.fragment.app.Fragment() {
    val client: ArrayList<Client> = ArrayList()

    lateinit var recView: androidx.recyclerview.widget.RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {

        return inflater.inflate(R.layout.fragment_feed, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addClients()
        // Creates a vertical Layout Manager
        val recView =  view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recycelerView)
        recView.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(this.context, LinearLayout.VERTICAL, false)

        val adapter = ClientAdapter(client)
        recView.adapter = adapter
        // You can use GridLayoutManager if you want multiple columns. Enter the number of columns as a parameter.
//        rv_animal_list.layoutManager = GridLayoutManager(this, 2)

        // Access the RecyclerView Adapter and load the data into it




    }
    fun addClients(){
        client.add(Client("Apostol"))
        client.add(Client("Dania"))
        client.add(Client("Aloxa"))
    }




}