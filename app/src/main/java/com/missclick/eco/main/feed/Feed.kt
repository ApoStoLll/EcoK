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
        update()
        feed_refresh.setOnRefreshListener{
            update()
            feed_refresh.isRefreshing = false
        }
        view.findViewById<MaterialButton>(R.id.search_friend_btn).setOnClickListener {
            val transaction = (activity as MainActivity).supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_holder,FeedFind())
            transaction.addToBackStack(null)
            transaction.commit()
        }

    }

    private fun getPosts(adapter : FeedAdapter){
        val activity = activity as MainActivity
        GlobalScope.launch(Dispatchers.Main) {
            val client = HttpClient("95.158.11.238", 8080)
            val user = withContext(Dispatchers.IO){
                client.connect()
                client.getUserData((activity as MainActivity).nickname, (activity as MainActivity))
            }
            val followings = user.followings
            for(following in followings) {
                val client2 = HttpClient("95.158.11.238", 8080)
                val userPosts = withContext(Dispatchers.IO){
                    client2.connect()
                    client2.getProfilePost(following)
                }
                for(post in userPosts){
                    val client3 = HttpClient("95.158.11.238", 8080)
                    val imageProfile = withContext(Dispatchers.IO){
                        client3.connect()
                        client3.getUserData(following,activity).imageName
                    }
                    if(post.share){

                        adapter.addItem(
                            PostItem(
                                following, post.action, post.score, post.description, 0,
                                post.imageName,
                                activity.filesDir.path + "/" + imageProfile
                            )
                        )
                        if (feedLoadingPanel != null) feedLoadingPanel.visibility = View.GONE
                    }
                }
            }

        }

//        posts.sortBy { it.time }
//        return posts
    }

    private fun update(){
        val myAdapter = FeedAdapter(activity as MainActivity, object : FeedAdapter.Callback {
                override fun onItemClicked(item: PostItem) {
                    val profileInfo = ProfilePostInfo()
                    val bundle = Bundle()
                    bundle.putParcelable("arg",item)
                    profileInfo.arguments = bundle
                    val transaction = (activity as MainActivity).supportFragmentManager.beginTransaction()
                    // transaction.addSharedElement(view!!, "info")
                    transaction.replace(R.id.fragment_holder, profileInfo)
                    transaction.addToBackStack(null)
                    transaction.commit()
                }
            })
        getPosts(myAdapter)
        feedRecycle.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        feedRecycle.adapter = myAdapter


    }

}