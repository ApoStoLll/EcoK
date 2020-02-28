package com.missclick.eco


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_feed.*
import layout.Client
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList


class Feed : Fragment() {
    val client: ArrayList<Client> = ArrayList()

    lateinit var recView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {

        return inflater.inflate(R.layout.fragment_feed, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addClients()
        // Creates a vertical Layout Manager
        val recView =  view.findViewById<RecyclerView>(R.id.recycelerView)
        recView.layoutManager = LinearLayoutManager(this.context,LinearLayout.VERTICAL,false)

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