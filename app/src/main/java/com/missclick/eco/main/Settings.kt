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
                    Log.e("Dir ",chosenImageUri?.getPath())
                    //val filename = chosenImageUri?.getPath().split('/')[]
                    val client = HttpClient("95.158.11.238", 8080)

                    GlobalScope.launch {
                        withContext(Dispatchers.IO) {
                            client.connect()
                            client.uploadImage("sgsge",chosenImageUri?.getPath(),(activity as MainActivity).nickname)

                        }
                    }
                }
            }
        }


    }

}