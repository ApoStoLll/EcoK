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

import com.missclick.eco.R
import com.missclick.eco.main.MainActivity
import com.missclick.eco.main.otherFrontend.AlienProfile
import kotlinx.android.synthetic.main.fragment_feed_find.*


class FeedFind : Fragment() {

    private val found = listOf(
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
