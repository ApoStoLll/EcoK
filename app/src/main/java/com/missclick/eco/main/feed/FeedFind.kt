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

    private var nicknames:List<String> = listOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed_find, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val found: MutableList<FeedFindItem> = mutableListOf()
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
        if(findRecycle == null) return
        findRecycle.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        findRecycle.adapter = myAdapter


        GlobalScope.launch(Dispatchers.Main) {
            val client = HttpClient("95.158.11.238", 8080)
            nicknames = withContext(Dispatchers.IO) {
                client.connect()
                client.getAllNicknames()
            }
        }

        find_profile_edit_text.setSelection(0)
        find_profile_edit_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if(s.toString() == ""){
                    found.clear()
                    myAdapter.notifyDataSetChanged()
                } else {
                    GlobalScope.launch(Dispatchers.Main) {
                        val currentNicknames = filter(s.toString().toLowerCase())
                        val client = HttpClient("95.158.11.238", 8080)
                        found.clear()
                        for(nick in currentNicknames){
                        val user = withContext(Dispatchers.IO) {
                            client.connect()
                            client.getUserData(nick,activity as MainActivity)
                        }
                        if(found.all { it.username != user.username })
                            found.add(FeedFindItem(user.username,user.name,
                            (activity as MainActivity).filesDir.path + "/" +user.imageName))
                    }
                    myAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int){}
        })



    }

    private fun filter(text : String): MutableList<String>{
        val currentNicknames : MutableList<String> = mutableListOf()
        for (nick in nicknames) {
            if (nick.toLowerCase().contains(text) && nick != (activity as MainActivity).nickname) {
                currentNicknames.add(nick)
            }
        }
        return currentNicknames
    }


}
