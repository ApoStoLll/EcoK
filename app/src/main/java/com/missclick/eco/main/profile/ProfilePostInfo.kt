package com.missclick.eco.main.profile


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import com.google.android.material.transition.SlideDistance
//import com.google.android.material.transition.MaterialContainerTransform
import com.missclick.eco.HttpClient
import com.missclick.eco.R
import com.missclick.eco.main.MainActivity
import com.missclick.eco.main.feed.PostItem
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile_post_info.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.ConnectException
import java.util.concurrent.TimeUnit


class ProfilePostInfo : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        exitTransition = MaterialFadeThrough.create(requireContext())
        return inflater.inflate(R.layout.fragment_profile_post_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val item = arguments?.getParcelable<PostItem>("arg")

        val description = if(item?.description != "NULL") " and says " + item?.description else ""
        info_post_description.text = item?.action + description
        info_post_score.text = item?.score.toString()
        info_post_name.text = item?.username

        GlobalScope.launch(Dispatchers.Main) {
            val client = HttpClient("95.158.11.238", 8080)
            val imageNameProfile = withContext(Dispatchers.IO) {
                try{
                    client.connect()
                    client.getImage(item!!.imageProfileName, activity as MainActivity)
                } catch (e : ConnectException){
                    "NULL"
                }

            }
            val imageName = withContext(Dispatchers.IO) {
                try {
                    client.connect()
                    client.getImage(item!!.imageName, activity as MainActivity)
                }catch (e : ConnectException){
                    "NULL"
                }
            }
            var imageBitPost =  BitmapFactory.decodeFile(context?.filesDir?.path + "/" +imageName)
            if(imageBitPost != null){
                imageBitPost = Bitmap.createScaledBitmap(imageBitPost, 150, 150, false)
                info_post_image.setImageBitmap(imageBitPost)
            }
            var imageBitProfile =  BitmapFactory.decodeFile(context?.filesDir?.path + "/" +imageNameProfile)
            if(imageBitProfile != null){
                imageBitProfile = Bitmap.createScaledBitmap(imageBitProfile, 150, 150, false)
                info_post_image_profile.setImageBitmap(imageBitProfile)
            }
            loadingPanelInfoPost.visibility = View.GONE
        }
    }
}



