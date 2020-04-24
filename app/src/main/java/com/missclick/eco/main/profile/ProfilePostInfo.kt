package com.missclick.eco.main.profile


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.missclick.eco.R
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile_post_info.*


class ProfilePostInfo : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_post_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val item = arguments?.getParcelable<PositiveItem>("arg")
        infoAction.text = item?.action.toString()
        infoScore.text = item?.score.toString()
        infoTime.text = item?.time.toString().split("'")[1]
        if (item?.share == true) infoShare.text = "You have shared this post with your friends"
        else infoShare.text = "You have not shared this post with your friends"
        var image =  BitmapFactory.decodeFile(context!!.filesDir.path + "/" + item?.imageName)
        image = if (image != null) Bitmap.createScaledBitmap(image, 250, 250, false) else return
        infoImage.setImageBitmap(image)
    }

}
