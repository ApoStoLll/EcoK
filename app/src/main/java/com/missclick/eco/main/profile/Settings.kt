package com.missclick.eco.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.missclick.eco.R
import android.content.Intent

import android.app.Activity.RESULT_OK
import com.missclick.eco.HttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.provider.MediaStore
import android.graphics.Bitmap
import android.util.Log
import com.google.android.material.transition.MaterialFadeThrough
import com.missclick.eco.DBHelper
import com.missclick.eco.main.MainActivity
import com.missclick.eco.register.RegisterActivity
import java.io.File
import java.io.FileOutputStream
import java.net.ConnectException


class Settings : androidx.fragment.app.Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        exitTransition = MaterialFadeThrough.create(requireContext())
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.btnExit).setOnClickListener {
            exit()
        }
    }

    private fun exit(){
        try{
            val activity = activity as MainActivity
            for ( myFile in  activity.filesDir.listFiles()){
                if (myFile.isFile) myFile.delete()
                Log.e("Dir",myFile.toString())
            }
            val dbHelper = DBHelper(activity)
            val db = dbHelper.writableDatabase
            db.delete("posts", null, null)
        }catch (e : ConnectException){ // другая ошибка
            Log.e("ERROR", e.toString())
        }
        val intent = Intent(activity as MainActivity, RegisterActivity::class.java)
        startActivity(intent)
    }

}