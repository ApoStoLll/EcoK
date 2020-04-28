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
        val item = arguments?.getParcelable<PositiveItem>("arg")

        infoAction.text = item?.action
        infoScore.text = item?.score.toString()
        infoTime.text = item?.time

        if(item?.description != "NULL") infoDescription.text = item?.description
        else infoDescription.text = ""
        if (item?.share == true) infoShare.text = "You have shared this post with your friends"
        else infoShare.text = "You have not shared this post with your friends"

        if(item?.imageName != "NULL"){
            val client = HttpClient("95.158.11.238", 8080)
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
            val arr = item?.imageName!!.split("/")
            val imageName = arr[arr.size - 1]
            setImage(imageName)

        }
    }

    private fun setImage(imageName: String){
        Log.e("info",imageName)

        //TimeUnit.MILLISECONDS.sleep(500L)
        Log.e("info2",imageName)
        var image =  BitmapFactory.decodeFile(context!!.filesDir.path + "/" + imageName)
        if (image == null) Log.e("info3","null")
        image = if (image != null) Bitmap.createScaledBitmap(image, 250, 250, false) else return
        Log.e("info4",imageName)
        infoImage.setImageBitmap(image)
    }

}
