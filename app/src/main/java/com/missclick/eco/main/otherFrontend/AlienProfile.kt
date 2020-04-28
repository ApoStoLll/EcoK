package com.missclick.eco.main.otherFrontend


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.missclick.eco.HttpClient

import com.missclick.eco.R
import com.missclick.eco.User
import com.missclick.eco.main.MainActivity
import com.missclick.eco.main.profile.PositiveAdapter
import com.missclick.eco.main.profile.PositiveItem
import com.missclick.eco.main.profile.ProfilePostInfo
import kotlinx.android.synthetic.main.fragment_alien_profile.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.ConnectException


class AlienProfile : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alien_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val alienUsername = arguments?.getString("alienNickname")
        alien_name_profile.text = alienUsername
        update(alienUsername)
        alien_refresh.setOnRefreshListener{
            update(alienUsername)
            alien_refresh.isRefreshing = false
        }
        view.findViewById<Button>(R.id.alien_btnFollow).setOnClickListener {
            GlobalScope.launch {
                val client = HttpClient("95.158.11.238", 8080)//(activity as MainActivity).client
                withContext(Dispatchers.IO) {
                    try{
                        client.connect()
                        client.follow((activity as MainActivity).nickname,alienUsername!!)
                        (activity as MainActivity).runOnUiThread { update(alienUsername) }
                    }catch (e : ConnectException){
                        Log.e("ERROR", e.toString())
                    }
                }
            }
        }
    }

    private fun update(alienUsername : String?){
        GlobalScope.launch {
            val client = HttpClient("95.158.11.238", 8080)//(activity as MainActivity).client
            withContext(Dispatchers.IO) {
                try{
                    client.connect()
                    val user = client.getUserData(alienUsername!!, (activity as MainActivity))
                    client.connect()
                    val actions = client.getProfilePost(alienUsername!!)
                    client.connect()
                    val myUser = client.getUserData((activity as MainActivity).nickname,(activity as MainActivity))
                    (activity as MainActivity).runOnUiThread { upd(user, actions,myUser) }
                }catch (e : ConnectException){
                    Log.e("ERROR", e.toString())
                }
            }
        }
    }

    private fun upd(user : User, actions:List<PositiveItem>, myUser: User){
        if(alien_name_profile == null) return
        val actionNew : MutableList<PositiveItem> = mutableListOf()
        if(myUser.followers != null) for(follower in myUser.followers) if (follower == user.username)
            alien_btnFollow.text = "unfollow"
        for(item in actions) if (item.share) actionNew.add(item)
        val myAdapter = PositiveAdapter(
            actionNew,
            object : PositiveAdapter.Callback {
                override fun onItemClicked(item: PositiveItem) {
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
        alien_recV.setHasFixedSize(true)
        alien_name_profile.text = user.name
        Log.e("Score",user.score)
        var image =  BitmapFactory.decodeFile(context!!.filesDir.path + "/" + user.imageName)
        image = if (image != null) Bitmap.createScaledBitmap(image, 250, 250, false) else return
        alien_image_profile.setImageBitmap(image)
        alien_score_profile.text = user.score
        alien_profile_name_toolbar.text = user.username
        alien_recV.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        alien_recV.adapter = myAdapter
    }
}