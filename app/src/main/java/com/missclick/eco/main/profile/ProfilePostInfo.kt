package com.missclick.eco.main.profile


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.transition.MaterialContainerTransform
import com.missclick.eco.FtpManager
import com.missclick.eco.HttpClient

import com.missclick.eco.R
import com.missclick.eco.main.MainActivity
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile_post_info.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.ConnectException


class ProfilePostInfo : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        sharedElementEnterTransition = MaterialContainerTransform(requireContext())
        return inflater.inflate(R.layout.fragment_profile_post_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val item = arguments?.getParcelable<PositiveItem>("arg")
        infoAction.text = item?.action
        infoScore.text = item?.score.toString()
        infoTime.text = item?.time
        infoDescription.text = item?.description
        Log.e(item?.share.toString(),item?.imageName)
        if (item?.share == true) infoShare.text = "You have shared this post with your friends"
        else infoShare.text = "You have not shared this post with your friends"

        val client = HttpClient("95.158.11.238", 8080)// (activity as MainActivity).client
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    client.connect()
                    client.getImage(item!!.imageName, activity as MainActivity)
                } catch (e: ConnectException) {
                    Log.e("ERROR", e.toString())
                }
            }
        }
        var image =  BitmapFactory.decodeFile(context!!.filesDir.path + "/" + item?.imageName)
        image = if (image != null) Bitmap.createScaledBitmap(image, 250, 250, false) else return
        infoImage.setImageBitmap(image)
    }

}
