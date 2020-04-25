package com.missclick.eco.main.feed


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.button.MaterialButton
import com.missclick.eco.HttpClient

import com.missclick.eco.R
import com.missclick.eco.User
import com.missclick.eco.main.MainActivity
import com.missclick.eco.main.otherFrontend.AlienProfile
import kotlinx.android.synthetic.main.fragment_feed_find.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.ConnectException


class FeedFind : Fragment() {

    private val found =  mutableListOf(
        FeedFindItem("kolya288","esggse"),
        FeedFindItem("lol123","segseg"),
        FeedFindItem("lol123","esgs"),
        FeedFindItem("kolya288","segseg"),
        FeedFindItem("lol123","segseg"),
        FeedFindItem("lol123","esgsegs")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_feed_find, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val client = HttpClient("95.158.11.238", 8080)// (activity as MainActivity).client
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                try{
                    client.connect()
                    val user = client.getUserData(find_profile_edit_text.text.toString(),activity as MainActivity)
                    (activity as MainActivity).runOnUiThread{addToRec(user)}
                }catch (e : ConnectException){
                    Log.e("ERROR", e.toString())
                }
            }
        }


        val myAdapter = FeedFindAdapter(
            found,
            object : FeedFindAdapter.Callback {
                override fun onItemClicked(item: FeedFindItem) {
                    val alienProfile = AlienProfile()
                    val bundle = Bundle()
                    bundle.putString("alienNickname",item.username)
                    alienProfile.arguments = bundle
                    val transaction = (activity as MainActivity).supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragment_holder,alienProfile)
                    transaction.addToBackStack(null)
                    transaction.commit()
                }
            })

        findRecycle.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        findRecycle.adapter = myAdapter

    }

    private fun addToRec(user : User){
        found.add(FeedFindItem(user.username,user.name))
    }
}
