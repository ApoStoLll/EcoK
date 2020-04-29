package com.missclick.eco.main.profile


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.missclick.eco.DBHelper
import com.missclick.eco.HttpClient

import com.missclick.eco.R
import com.missclick.eco.main.MainActivity
import com.missclick.eco.register.RegisterActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.ConnectException


class EditProfile : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.btnChangeAva).setOnClickListener {
            changeAvatar()
        }
    }

    private fun changeAvatar(){
        val photoPickerIntent = Intent(Intent.ACTION_GET_CONTENT)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val chosenImageUri = data!!.data
            val bitmap = MediaStore.Images.Media.getBitmap((activity as MainActivity).contentResolver, chosenImageUri)
            val arr = chosenImageUri!!.path?.split('/')
            lateinit var file : File
            if ( arr != null) file = File((activity as MainActivity).filesDir.path ,arr[arr.size - 1] + ".png")
            val fOut = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut)
            fOut.close()
            val client = HttpClient("95.158.11.238", 8080)// (activity as MainActivity).client
            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    try{
                        client.connect()
                        client.uploadImage(file,(activity as MainActivity).nickname,false)
                    }catch (e : ConnectException){
                        Log.e("ERROR", e.toString())
                    }
                }
            }
        }
    }

}
