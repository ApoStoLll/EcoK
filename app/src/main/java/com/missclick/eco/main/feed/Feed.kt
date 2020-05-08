package com.missclick.eco.main.feed


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.transition.MaterialFadeThrough
import com.missclick.eco.HttpClient
import com.missclick.eco.R
import com.missclick.eco.main.MainActivity
import com.missclick.eco.main.profile.PositiveItem
import com.missclick.eco.main.profile.ProfilePostInfo
import kotlinx.android.synthetic.main.fragment_feed.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.ConnectException
import java.util.concurrent.TimeUnit


class Feed : androidx.fragment.app.Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        exitTransition = MaterialFadeThrough.create(requireContext())
        (activity as MainActivity).setSupportActionBar(feed_tool_bar)
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var count = 1
        val actions : MutableList<PostItem> = mutableListOf()
        val myAdapter = FeedAdapter(activity as MainActivity, object : FeedAdapter.Callback {
            override fun onItemClicked(item: PostItem) {
                val profileInfo = ProfilePostInfo()
                val bundle = Bundle()
                bundle.putParcelable("arg",item)
                profileInfo.arguments = bundle
                val transaction = (activity as MainActivity).supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_holder, profileInfo)
                transaction.addToBackStack(null)
                transaction.commit()
            }
        })
        feedRecycle.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        feedRecycle.adapter = myAdapter
        getPosts(actions, count, myAdapter)
        count++
        feed_refresh.setOnRefreshListener{
            count = 1
            //actions.clear()
            getPosts(actions, count, myAdapter)
            feed_refresh.isRefreshing = false
        }
        view.findViewById<MaterialButton>(R.id.search_friend_btn).setOnClickListener {
            val transaction = (activity as MainActivity).supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_holder,FeedFind())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        feedRecycle.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    getPosts(actions, count, myAdapter)
                    myAdapter.notifyDataSetChanged()
                    count++
                }
            }
        })
    }

//    try{
//
//    } catch (e : ConnectException){
//        Log.e("ERROR", e.toString())
//    }

    private fun getPosts(actions: MutableList<PostItem>, count: Int, myAdapter : FeedAdapter){ // Костыль с созданием пустого списка
        val activity = activity as MainActivity
        GlobalScope.launch(Dispatchers.Main) {
            val client = HttpClient("95.158.11.238", 8080)
            val posts = withContext(Dispatchers.IO) {
                try{
                    client.connect()
                    client.getFeedPosts(activity.nickname, count)
                } catch (e : ConnectException){
                    mutableListOf<PostItem>()
                }
            }
            for(post in posts){
                val imageName = withContext(Dispatchers.IO) {
                    client.connect()
                    client.getImage(post.imageName, activity)
                }
                val imageNameProfile = withContext(Dispatchers.IO) {
                    client.connect()
                    client.getImage(post.imageProfileName, activity)
                }
                post.imageName = imageName
                post.imageProfileName = imageNameProfile
            }
            if(count == 1) myAdapter.setItems(posts)
            for(post in posts){
                myAdapter.addItem(post, posts.size - 1)
            }
            //actions.addAll(posts)
            if (feedLoadingPanel != null) feedLoadingPanel.visibility = View.GONE
        }
    }

}