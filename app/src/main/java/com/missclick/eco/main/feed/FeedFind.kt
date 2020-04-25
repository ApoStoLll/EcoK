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

import com.missclick.eco.User
import com.missclick.eco.main.MainActivity
import com.missclick.eco.main.otherFrontend.AlienProfile
import kotlinx.android.synthetic.main.fragment_feed_find.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.ConnectException
import android.text.Editable
import android.text.TextWatcher
import com.missclick.eco.R


class FeedFind : Fragment() {

    private val found:MutableList<FeedFindItem> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_feed_find, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        find_profile_edit_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
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
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })



    }

    private fun addToRec(user : User){
        found.add(FeedFindItem(user.username,user.name,(activity as MainActivity).filesDir.path + "/" +user.imageName))
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
}
