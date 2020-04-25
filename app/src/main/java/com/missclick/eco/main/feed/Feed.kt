package com.missclick.eco.main.feed


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.transition.MaterialFadeThrough
import com.missclick.eco.R
import com.missclick.eco.main.MainActivity
import com.missclick.eco.main.profile.PositiveItem
import kotlinx.android.synthetic.main.fragment_feed.*


class Feed : androidx.fragment.app.Fragment() {

    private val posts = listOf(
        PositiveItem(-1,"Выкинул бутылку", -10),
        PositiveItem(-2,"Выкинул бумажку", -5)

    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        exitTransition = MaterialFadeThrough.create(requireContext())
        (activity as MainActivity).setSupportActionBar(feed_tool_bar)
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<MaterialButton>(R.id.search_friend_btn).setOnClickListener {
            val transaction = (activity as MainActivity).supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_holder,FeedFind())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        val myAdapter = FeedPostAdapter(
            posts,
            object : FeedPostAdapter.Callback {
                override fun onItemClicked(item: PositiveItem) {

                }
            })

        feedRecycle.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        feedRecycle.adapter = myAdapter
    }

}