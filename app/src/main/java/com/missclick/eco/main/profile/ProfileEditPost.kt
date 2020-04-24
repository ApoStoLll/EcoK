package com.missclick.eco.main.profile


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.missclick.eco.HttpClient

import com.missclick.eco.R
import com.missclick.eco.main.MainActivity
import kotlinx.android.synthetic.main.fragment_profile_edit_post.*
import kotlinx.android.synthetic.main.fragment_profile_positive.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.ConnectException


class ProfileEditPost : Fragment() {

    private var imageName = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile_edit_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val item = arguments!!.getParcelable<PositiveItem>("arg")
        view.findViewById<Button>(R.id.btnAddPostPhoto ).setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_GET_CONTENT)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, 1)
        }
        view.findViewById<Button>(R.id.btnPost ).setOnClickListener {
            item.description = description.text.toString()
            item.share = checkBoxShare.isChecked
            item.imageName = imageName
            requestToServer(item)
            (activity as MainActivity).startMenu(3)
        }

    }

    private fun requestToServer(item : PositiveItem){
        GlobalScope.launch {
            val client = HttpClient("95.158.11.238", 8080)
            withContext(Dispatchers.IO) {
                try{
                    client.connect()
                    client.addProfilePost(item,(activity as MainActivity).nickname)
                }catch (e : ConnectException){
                    Log.e("ERROR", e.toString())
                }

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val chosenImageUri = data!!.data
            val bitmap = MediaStore.Images.Media.getBitmap((activity as MainActivity).getContentResolver(), chosenImageUri)
            val arr = chosenImageUri!!.path.split('/')
            imageName = arr[arr.size - 1] + ".png"
            val file = File((activity as MainActivity).filesDir.path ,imageName) //"ava.png")// //НАЗВАНИЕ ФАЙЛА ТУТ
            val fOut = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut)
            fOut.close()
            Log.e("IN",imageName)
            val image = if (bitmap != null) Bitmap.createScaledBitmap(bitmap, 250, 250, false) else return
            imageCurrent.setImageBitmap(image)
            val client = HttpClient("95.158.11.238", 8080)// (activity as MainActivity).client
            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    try{
//                        client.connect()
//                        client.uploadImage(file,(activity as MainActivity).nickname)
                    }catch (e : ConnectException){
                        Log.e("ERROR", e.toString())
                    }
                }
            }

        }
    }

}