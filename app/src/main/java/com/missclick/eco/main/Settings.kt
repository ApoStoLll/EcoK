package com.missclick.eco.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.missclick.eco.R
import android.content.Intent

import android.app.Activity.RESULT_OK
import android.database.Cursor
import android.util.Log
import com.missclick.eco.HttpClient
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.provider.MediaStore
import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream


class Settings : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.btnChangeAva ).setOnClickListener {
            changeAvatar()
        }
    }

    private fun changeAvatar(){
        val photoPickerIntent = Intent(Intent.ACTION_GET_CONTENT)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, 1)



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            1 -> {
                if (resultCode == RESULT_OK) {
                    val chosenImageUri = data!!.data
                    Log.e("Dirrr ",chosenImageUri?.getPath())

                    val client = HttpClient("95.158.11.238", 8080)
                    val bitmap = MediaStore.Images.Media.getBitmap((activity as MainActivity).getContentResolver(), chosenImageUri)
                    val file = File((activity as MainActivity).filesDir.path + "/ava1.jpg")
                    val fOut = FileOutputStream(file)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut)
                    GlobalScope.launch {
                        withContext(Dispatchers.IO) {
                            client.connect()
                            client.uploadImage("sgsge",file,(activity as MainActivity).nickname)

                        }
                    }
                }
            }
        }


    }

}