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
import com.missclick.eco.DBHelper
import com.missclick.eco.main.MainActivity
import com.missclick.eco.register.RegisterActivity
import kotlinx.android.synthetic.main.fragment_profile_edit_post.*
import java.io.File
import java.io.FileOutputStream
import java.net.ConnectException


class Settings : androidx.fragment.app.Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.btnChangeAva).setOnClickListener {
            changeAvatar()
        }
        view.findViewById<Button>(R.id.btnExit).setOnClickListener {
            exit()
        }
    }

    private fun changeAvatar(){
        val photoPickerIntent = Intent(Intent.ACTION_GET_CONTENT)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, 1)
    }

    private fun exit(){
        try{
            val activity = activity as MainActivity
            for ( myFile in  activity.filesDir.listFiles()){
                if (myFile.isFile) myFile.delete()
                Log.e("Dir",myFile.toString())
            }
            val dbHelper = DBHelper(activity)
            val db = dbHelper.getWritableDatabase()
            db.delete("posts", null, null)
        }catch (e : ConnectException){ // другая ошибка
            Log.e("ERROR", e.toString())
        }
        val intent = Intent(activity as MainActivity, RegisterActivity::class.java)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val chosenImageUri = data!!.data
            val bitmap = MediaStore.Images.Media.getBitmap((activity as MainActivity).getContentResolver(), chosenImageUri)
            val arr = chosenImageUri!!.path.split('/')
            val file = File((activity as MainActivity).filesDir.path ,arr[arr.size - 1] + ".png") //"ava.png")// //НАЗВАНИЕ ФАЙЛА ТУТ
            val fOut = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut)
            fOut.close()
            val client = HttpClient("95.158.11.238", 8080)// (activity as MainActivity).client
            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    try{
                                client.connect()
                                client.uploadImage(file,(activity as MainActivity).nickname)
                    }catch (e : ConnectException){
                                Log.e("ERROR", e.toString())
                    }
                }
            }

        }
    }

}